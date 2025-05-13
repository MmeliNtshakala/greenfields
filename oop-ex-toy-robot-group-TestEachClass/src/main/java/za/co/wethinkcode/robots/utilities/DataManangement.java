package za.co.wethinkcode.robots.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import za.co.wethinkcode.robots.robot.Robot;

public class DataManangement {

  private static BufferedReader bufferedReader;
  private static PrintWriter printWriter;

  public static void sendCommandToJSON(Socket socket, String command, String name) {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode jsonNode = mapper.createObjectNode();
    ArrayNode argumentsList = mapper.createArrayNode();

    jsonNode.put("robot", name);

    String[] commandParts = command.split(" ");

    if (commandParts.length > 1) {
      jsonNode.put("command", commandParts[0]);
      for (int i = 1; i < commandParts.length; i++) {
        argumentsList.add(commandParts[i]);
      }
    } else {
      jsonNode.put("command", command);
    }
    jsonNode.set("arguments", argumentsList);

    try {
      printWriter = new PrintWriter(socket.getOutputStream(), true);
      printWriter.println(jsonNode.toString());
    } catch (IOException e) {
      System.out.println("Error sending command to JSON: " + e.getMessage());
    }
  }

  public static JSONObject responseJsonToClient(String message, Robot robot) {

    JSONObject response = new JSONObject();
    response.put("result", "OK");
    response.put("message", message);
    response.put("State", robot.getStateAsJSON());
    return response;
  }

  public static JsonNode readJSON(Socket socket) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jsonNode = null;

    try {
      bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String jsonString = bufferedReader.readLine();
      if (jsonString != null) {
        jsonNode = mapper.readTree(jsonString);
      }
    } catch (IOException e) {
    }

    return jsonNode;
  }
}