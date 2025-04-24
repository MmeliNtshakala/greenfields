package za.co.wethinkcode.robots.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import za.co.wethinkcode.robots.client.ClientHandler;
import za.co.wethinkcode.robots.world.World;

public class Server{
    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        System.out.println("Server is starting...");
        World world = new World();  // This is the part where the world is created and initialized.
        ClientHandler.setWorld(world);  // This part gives every handler access to the world.
        System.out.println("THE WORLD OF SIZE (" + world.getSizeOfWorld()[0] + "," + world.getSizeOfWorld()[1] +") IS CREATED!");
        try{
            while(!serverSocket.isClosed()){
                Socket socket= serverSocket.accept();  // Accepts the connection from the client.
                ClientHandler clientHandler = new ClientHandler(socket);  // Assigns a handler to the client.
                Thread thread = new Thread(clientHandler);
                thread.start();   // This part tells the handler to start its job, which launches the clients robot to the world.
            }
        }catch(IOException e){
            ClientHandler.setWorld(null);
            closeServer();
        }
    }

    public void closeServer(){
        try {
            if(serverSocket != null){
                ClientHandler.setWorld(null);
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getOpenPort() {
        // This method finds a free port for the server to use.
        // We will use this method to find a free port for the server to use. when launching the server on a different computer.
        // for now we will use port 9999.
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException("Unable to find free port", e);
        }
    }

    public static void main(String[] args) throws IOException{
        ServerSocket serverSoc = new ServerSocket(9999);
        Server server = new Server(serverSoc);
        server.startServer();
    }
}