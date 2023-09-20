package cc.altoya.settlements.Commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.DatabaseConnections;

public class CommandClaim {
  public static boolean handleClaim(Player player, String[] args) {
    if (!player.hasPermission("settlements.claim")) {
      return false;
    }
    if (args.length != 1) {
      return false;
    }
    String uuid = player.getUniqueId().toString();
    int x = player.getLocation().getChunk().getX();
    int y = player.getLocation().getChunk().getZ();

    int claimCount = claimCount(uuid);

    if(claimCount != 0 && !connectedToCurrentClaims(uuid, x, y)){
      player.sendMessage("Your claims must be within 1 chunk of each other.");
      return false;
    }

    if(claimCount > 9){
      player.sendMessage("You have hit your claim limit.");
      return false;
    }

    if(!claimWithinBoundary(x, y)){
      player.sendMessage("You must claim within X chunks of spawn.");
      return false;
    }

    String trusted = "{" + uuid + "}";

    String query = "INSERT INTO claims (uuid, x, y, trusted) VALUES (?, ?, ?, ?)";
    try {
      PreparedStatement statement = DatabaseConnections.getConnection().prepareStatement(query);
      statement.setString(1, uuid);
      statement.setInt(2, x);
      statement.setInt(3, y);
      statement.setString(4, trusted);

      int rowsAffected = statement.executeUpdate();

      if (rowsAffected > 0) {
        player.sendMessage("Chunk claimed successfully.");
      }

    } catch (SQLIntegrityConstraintViolationException e) {
      player.sendMessage("You have already claimed this land.");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }

  public static boolean claimWithinBoundary(int x, int y){
    int claimBoundary = 4;
    boolean withinX = Math.abs(claimBoundary) - Math.abs(x) > 0;
    boolean withinY = Math.abs(claimBoundary) - Math.abs(y) > 0;

    return withinX && withinY;
  }

  public static int claimCount(String uuid){
    String query = "SELECT * FROM claims WHERE uuid = ?";
    int count = 0;
    try {
      PreparedStatement selectStatement = DatabaseConnections.getConnection().prepareStatement(query);
      selectStatement.setString(1, uuid);
      ResultSet resultSet = selectStatement.executeQuery();
    
      while (resultSet.next()) {
        count++;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return count;
  }

  public static boolean connectedToCurrentClaims(String uuid, int x, int y){
    String query = "SELECT * FROM claims WHERE uuid = ?";
    try {
      PreparedStatement selectStatement = DatabaseConnections.getConnection().prepareStatement(query);
      selectStatement.setString(1, uuid);
      ResultSet resultSet = selectStatement.executeQuery();
    
      while (resultSet.next()) {
        int currentX = resultSet.getInt("x");
        int currentY = resultSet.getInt("y");
        boolean isWithinOneOfX = Math.abs(x - currentX) <= 1;
        boolean isWithinOneOfY = Math.abs(y - currentY) <= 1;
        if(isWithinOneOfX && isWithinOneOfY){
          return true;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }
}

