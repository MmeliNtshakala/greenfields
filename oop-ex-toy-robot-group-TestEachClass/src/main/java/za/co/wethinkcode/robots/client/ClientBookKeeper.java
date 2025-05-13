package za.co.wethinkcode.robots.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import za.co.wethinkcode.robots.utilities.Ansi;

public class ClientBookKeeper {

  private static volatile ClientBookKeeper instance = null;
  private static ConcurrentHashMap<String, ClientHandler> clientHandlers;
  private static volatile ConcurrentHashMap<String, CopyOnWriteArrayList<ClientHandler>> teams;

  private ClientBookKeeper() {
    clientHandlers = new ConcurrentHashMap<>();
  }

  public static synchronized ClientBookKeeper getInstance() {
    ClientBookKeeper localInstance = instance;
    if (localInstance == null) {
      synchronized (ClientBookKeeper.class) {
        localInstance = instance;
        if (localInstance == null) {
          instance = localInstance = new ClientBookKeeper();
        }
      }
    }
    return localInstance;
  }

  public static ConcurrentHashMap<String, ClientHandler> getClientHandlers() {
    return clientHandlers;
  }

  public void addClient(String username, ClientHandler clientHandler) {
    clientHandlers.put(username, clientHandler);
  }

  public void removeClient(String username) {
    clientHandlers.remove(username);
  }

  public static boolean handlerPresent(String username) {
    return clientHandlers != null && clientHandlers.containsKey(username);
  }

  public synchronized boolean isTeamAvailable(String teamName) {
    return teams != null && teams.containsKey(teamName);
  }

  public synchronized void addOnTeam(String teamName, ClientHandler clientHandler) {
    if (teams == null) {
      teams = new ConcurrentHashMap<>();
    }
    teams.computeIfAbsent(teamName, k -> new CopyOnWriteArrayList<>()).add(clientHandler);
  }

  public synchronized void removeFromTeam(String teamName, ClientHandler clientHandler) {
    if (teams != null && teams.containsKey(teamName)) {
      CopyOnWriteArrayList<ClientHandler> teamMembers = teams.get(teamName);
      if (teamMembers != null) {
        teamMembers.remove(clientHandler);
        if (teamMembers.isEmpty()) {
          teams.remove(teamName);
        }
      }
    }
  }

  public synchronized CopyOnWriteArrayList<ClientHandler> getTeamMembers(String teamName) {
    if (teams != null) {
      return teams.get(teamName);
    }
    return null;
  }

  private synchronized void sendMessage(ClientHandler client, String message) throws IOException {
    BufferedWriter bufferedWriter = client.getBufferedWriter();
    bufferedWriter.write(message);
    bufferedWriter.newLine();
    bufferedWriter.flush();
  }

  public synchronized void messageTeamMembers(ClientHandler sender, String message) {
    String teamName = sender.getTeamName();
    if (teamName != null && teams != null && teams.containsKey(teamName)) {
      CopyOnWriteArrayList<ClientHandler> teamMembers = teams.get(teamName);
      for (ClientHandler member : teamMembers) {
        if (!member.equals(sender)) {
          try {
            sendMessage(member, message);
          } catch (IOException e) {
            System.err.println("ERROR SENDING MESSAGE: " + e.getMessage());
          }
        }
      }
    }
  }

  public synchronized void broadcastMessage(ClientHandler sender, String message) {
    for (ClientHandler client : clientHandlers.values()) {
      try {
        if (!client.equals(sender)) {
          sendMessage(client, message);
        } else {
          sendMessage(client, Ansi.cyan((Ansi.welcomeText())));
        }
      } catch (IOException e) {
        System.err.println("ERROR SENDING MESSAGE: " + e.getMessage());
      }
    }
  }

  public synchronized void responseMessage(ClientHandler toSender, String message) {
    BufferedWriter bufferedWriter = toSender.getBufferedWriter();
    try {
      bufferedWriter.write(message);
      bufferedWriter.newLine();
      bufferedWriter.flush();
    } catch (IOException e) {
      System.err.println("ERROR SENDING MESSAGE: " + e.getMessage());
    }
  }

  public static ConcurrentHashMap<String, ClientHandler> getAllHandlers() {
    return clientHandlers;
  }

}
