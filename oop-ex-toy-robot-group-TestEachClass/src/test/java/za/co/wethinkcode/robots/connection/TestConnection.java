package za.co.wethinkcode.robots.connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robots.client.Client;
import za.co.wethinkcode.robots.server.TestableServer;

public class TestConnection {
  private TestableServer server;
  private static final int PORT = 9999;
  private ByteArrayOutputStream outContent;
  private Thread serverThread;

  @BeforeEach
  public void setUp() throws IOException {
    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    serverThread = new Thread(() -> {
      try {
        server = new TestableServer(PORT);
        server.startServer();
      } catch (IOException e) {
        fail("Failed to start server: " + e.getMessage());
      }
    });

    serverThread.start();
  }

  @AfterEach
  public void tearDown() throws IOException {
    server.closeServer();
    System.setOut(null);
  }

  @Test
  public void testClientServerClosed() {
    System.out.println("\nTesting Client Server Closed...");
    InputStream inputStream = new ByteArrayInputStream(("localhost\n" + PORT + "\nmxo").getBytes());
    System.setIn(inputStream);
    Client.main(new String[] {});

    server.closeServer();
    try {
      serverThread.join(2000);
      // clientThread.join(2000);
    } catch (InterruptedException e) {
    }

    String output = outContent.toString();
    System.out.println("Output: " + output);
    // assertTrue(output.contains("THE SERVER HAS DISCONNECTED!"), "Expected server
    // closed message not found.");
    // System.out.println("Server closed successfully. Test passed.");
  }
}
