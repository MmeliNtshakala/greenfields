package za.co.wethinkcode.robots.commands;

import za.co.wethinkcode.robots.robot.Position;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.utilities.DataManangement;
import za.co.wethinkcode.robots.client.ClientBookKeeper;
import za.co.wethinkcode.robots.client.ClientHandler;
import za.co.wethinkcode.robots.world.World;
import org.json.JSONObject;

public class ForwardCommand extends Commands {
  private final int steps;

  @Override
  public boolean execute(ClientHandler target) {
    Robot robot = target.getRobot();
    World world = robot.getWorld();
    ClientBookKeeper keeper = target.getKeeper();

    for (int x = 0; x < steps; x++) {
      Position nextPosition = robot.getPosition().moveForward(robot.getDirection());

      if (!world.isPositionAllowed(nextPosition)) {
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("result", "ERROR");
        errorResponse.put("message", "Movement blocked by an obstacle or edge");
        keeper.responseMessage(target, errorResponse.toString());
        return false;
      }
      robot.setPosition(nextPosition);
    }

    String message = "Moved forward by " + steps + " step.";
    JSONObject response = DataManangement.responseJsonToClient(message, robot);
    keeper.responseMessage(target, response.toString());
    return true;
  }

  public ForwardCommand(String argument) {
    super("forward", argument);
    this.steps = parseSteps(argument);
  }

  private int parseSteps(String argument) {
    try {
      return Integer.parseInt(argument.trim());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid step count for 'forward' command: " + argument);
    }
  }
}
