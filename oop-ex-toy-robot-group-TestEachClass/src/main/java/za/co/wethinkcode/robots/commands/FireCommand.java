package za.co.wethinkcode.robots.commands;

import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.robot.Position;
import za.co.wethinkcode.robots.robot.Direction;
import za.co.wethinkcode.robots.client.ClientHandler;
import za.co.wethinkcode.robots.client.ClientBookKeeper;
import za.co.wethinkcode.robots.world.World;
import org.json.JSONObject;

public class FireCommand extends Commands {
    private static final int FIRE_RANGE = 5;

    public FireCommand() {
        super("fire");
    }
    @Override
    public boolean execute(ClientHandler target) {
        Robot robot = target.getRobot();
        ClientBookKeeper keeper = target.getKeeper();
        World world = robot.getWorld();

        if (!robot.hasBullets()) {
            JSONObject response = new JSONObject();
            response.put("result", "ERROR");
           //  response.put("message", "No bullets left. Reload to fire.");
            keeper.responseMessage(target, response.toString());
            return false;
        }
        robot.decreaseBullets();

        Position currentPosition = robot.getPosition();
        Direction direction = robot.getDirection();
        for (int i = 1; i <= FIRE_RANGE; i++) {
            Position targetPosition = currentPosition.moveForward(direction);

            for (Robot otherRobot : world.getRobots()) {
                if (otherRobot.getPosition().equals(targetPosition) && !otherRobot.equals(robot)) {
                    // otherRobot.disable();
                    JSONObject response = new JSONObject();
                    response.put("result", "OK");
                    response.put("message", "Hit robot " + otherRobot.getName() + " at position " + targetPosition);
                    keeper.responseMessage(target, response.toString());
                    return true;
                }
            }
            currentPosition = targetPosition;
        }
        JSONObject response = new JSONObject();
        response.put("result", "OK");
        response.put("message", "Missed! No robot in range.");
        keeper.responseMessage(target, response.toString());
        return true;
    }

}