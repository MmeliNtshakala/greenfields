package za.co.wethinkcode.robots.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class TestClient {
  private ByteArrayOutputStream outContent;
  private InputStream inputStream;

  @BeforeEach
  public void setUp() {
    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
  }

  @AfterEach
  public void closeStreams() {
    try {
      System.out.println("Closing output stream...");
      System.setOut(null);
      outContent.close();
    } catch (IOException e) {
      fail("Failed to close output stream: " + e.getMessage());
    }
  }

  @Test
  public void testInvalidHost() {
    System.out.println("\nTesting Invalid Host address...");
    inputStream = new ByteArrayInputStream("dodi\nlocalhost\n9988\nmxo".getBytes());
    System.setIn(inputStream);
    Client.main(new String[] {});

    String output = outContent.toString();
    assertTrue(output.contains("Invalid IPv4 Address!!!"),
        "Expected error message for invalid host address not found.");
  }

  @Test
  public void testInvalidPortDigitEntered() {
    System.out.println("\nTesting Invalid Port...");
    inputStream = new ByteArrayInputStream("localhost\n-5\n9988\nmxo".getBytes());
    System.setIn(inputStream);
    Client.main(new String[] {});

    String output = outContent.toString();
    assertTrue(output.contains("Invalid PORT, must be between 1 and 65535."),
        "Expected error message for invalid port number not found.");
  }

  @Test
  public void testInvalidPortStringEntered() {
    System.out.println("\nTesting Invalid Port...");
    inputStream = new ByteArrayInputStream("localhost\nhey\n9988\nmxo".getBytes());
    System.setIn(inputStream);
    Client.main(new String[] {});

    String output = outContent.toString();
    assertTrue(output.contains("Invalid PORT, enter a valid number."),
        "Expected error message for invalid port number not found.");
  }

  @Test
  public void testNoConnection() {
    System.out.println("\nTesting No Connection...");
    inputStream = new ByteArrayInputStream("localhost\n9988\nmxo".getBytes());
    System.setIn(inputStream);
    Client.main(new String[] {});

    String output = outContent.toString();
    assertTrue(output.contains("ERROR CONNECTING TO THE SERVER!"),
        "Expected error message for no connection not found.");
  }

}
