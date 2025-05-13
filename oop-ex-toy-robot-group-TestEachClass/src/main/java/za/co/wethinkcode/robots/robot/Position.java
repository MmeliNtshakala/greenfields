package za.co.wethinkcode.robots.robot;

import org.json.JSONArray;

public class Position {
  private final int x;
  private final int y;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Position moveForward(Direction direction) {
    return switch (direction) {
      case NORTH -> new Position(x, y + 1);
      case SOUTH -> new Position(x, y - 1);
      case EAST -> new Position(x + 1, y);
      case WEST -> new Position(x - 1, y);
      default -> this;
    };
  }

  public Position moveBack(Direction direction) {
    return switch (direction) {
      case NORTH -> new Position(x, y - 1);
      case SOUTH -> new Position(x, y + 1);
      case EAST -> new Position(x - 1, y);
      case WEST -> new Position(x + 1, y);
      default -> this;
    };
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public JSONArray toJSON() {
    return new JSONArray(new int[] { this.x, this.y });
  }

  @Override
  public String toString() {
    return "(" + x + "," + y + ")";
  }
}
