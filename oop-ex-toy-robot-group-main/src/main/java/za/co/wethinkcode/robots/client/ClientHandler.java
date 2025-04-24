package za.co.wethinkcode.robots.client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.world.World;

public class ClientHandler implements Runnable{

  public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
  private final Socket socket;
  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
  private String clientName;
  private static World world;
  private Robot robot;
  

  @Override
  public void run() {
    String messageFromClient;
    while (socket.isConnected()) {
      try {
        messageFromClient = bufferedReader.readLine().toLowerCase();
        if(messageFromClient.contains("launch")){
          String[] messageComponents = messageFromClient.split(" ");
          String type = messageComponents[messageComponents.length - 1];
          launchRobot(type);
        }
        else{
        updateRobot(messageFromClient);
        }
      } catch (IOException e) {
        closeEverything(socket, bufferedReader, bufferedWriter);
        break;
      }
    }
  }
  public final void broadcastMessage(String messageToSend){
    // this method will be responsible for sending the message to all the clients.
    // this method well be implemented on the GUI for chatting.
    // will edit this code for private team chatting and communication

    for (ClientHandler clientHandler : clientHandlers) {
      if (!clientHandler.clientName.equals(clientName)) {
        if (messageToSend.contains("@")) {
          String[] parts = messageToSend.split("@", 2);
          String recipientName = parts[1].split(" ")[0].trim();
          String message = parts[1].split(" ")[1].trim();
          if (clientHandler.clientName.equals(recipientName)) {
            try {
              clientHandler.bufferedWriter.write(clientName + message);
              clientHandler.bufferedWriter.newLine();
              clientHandler.bufferedWriter.flush();
            } catch (IOException e) {
              closeEverything(socket, bufferedReader, bufferedWriter);
            }
          }
        } else{
        try {
          clientHandler.bufferedWriter.write(messageToSend);
          clientHandler.bufferedWriter.newLine();
          clientHandler.bufferedWriter.flush();
        } catch (IOException e) {
          closeEverything(socket, bufferedReader, bufferedWriter);
        }
      }
    }
  }}

  public final void broadcastUpdate(){
    // this method will be responsible for sending the update to all the clients
  }

  public void updateRobot(String message) throws IOException{
    // this function will be responsible for updating the robot in the world based on the instructions

    String command = message.split(":")[1].trim();
    try {
          this.bufferedWriter.write("you commanded : " + command);
          this.bufferedWriter.newLine();
          this.bufferedWriter.flush();
    }catch (IOException e){}
  }

  public void removeClientHandler(){
    clientHandlers.remove(this);
    broadcastMessage("SERVER: " + clientName + " has left the WORLD");
  }

  private void launchRobot(String robotType) throws IOException{

    // this method will be responsible for launching the robot in the world
    // this method will be implemented on the GUI for launching the robot.
    // will edit this code for private team launching and communication

    if(this.robot != null && this.robot.getState()){
      this.bufferedWriter.write("YOU HAVE A ROBOT PRESENT IN THE WORLD");
      this.bufferedWriter.newLine();
      this.bufferedWriter.flush();
      return;
    }

    switch (robotType) {
        case "sniper":
            this.robot = new Robot(clientName, "sniper");
            break;
        case "flash":
            this.robot = new Robot(clientName, "flash");
            break;
        default:
            this.bufferedWriter.write("INVALID ROBOT TYPE");
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
            return;
      }
    broadcastMessage(clientName + " has launched their robot to the WORLD!");
  }
  
  public final void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferWriter){
    removeClientHandler();
    try {
      if (bufferedReader != null) {
        bufferedReader.close();
      }
      if (bufferWriter != null) {
        bufferWriter.close();
      }
      if (socket != null) {
        socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void setWorld(World world) {
    ClientHandler.world = world;
  }

  public ClientHandler(Socket socket) throws IOException {
    this.socket = socket;
    try {
      this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      this.clientName = bufferedReader.readLine();
      clientHandlers.add(this);
      broadcastMessage(clientName + " has entered the WORLD!");
    } catch (IOException e) {
      closeEverything(socket, bufferedReader, bufferedWriter);
    }
  }
}
