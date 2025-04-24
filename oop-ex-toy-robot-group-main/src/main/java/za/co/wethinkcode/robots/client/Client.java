package za.co.wethinkcode.robots.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public final class Client{
  private Socket socket;
  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
  private String clientName;
  

  public void sendMessage() throws IOException{
    bufferedWriter.write(clientName);
    bufferedWriter.newLine();
    bufferedWriter.flush();
    try (Scanner scanner = new Scanner(System.in)) {
      while (socket.isConnected()) {
          System.out.print("COMMAND : ");
          String messageToSend = scanner.nextLine();
          if(!messageToSend.equals("")){
          bufferedWriter.write(clientName + " : " + messageToSend);
          bufferedWriter.newLine();
          bufferedWriter.flush();
          }
      }
    }
  }

  public void listenMessages(){
    new Thread(() -> {
        String messageFromServer;
        while(socket.isConnected()){
            try {
                messageFromServer = bufferedReader.readLine();
                if(messageFromServer != null){
                    System.out.println(messageFromServer);
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            } 
        }
    }).start();
  }

  public final void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferWriter){
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

  public Client(Socket socket, String clientName){
    this.socket = socket;
    try {
      this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      this.clientName = clientName;
    } catch (IOException e) {
        closeEverything(socket, bufferedReader, bufferedWriter);
    }
  }
  
  public static void main(String[] args) throws IOException{
    String username;
    try (Scanner scanner = new Scanner(System.in)) {
        System.out.print("Enter your name : ");
        username = scanner.nextLine();
        Socket socket = new Socket("localhost", 9999);
        Client client = new Client(socket, username);
        client.listenMessages();
        client.sendMessage();
    }
  }
}