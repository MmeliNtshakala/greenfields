package za.co.wethinkcode.robots.commands;

import com.fasterxml.jackson.databind.JsonNode;

import za.co.wethinkcode.robots.client.ClientHandler;

public abstract class Commands {
  private final String name;
  private final String argument;

  public abstract boolean execute(ClientHandler target);

  public Commands(String name) {
    this(name, " ");
  }

  public Commands(String name, String argument) {
    this.name = name.trim().toLowerCase();
    this.argument = argument != null ? argument.trim() : "";
  }

  public String getName() {
    return name;
  }

  public String getArgument() {
    return argument;
  }

  @Override
  public String toString() {
    return name + (argument.isEmpty() ? "" : " " + argument);
  }

  public static Commands create(JsonNode commands) {

    String commandName = commands.get("command").asText().trim().toLowerCase();
    JsonNode argumentList = commands.get("arguments");
    String argument = null;
    if (argumentList != null && argumentList.isArray()) {
      StringBuilder args = new StringBuilder();
      for (JsonNode arg : argumentList) {
        args.append(arg.asText()).append(" ");
      }
      argument = args.toString().trim();
    } else if (argumentList != null) {
      argument = argumentList.asText().trim();
    }

    switch (commandName) {
      case "off" -> {
        return new ShutdownCommand();
      }
      case "help" -> {
        return new HelpCommand();
      }
      case "forward" -> {
        return new ForwardCommand(argument);
      }
      case "back" -> {
        return new BackwardCommand(argument);
      }
      case "right" -> {
        return new TurnCommand("right");
      }
      case "left" -> {
        return new TurnCommand("left");
      }
      case "sprint" -> {
        return new SprintCommand(argument);
      }
      case "look" -> {
        return new LookCommand();
      }
      case "state" -> {
        return new StateCommand();
      }
      case "launch" -> {
        return new LaunchCommand(argument);
      }
      case "join" -> {
        return new JoinCommand(argument);
      }
      case "create" -> {
        return new CreateCommand(argument);
      }
      case "robots" -> {
        return new RobotCommand();
      }
      case "quit" -> {
        return new QuitCommand();
      }
      case "fire" -> { 
                return new FireCommand();
            }
      default -> throw new IllegalArgumentException("Unsupported command:" + commandName + argument);

    }
  }
}
