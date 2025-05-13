package za.co.wethinkcode.robots.commands;

import za.co.wethinkcode.robots.client.ClientBookKeeper;
import za.co.wethinkcode.robots.client.ClientHandler;
import za.co.wethinkcode.robots.utilities.Ansi;
import za.co.wethinkcode.robots.utilities.ReadPropertiesFile;

public class HelpCommand extends Commands {

  public HelpCommand() {
    super("help");
  }

  @Override
  public boolean execute(ClientHandler target) {
  
    ClientBookKeeper keeper = target.getKeeper();
    keeper.responseMessage(target, HelpMessage());
    return true;
  }

  public static String HelpMessage() {
    ReadPropertiesFile file = new ReadPropertiesFile("printStatements.properties");
    String output = file.returnValue("helpCommandText");
    return Ansi.green(output);
  }
}