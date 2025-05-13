package za.co.wethinkcode.robots.commands;

import java.io.IOException;

import za.co.wethinkcode.robots.client.ClientHandler;
import za.co.wethinkcode.robots.server.Server;
import za.co.wethinkcode.robots.utilities.LOG;

public class QuitCommand extends Commands {

  public QuitCommand() {
    super("quit");
  }

  private void closeServer() {
    try {
      Server.setRunning(false);
      Server.getInstance(0).closeServer();
      LOG.addToServerLog("THE SERVER HAS BEEN CLOSED");
    } catch (IOException e) {
      System.err.println("Error while closing the server: " + e.getMessage());
    }
  }

  @Override
  public boolean execute(ClientHandler target) {
    closeServer();
    return true;
  }
}
