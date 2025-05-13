package za.co.wethinkcode.robots.commands;

import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.utilities.DataManangement;
import org.json.JSONObject;
import za.co.wethinkcode.robots.client.ClientBookKeeper;
import za.co.wethinkcode.robots.client.ClientHandler;

public class ShutdownCommand extends Commands {
  public ShutdownCommand() {
    super("shutdown");
  }

  @Override
  public boolean execute(ClientHandler target) {
    Robot robot = target.getRobot();
    ClientBookKeeper keeper = target.getKeeper();

    String message = " Shutting down...";
    JSONObject response = DataManangement.responseJsonToClient(message, robot);
    keeper.responseMessage(target, response.toString());
    return true;
  }
}
