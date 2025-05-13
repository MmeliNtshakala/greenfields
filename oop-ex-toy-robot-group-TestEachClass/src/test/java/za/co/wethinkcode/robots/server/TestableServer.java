package za.co.wethinkcode.robots.server;

import java.io.IOException;

public class TestableServer extends Server {
  public TestableServer(int port) throws IOException {
    super(port);
  }

  @Override
  public void startServer() {
    super.startServer();
  }

  @Override
  public void closeServer() {
    super.closeServer();
  }

  @Override
  public boolean isServerRunning() {
    return super.isServerRunning();
  }

  public static int getOpenPort() {
    return Server.getOpenPort();
  }
}
