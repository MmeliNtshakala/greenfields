package za.co.wethinkcode.robots.robot;

public enum Direction {
  NORTH, EAST, SOUTH, WEST;

  public Direction turnLeft() {
    switch (this) {
      case NORTH -> {
        return WEST;
      }
      case WEST -> {
        return SOUTH;
      }
      case SOUTH -> {
        return EAST;
      }
      case EAST -> {
        return NORTH;
      }
      default -> throw new IllegalArgumentException("Direction doesn't exist.");
    }
  }

  public Direction turnRight() {
    switch (this) {
      case NORTH -> {
        return EAST;
      }
      case EAST -> {
        return SOUTH;
      }
      case SOUTH -> {
        return WEST;
      }
      case WEST -> {
        return NORTH;
      }
      default -> throw new IllegalArgumentException("Direction doesn't exist.");
    }
  }

  @Override
  public String toString() {
    return this.name();
  }

  public String toJSON() {
    return this.toString();
  }
}
