package za.co.wethinkcode.robots.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import za.co.wethinkcode.robots.client.ClientBookKeeper;
import za.co.wethinkcode.robots.client.ClientHandler;
import za.co.wethinkcode.robots.utilities.Ansi;
import za.co.wethinkcode.robots.utilities.LOG;
import za.co.wethinkcode.robots.world.World;

public class Server {
  private static ServerSocket serverSocket = null;
  public static volatile boolean isRunning = false;
  private static Server instance;

  // Private constructor to prevent direct instantiation
  protected Server(int PORT) throws IOException {
    Server.serverSocket = new ServerSocket(PORT);
    Server.serverSocket.setReuseAddress(true);
  }

  public static synchronized Server getInstance(int PORT) throws IOException {
    if (instance == null) {
      instance = new Server(PORT);
    }
    return instance;
  }

  protected void startServer() {
    LOG.addToServerLog("SERVER STARTED ON PORT : " + serverSocket.getLocalPort());
    World world = new World(); // This is the part where the world is created and initialized.
    ClientHandler.setWorld(world); // This part gives every handler access to the world.
    LOG.addToServerLog("WORLD CREATED : " + world.toString());
    // Please use this for printing to terminal, different colors are available
    Ansi.print(Ansi.cyan("Welcome Message from Server...."));
    Ansi.print(Ansi.red(world.dumpWorld()));
    Ansi.print(Ansi.welcomeText());
    Thread thread = null;
    try {
      Server.isRunning = true;
      while (isRunning && !serverSocket.isClosed()) {
        Socket socket = serverSocket.accept(); // Accepts the connection from the client.
        ClientHandler clientHandler = new ClientHandler(socket); // Assigns a handler to the client.
        thread = new Thread(clientHandler);
        thread.start(); // This part tells the handler to start its job, which launches the client's
                        // robot to the world.
      }
    } catch (IOException e) {
      if (isRunning) {
        LOG.addToServerLog("ERROR WHILE RUNNING THE SERVER : " + e.getMessage());
      }
    } finally {
      ClientHandler.setWorld(null);
      closeServer(); // This part closes the server when it is done.
      try {
        if (thread != null)
          thread.join();
      } catch (InterruptedException e) {
      }
    }
  }

  public void closeServer() {
    isRunning = false;
    try {
      disconnectAllClients();

      if (serverSocket != null && !serverSocket.isClosed()) {
        serverSocket.close();
        LOG.addToServerLog("SERVER HAS BEEN CLOSED.");
      }

      instance = null;
    } catch (IOException e) {
      LOG.addToServerLog("ERROR CLOSING THE SERVER : " + e.getMessage());
    }
  }

  private void disconnectAllClients() {
    ConcurrentHashMap<String, ClientHandler> allHandlers = ClientBookKeeper.getClientHandlers();
    if (allHandlers == null)
      return;
    for (String username : allHandlers.keySet()) {
      ClientHandler handler = allHandlers.get(username);
      try {
        if (handler != null) {
          handler.closeConnection(); // Ensure the client connection is closed
          ClientBookKeeper.getInstance().removeClient(username); // Remove the client from the bookkeeper
        }
      } catch (IOException e) {
        LOG.addToServerLog("ERROR DISCONNECTING CLIENT (" + username + "): " + e.getMessage());
      }
    }
    LOG.addToServerLog("ALL CLIENTS HAVE BEEN DISCONNECTED.");
  }

  protected boolean isServerRunning() {
    return isRunning;
  }

  protected boolean isThereAnyClientConnected() {
    return !ClientBookKeeper.getAllHandlers().isEmpty();
  }

  protected static int getOpenPort() {
    // This method finds a free port for the server to use.
    try (ServerSocket socket = new ServerSocket(0)) {
      socket.setReuseAddress(true);
      return socket.getLocalPort();
    } catch (IOException e) {
      throw new RuntimeException("UNABLE TO FIND A FREE PORT : ", e);
    }
  }

  public static void setRunning(boolean run) {
    isRunning = run;
  }

  public static void main(String[] args) {
    try {
      Server server = Server.getInstance(9999);
      Runtime.getRuntime().addShutdownHook(new Thread(server::closeServer));
      server.startServer();
    } catch (IOException e) {
      LOG.addToServerLog("ERROR? STARTING THE SERVER: " + e.getMessage());
    }
  }
}