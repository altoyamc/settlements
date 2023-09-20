package cc.altoya.settlements.Commands;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DatabaseConnections;

public class CommandUnclaimAll {
  public static boolean handleUnclaimAll(Player player, String[] args) {
    if (!player.hasPermission("settlements.trust")) {
      ChatUtil.sendErrorMessage(player, "You don't have permission to run this command.");
      return true;
    }
    if (args.length != 1) {
      ChatUtil.sendErrorMessage(player, "This command only requires one argument. /chunk unclaimall");
      return true;
    }

    String uuid = player.getUniqueId().toString();

    String deleteQuery = "DELETE FROM claims WHERE uuid = ?";

    try {
      PreparedStatement deleteStatement = DatabaseConnections.getConnection().prepareStatement(deleteQuery);
      deleteStatement.setString(1, uuid);

      int rowsAffected = deleteStatement.executeUpdate();

      if (rowsAffected > 0) {
        ChatUtil.sendSuccessMessage(player, "All your claims have been successfully removed.");
      } else {
        ChatUtil.sendErrorMessage(player, "No claims found for your UUID.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }

}
