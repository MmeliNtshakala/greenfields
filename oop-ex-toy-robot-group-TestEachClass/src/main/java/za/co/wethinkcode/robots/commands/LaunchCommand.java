package za.co.wethinkcode.robots.commands;

import za.co.wethinkcode.robots.client.ClientHandler;

public class LaunchCommand extends Commands {

  @Override
  public boolean execute(ClientHandler target) {
    target.launchRobot(this.getArgument());
    return true;
  }

  public LaunchCommand(String argument) {
    super("launch", argument);
  }

}
