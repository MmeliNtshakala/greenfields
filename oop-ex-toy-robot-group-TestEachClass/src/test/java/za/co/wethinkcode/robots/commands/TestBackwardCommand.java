package za.co.wethinkcode.robots.commands;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robots.client.ClientBookKeeper;
import za.co.wethinkcode.robots.client.ClientHandler;
import za.co.wethinkcode.robots.robot.Position;
import za.co.wethinkcode.robots.robot.Robot;
import za.co.wethinkcode.robots.robot.Direction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TestBackwardCommand {

    private Robot robot;
    private ClientHandler mockHandler;
    private ClientBookKeeper mockBookKeeper;

    @BeforeEach
    public void setUp() {
        // Initialize a test robot with a name and type
        robot = new Robot("TestBot", "basic");
        robot.setPosition(new Position(0, 0));
        robot.setDirection(Direction.NORTH);

        mockHandler = mock(ClientHandler.class);
        mockBookKeeper = mock(ClientBookKeeper.class);

        when(mockHandler.getRobot()).thenReturn(robot);
        when(mockHandler.getKeeper()).thenReturn(mockBookKeeper);
    }

    @Test
    public void testExecuteBackwardCommand() {
        BackwardCommand command = new BackwardCommand("3");
        boolean result = command.execute(mockHandler);

        assertTrue(result, "Command should execute successfully.");
        assertEquals(new Position(0, -3), robot.getPosition(), "Robot should move 3 steps back.");

        String expectedMessage = "Moved back by 3 step.";
        JSONObject expectedResponse = new JSONObject();
        expectedResponse.put("message", expectedMessage);
        expectedResponse.put("robot", robot.getStateAsJSON());

        verify(mockBookKeeper).responseMessage(eq(mockHandler), eq(expectedResponse.toString()));
    }
}