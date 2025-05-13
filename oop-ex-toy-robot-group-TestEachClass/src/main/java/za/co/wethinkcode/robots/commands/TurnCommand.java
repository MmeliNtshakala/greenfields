package za.co.wethinkcode.robots.commands;

import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.utilities.DataManangement;
import za.co.wethinkcode.robots.robot.Direction;
import za.co.wethinkcode.robots.client.ClientBookKeeper;
import za.co.wethinkcode.robots.client.ClientHandler;
import org.json.JSONObject;

public class TurnCommand extends Commands {
  private final String turnDirection;

  @Override
  public boolean execute(ClientHandler target) {
    Robot robot = target.getRobot();
    Direction currentDirection = robot.getCurrentDirection();
    ClientBookKeeper keeper = target.getKeeper();

    if ("left".equalsIgnoreCase(turnDirection)) {
      currentDirection = currentDirection.turnLeft();
    } else if ("right".equalsIgnoreCase(turnDirection)) {
      currentDirection = currentDirection.turnRight();
    } else {
      JSONObject errorResponse = new JSONObject();
      errorResponse.put("result", "ERROR");
      errorResponse.put("message", "Invalid turn direction.");
      keeper.responseMessage(target, errorResponse.toString());
      return false;
    }

    robot.setCurrentDirection(currentDirection);

    String message = "Turned " + turnDirection + ".";
    JSONObject response = DataManangement.responseJsonToClient(message, robot);
    keeper.responseMessage(target, response.toString());
    return true;
  }

  public TurnCommand(String argument) {
    super("turn", argument);
    this.turnDirection = argument.trim();
  }
}
