package za.co.wethinkcode.robots.commands;

import za.co.wethinkcode.robots.client.ClientBookKeeper;
import za.co.wethinkcode.robots.client.ClientHandler;

public class CreateCommand extends Commands {
  @Override
  public boolean execute(ClientHandler target) {
    ClientBookKeeper keeper = target.getKeeper();
    String teamName = this.getArgument();
    if (keeper.isTeamAvailable(teamName)) {
      keeper.responseMessage(target, "TEAM ALREADY EXISTS!");
    } else {
      target.setTeamName(teamName);
      keeper.addOnTeam(teamName, target);
      keeper.responseMessage(target, "YOU HAVE CREATED THE TEAM : " + teamName);
    }

    return true;
  }

  public CreateCommand(String argument) {
    super("create", argument);
  }
}
