package za.co.wethinkcode.robots.robot;

public enum RobotProperties{
  SNIPER(80,36),
  FLASH(50,65);

  private final int firePower;
  private final int speed;

  RobotProperties(int firePower, int speed){
    this.firePower = firePower;
    this.speed = speed;
  }

  public int getFirePower() {
    return firePower;
  }

  public int getSpeed() {
    return speed;
  }
}