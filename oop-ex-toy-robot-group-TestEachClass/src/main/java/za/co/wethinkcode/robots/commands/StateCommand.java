package za.co.wethinkcode.robots.commands;

import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.client.*;
import org.json.JSONObject;
import org.json.JSONArray;

public class StateCommand extends Commands {
  public StateCommand() {
    super("state");
  }

  @Override
  public boolean execute(ClientHandler target) {
    Robot robot = target.getRobot();
    ClientBookKeeper keeper = target.getKeeper();

    JSONObject response = new JSONObject();
    response.put("result", "OK");

    JSONObject robotData = new JSONObject();
    robotData.put("name", robot.getName());
    robotData.put("alive", robot.getState());
    robotData.put("position", new JSONArray(new int[] { robot.getPosition().getX(), robot.getPosition().getY() }));
    robotData.put("direction", robot.getDirection().toString());
    robotData.put("type", robot.getProperties());

    response.put("State", robotData);

    keeper.responseMessage(target, response.toString());
    return true;
  }
}
