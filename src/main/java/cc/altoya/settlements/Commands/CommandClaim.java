package cc.altoya.settlements.Commands;

import java.sql.PreparedStatement;
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

}
