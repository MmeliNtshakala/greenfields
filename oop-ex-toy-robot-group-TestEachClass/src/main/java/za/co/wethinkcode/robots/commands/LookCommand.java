package za.co.wethinkcode.robots.commands;

import za.co.wethinkcode.robots.client.ClientBookKeeper;
import za.co.wethinkcode.robots.client.ClientHandler;
import za.co.wethinkcode.robots.robot.Direction;
import za.co.wethinkcode.robots.robot.Position;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.world.World;
import za.co.wethinkcode.robots.world.Obstacle;
import org.json.JSONArray;
import org.json.JSONObject;

public class LookCommand extends Commands {
  private static final int DEFAULT_LOOK_RANGE = 5;

  public LookCommand() {
    super("look");
  }

  @Override
  public boolean execute(ClientHandler target) {
    Robot robot = target.getRobot();
    World world = robot.getWorld();
    Position position = robot.getPosition();
    Direction direction = robot.getDirection();
    ClientBookKeeper keeper = target.getKeeper();

    JSONObject response = new JSONObject();
    response.put("result", "OK");

    JSONObject robotData = new JSONObject();
    robotData.put("name", robot.getName());
    robotData.put("position", new JSONArray(new int[] { position.getX(), position.getY() }));
    robotData.put("direction", direction.toString());
    response.put("robot", robotData);

    JSONArray visibleObstacleArray = new JSONArray();
    JSONArray hiddenObstacleArray = new JSONArray();

    for (Obstacle obstacle : world.getObstacle()) {
      if (isVisible(position, direction, obstacle, DEFAULT_LOOK_RANGE)) {
        JSONObject obs = new JSONObject();
        obs.put("x", obstacle.getX());
        obs.put("y", obstacle.getY());
        visibleObstacleArray.put(obs);
      } else if (isInSight(position, direction, obstacle)) {
        JSONObject obs = new JSONObject();
        obs.put("x", obstacle.getX());
        obs.put("y", obstacle.getY());
        hiddenObstacleArray.put(obs);
      }
    }

    response.put("visible_obstacles", visibleObstacleArray);
    response.put("hidden_obstacles", hiddenObstacleArray);

    keeper.responseMessage(target, response.toString());
    return true;
  }

  private boolean isVisible(Position position, Direction direction, Obstacle obstacle, int range) {
    int deltaX = obstacle.getX() - position.getX();
    int deltaY = obstacle.getY() - position.getY();

    return switch (direction) {
      case NORTH -> deltaX == 0 && deltaY > 0 && deltaY <= range;
      case EAST -> deltaY == 0 && deltaX > 0 && deltaX <= range;
      case SOUTH -> deltaX == 0 && deltaY < 0 && Math.abs(deltaY) <= range;
      case WEST -> deltaY == 0 && deltaX < 0 && Math.abs(deltaX) <= range;
      default -> false;
    };
  }

  private boolean isInSight(Position position, Direction direction, Obstacle obstacle) {
    int deltaX = obstacle.getX() - position.getX();
    int deltaY = obstacle.getY() - position.getY();

    return switch (direction) {
      case NORTH -> deltaX == 0 && deltaY > 0;
      case EAST -> deltaY == 0 && deltaX > 0;
      case SOUTH -> deltaX == 0 && deltaY < 0;
      case WEST -> deltaY == 0 && deltaX < 0;
      default -> false;
    };
  }
}