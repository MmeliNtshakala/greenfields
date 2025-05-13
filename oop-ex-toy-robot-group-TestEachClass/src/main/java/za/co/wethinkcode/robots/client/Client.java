package za.co.wethinkcode.robots.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import za.co.wethinkcode.robots.utilities.DataManangement;

public final class Client {
  private static java.util.Scanner scanner;
  private Socket socket;
  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
  private String clientName;

  private Client(Socket socket, String clientName) {
    this.socket = socket;
    this.clientName = clientName;
    try {
      this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    } catch (IOException e) {
      System.err.println("ERROR INITIALIZING THE CLIENT: " + e.getMessage());
      closeEverything(socket, bufferedReader, bufferedWriter);
    }
  }

  private void sendMessage() {
    try {
      bufferedWriter.write(clientName);
      bufferedWriter.newLine();
      bufferedWriter.flush();

      try (Scanner scanner2 = new Scanner(System.in)) {
        while (socket.isConnected()) {
          if (scanner2.hasNextLine()) {
            String messageToSend = scanner2.nextLine().trim();
            if (!messageToSend.isEmpty()) {
              DataManangement.sendCommandToJSON(socket, messageToSend, clientName);
            }
          } else {
            System.out.println("Exiting...");
            break;
          }
        }
      }
    } catch (IOException e) {
      System.err.println("ERROR SENDING MESSAGE: " + e.getMessage());
      closeEverything(socket, bufferedReader, bufferedWriter);
    }
  }

  private void listenMessages() {
    new Thread(() -> {
      try {
        String messageFromServer;
        while (socket.isConnected() && (messageFromServer = bufferedReader.readLine()) != null) {
          System.out.println(messageFromServer);
        }
      } catch (IOException e) {
        System.out.println("THE SERVER HAS DISCONNECTED!");
      } finally {
        closeEverything(socket, bufferedReader, bufferedWriter);
        System.exit(0);
      }
    }).start();
  }

  private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
    try {
      if (bufferedReader != null)
        bufferedReader.close();
      if (bufferedWriter != null)
        bufferedWriter.close();
      if (socket != null)
        socket.close();
    } catch (IOException e) {
      System.err.println("ERROR CLOSING THE SOCKET: " + e.getMessage());
    }
  }

  private static boolean isHostValid(String address) {
    if (address.equals("localhost"))
      return true;
    String[] addressComponents = address.trim().split("\\.");

    if (addressComponents.length != 4)
      return false;

    for (String component : addressComponents) {
      try {
        int value = Integer.parseInt(component);
        if (value < 0 || value > 255)
          return false;
      } catch (NumberFormatException e) {
        return false;
      }
    }
    return true;
  }

  private static String getHost() {
    while (true) {
      String host = getInput("\nENTER THE HOST ADDRESS ");
      if (isHostValid(host))
        return host;
      System.out.println("\nInvalid IPv4 Address!!!\n");
    }
  }

  private static int getPort() {
    while (true) {
      String portInput = getInput("\nENTER THE PORT NUMBER ");

      try {
        int port = Integer.parseInt(portInput);
        if (port >= 1 && port <= 65535) {
          return port;
        } else {
          System.out.println("\nInvalid PORT, must be between 1 and 65535.\n");
        }
      } catch (NumberFormatException e) {
        System.out.println("\nInvalid PORT, enter a valid number.\n");
      }
    }
  }

  private static String getUsername() {
    while (true) {
      String username = getInput("\nENTER YOUR NAME ");

      // Check if the username is not empty
      if (username == null || username.trim().isEmpty()) {
        System.out.println("Username cannot be empty. Please try again.");
        continue;
      }

      // Return the valid username
      return username.trim(); // Trim any leading/trailing whitespace
    }
  }

  private static String getInput(String prompt) {
    System.out.print(prompt + ": ");
    String input = scanner.nextLine();
    while (input.isEmpty()) {
      System.out.print("Input cannot be empty. \n" + prompt + ": ");
      input = scanner.nextLine();
    }
    return input.trim();
  }

  public static void main(String[] args) {
    scanner = new Scanner(System.in);

    String host = getHost();
    int portNumber = getPort();
    String username = getUsername();

    try {
      Socket socket = new Socket(host, portNumber);
      Client client = new Client(socket, username);
      client.listenMessages();
      client.sendMessage();
    } catch (IOException e) {
      System.out.println("ERROR CONNECTING TO THE SERVER!");
    }

  }
}