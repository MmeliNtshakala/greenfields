package za.co.wethinkcode.robots.robot;

import org.json.JSONObject;

public enum RobotProperties {
  SNIPER(80, 36),
  FLASH(50, 65);

  private final int firePower;
  private final int speed;

  RobotProperties(int firePower, int speed) {
    this.firePower = firePower;
    this.speed = speed;
  }

  public int getFirePower() {
    return firePower;
  }

  public int getSpeed() {
    return speed;
  }

  public JSONObject toJSON() {
    JSONObject json = new JSONObject();
    json.put("firePower", this.firePower);
    json.put("speed", this.speed);
    json.put("type", this.name());
    return json;
  }
}
