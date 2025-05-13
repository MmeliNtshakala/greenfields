package za.co.wethinkcode.robots.commands;

import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;
import za.co.wethinkcode.robots.client.ClientBookKeeper;
import za.co.wethinkcode.robots.client.ClientHandler;
import za.co.wethinkcode.robots.robot.Robot;

public class RobotCommand extends Commands {
  RobotCommand() {
    super("robots");
  }

  @Override
  public boolean execute(ClientHandler target) {
    // Retrieve all client handlers from the ClientBookKeeper
    ConcurrentHashMap<String, ClientHandler> allRobotHandlers = ClientBookKeeper.getClientHandlers();

    // Iterate over all handlers and print the robot state as JSON
    for (ClientHandler handler : allRobotHandlers.values()) {
      Robot robot = handler.getRobot();
      if (robot != null) { // Ensure the handler has an associated robot
        JSONObject robotState = robot.getStateAsJSON();
        System.out.println(robotState.toString()); // Print robot state to standard output
      } else {
        System.err.println("Handler for client " + handler.getClientName() + " does not have an associated robot.");
      }
    }
    return true;
  }

}
