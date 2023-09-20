package cc.altoya.settlements.Commands;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.DatabaseConnections;

public class CommandUnclaim {
  public static boolean handleUnclaim(Player player, String[] args) {
    if (!player.hasPermission("settlements.unclaim")) {
      return false;
    }
    if (args.length != 1) {
      return false;
    }
    String uuid = player.getUniqueId().toString();
    int x = player.getLocation().getChunk().getX();
    int y = player.getLocation().getChunk().getZ();

    String deleteQuery = "DELETE FROM claims WHERE uuid = ? AND x = ? AND y = ?";

    try {
      PreparedStatement deleteStatement = DatabaseConnections.getConnection().prepareStatement(deleteQuery);
      deleteStatement.setString(1, uuid);
      deleteStatement.setInt(2, x);
      deleteStatement.setInt(3, y);

      int rowsAffected = deleteStatement.executeUpdate();

      if (rowsAffected > 0) {
        player.sendMessage("Chunk unclaimed successfully.");
      } else {
        player.sendMessage("No claim found at your current location.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }

}
