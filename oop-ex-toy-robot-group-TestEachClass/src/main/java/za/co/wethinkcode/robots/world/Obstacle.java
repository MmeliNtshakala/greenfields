package za.co.wethinkcode.robots.world;

public class Obstacle {
  private final int x;
  private final int y;

  public Obstacle(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isAtPosition(int x, int y) {
    return this.x == x && this.y == y;
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }
}
