package za.co.wethinkcode.robots.commands;

import za.co.wethinkcode.robots.client.ClientHandler;
import za.co.wethinkcode.robots.client.ClientBookKeeper;
import za.co.wethinkcode.robots.robot.Robot;

public class TestClientHandler extends ClientHandler {
    private final Robot robot;

    public TestClientHandler(Robot robot) {
        super(null, null);
        this.robot = robot;
    }

    @Override
    public Robot getRobot() {
        return this.robot;
    }

    @Override
    public ClientBookKeeper getKeeper() {
        return null;
    }
}