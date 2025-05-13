package za.co.wethinkcode.robots.commands;

import za.co.wethinkcode.robots.client.ClientBookKeeper;
import za.co.wethinkcode.robots.client.ClientHandler;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.utilities.DataManangement;
import org.json.JSONObject;

public class BackwardCommand extends Commands {
  private final int steps;

  @Override
  public boolean execute(ClientHandler target) {
    Robot robot = target.getRobot();
    ClientBookKeeper keeper = target.getKeeper();

    for (int x = 0; x < steps; x++) {
      robot.moveBack();
    }

    String message = "Moved back by " + steps + " step.";
    JSONObject response = DataManangement.responseJsonToClient(message, robot);
    keeper.responseMessage(target, response.toString());
    return true;
  }

  public BackwardCommand(String argument) {
    super("back", argument);
    this.steps = parseSteps(argument);
  }

  private int parseSteps(String argument) {
    try {
      return Integer.parseInt(argument.trim());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid step count for 'backward' command: " + argument);
    }
  }
}
