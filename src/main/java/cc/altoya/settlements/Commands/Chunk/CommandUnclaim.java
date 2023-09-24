package cc.altoya.settlements.Commands.Chunk;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DatabaseUtil;

public class CommandUnclaim {
  public static boolean handle(Player player, String[] args) {
    if (!player.hasPermission("settlements.unclaim")) {
      ChatUtil.sendErrorMessage(player, "You don't have permission to run this command.");
      return true;
    }
    if (args.length != 1) {
      ChatUtil.sendErrorMessage(player, "This command only requires one argument. /chunk unclaim");
      return true;
    }
    String uuid = player.getUniqueId().toString();
    int x = player.getLocation().getChunk().getX();
    int y = player.getLocation().getChunk().getZ();

    String deleteQuery = "DELETE FROM claims WHERE uuid = ? AND x = ? AND y = ?";

    try {
      PreparedStatement deleteStatement = DatabaseUtil.getConnection().prepareStatement(deleteQuery);
      deleteStatement.setString(1, uuid);
      deleteStatement.setInt(2, x);
      deleteStatement.setInt(3, y);

      int rowsAffected = deleteStatement.executeUpdate();

      if (rowsAffected > 0) {
        ChatUtil.sendSuccessMessage(player, "Chunk unclaimed successfully.");
      } else {
        ChatUtil.sendErrorMessage(player, "No claim found at your current location.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }
}
