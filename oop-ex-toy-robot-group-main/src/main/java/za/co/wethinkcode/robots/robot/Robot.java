package za.co.wethinkcode.robots.robot;

public class Robot{
  private final String name;
  private final RobotProperties properties;
  private boolean alive = false;

  private RobotProperties setProperty(String type){
    return RobotProperties.valueOf(type.toUpperCase());
  }

  public boolean getState(){
    return alive;
  }

  public RobotProperties getProperties(){
    return properties;
  }

  public Robot(String name, String type){
    this.name = name;
    this.properties = setProperty(type);
    this.alive = true;
  }
}