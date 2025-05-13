package za.co.wethinkcode.robots.world;

import java.util.ArrayList;
import java.util.List;
import za.co.wethinkcode.robots.robot.Position;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.utilities.ReadPropertiesFile;

public class World {
  final private int width;
  final private int height;
  private final List<Obstacle> obstacles;
  final private List<Robot> robots;

  public World() {

    ReadPropertiesFile files = new ReadPropertiesFile("worldConfig.properties");

    this.width = Integer.parseInt(files.returnValue("width"));
    this.height = Integer.parseInt(files.returnValue("height"));
    this.robots = new ArrayList<>();
    this.obstacles = new ArrayList<>();
    addObstacles();
  }

  private void addObstacles() {
    obstacles.add(new Obstacle(10, 10));
    obstacles.add(new Obstacle(1, 9));
    obstacles.add(new Obstacle(0, 18));
    obstacles.add(new Obstacle(12, 11));
  }

  public List<Obstacle> getObstacle() {
    return obstacles;
  }

  public boolean isPositionAllowed(Position newPosition) {
    for (Obstacle obstacle : obstacles) {
      if (obstacle.isAtPosition(newPosition.getX(), newPosition.getY())) {
        return false;
      }
    }
    return newPosition.getX() >= 0 && newPosition.getX() < width &&
        newPosition.getY() >= 0 && newPosition.getY() < height;
  }

  public Obstacle getObstacleAt(Position position) {
    for (Obstacle obstacle : obstacles) {
      if (obstacle.isAtPosition(position.getX(), position.getY())) {
        return obstacle;
      }
    }
    return null;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public void addRobot(Robot robot) {
    robots.add(robot);
  }

  public List<Robot> getRobots() {
    return robots;
  }

  public Robot getRobotAt(Position position) {
    for (Robot robot : robots) {
      if (robot.getPosition().equals(position)) {
        return robot;
      }
    }
    return null;
  }

  // returns a string representation of the world's current state
  public String dumpWorld() {
    StringBuilder info = new StringBuilder();

    info.append("Robot World Created!\n");
    info.append("World Size: ").append(width).append("x").append(height).append("\n");

    // new obstacle addition
    info.append("Obstacle at:\n");
    for (Obstacle obstacle : obstacles) {
      info.append("--at").append(obstacle.toString()).append("\n");
    }
    info.append("Robots:\n");
    for (Robot robot : robots) {
      info.append("-- ").append(robot.getName()).append("\n");
    }
    return info.toString();
  }

}
