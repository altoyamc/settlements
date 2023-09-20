package cc.altoya.settlements.Commands;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import cc.altoya.settlements.DatabaseConnections;

public class CommandUnclaimAll {
  public static boolean handleUnclaimAll(Player player, String[] args) {
    if (!player.hasPermission("settlements.trust")) {
      return false;
    }
    if (args.length != 1) {
      return false;
    }

    String uuid = player.getUniqueId().toString();

    String deleteQuery = "DELETE FROM claims WHERE uuid = ?";

    try {
      PreparedStatement deleteStatement = DatabaseConnections.getConnection().prepareStatement(deleteQuery);
      deleteStatement.setString(1, uuid);

      int rowsAffected = deleteStatement.executeUpdate();

      if (rowsAffected > 0) {
        player.sendMessage("All your claims have been successfully removed.");
      } else {
        player.sendMessage("No claims found for your UUID.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }

}
