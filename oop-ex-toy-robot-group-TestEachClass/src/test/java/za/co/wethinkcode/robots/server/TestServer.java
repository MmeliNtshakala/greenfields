package za.co.wethinkcode.robots.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

public class TestServer {
  private TestableServer server;
  ByteArrayOutputStream outContent;

  @BeforeEach
  public void setUp() {
    try {
      System.out.println("Setting up the server for testing...");
      int port = TestableServer.getOpenPort();
      server = new TestableServer(port);
      System.out.println("Server setup complete.");

      outContent = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outContent));
    } catch (IOException e) {
      fail("Failed to create server: " + e.getMessage());
    }
  }

  @AfterEach
  public void closeStreams() {
    try {
      System.setOut(null);
      outContent.close();
      server.closeServer();
    } catch (IOException e) {
      fail("Failed to close output stream: " + e.getMessage());
    }
  }

  @Test
  public void testServer() {
    try {
      System.out.println("Testing server functionality...");

      assertFalse(server.isServerRunning(), "Server should not be running before startServer() is called.");

      Thread serverThread = new Thread(() -> {
        try {
          server.startServer();
        } catch (Exception e) {
          fail("Exception in server thread: " + e.getMessage());
        }
      });
      serverThread.start();

      Thread.sleep(500);
      assertTrue(server.isServerRunning(), "Server should be running after startServer() is called.");

      server.closeServer();
      serverThread.join(1000);
      assertFalse(server.isServerRunning(), "Server should not be running after closeServer() is called.");

      String output = outContent.toString();
      assertTrue(output.contains("SERVER IS STARTING..."), "Output should contain 'SERVER IS STARTING...'");
      assertTrue(output.contains("THE WORLD OF SIZE"), "Output should contain 'THE WORLD OF SIZE'");
      assertTrue(output.contains("SERVER HAS BEEN CLOSED."), "Output should contain 'SERVER HAS BEEN CLOSED.'");
      assertTrue(output.contains("SERVER HAS BEEN CLOSED."), "Output should contain 'SERVER HAS BEEN CLOSED.'");

    } catch (InterruptedException e) {
      fail("Exception during test: " + e.getMessage());
    }
  }
}
