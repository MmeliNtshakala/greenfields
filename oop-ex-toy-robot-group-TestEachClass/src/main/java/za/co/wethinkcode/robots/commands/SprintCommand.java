package za.co.wethinkcode.robots.commands;

import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.client.ClientHandler;

public class SprintCommand extends Commands {
  public SprintCommand(String argument) {
    super("sprint", argument);
  }

  @Override
  public boolean execute(ClientHandler target) {
    Robot robot = target.getRobot();
    int steps = Integer.parseInt(getArgument());
    robot.sprint(steps);
    return true;
  }
}
