package za.co.wethinkcode.robots.commands;

import za.co.wethinkcode.robots.client.*;

public class JoinCommand extends Commands {

  @Override
  public boolean execute(ClientHandler target) {
    ClientBookKeeper keeper = target.getKeeper();
    String teamName = this.getArgument();
    if (keeper.isTeamAvailable(teamName)) {
      target.setTeamName(teamName);
      keeper.addOnTeam(teamName, target);
      keeper.responseMessage(target, "YOU HAVE JOINED THE TEAM : " + teamName + "\n");
      keeper.messageTeamMembers(target, "\n" + (target.getClientName()) + " has joined the team\n");
    } else {
      keeper.responseMessage(target, "TEAM NOT FOUND!");
    }

    return true;
  }

  public JoinCommand(String argument) {
    super("join", argument);
  }

}
