package cc.altoya.settlements.Commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DatabaseConnections;

public class CommandTrust {
  public static boolean handleTrust(Player player, String[] args) {
    if (!player.hasPermission("settlements.trust")) {
      ChatUtil.sendErrorMessage(player, "You don't have permission to run this command.");
      return true;
    }
    if (args.length != 2) {
      ChatUtil.sendErrorMessage(player, "This command only requires two argument. /chunk trust {username}");
      return true;
    }

    String targetName = args[1];
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetName);

    if (targetPlayer == null) {
      player.sendMessage("This player couldn't be found.");
      return true;
    }

    UUID targetUUID = targetPlayer.getUniqueId();

    String uuid = player.getUniqueId().toString();
    int x = player.getLocation().getChunk().getX();
    int y = player.getLocation().getChunk().getZ();

    String query = "SELECT * FROM claims WHERE x = ? AND y = ?";
    try {
      PreparedStatement selectStatement = DatabaseConnections.getConnection().prepareStatement(query);
      selectStatement.setInt(1, x);
      selectStatement.setInt(2, y);
      ResultSet resultSet = selectStatement.executeQuery();

      if (resultSet.next()) {
        String existingTrusted = resultSet.getString("trusted").replaceAll("}", "");

        if (existingTrusted.contains(targetUUID.toString())) {
          player.sendMessage("This person is already trusted.");
          return true;
        }

        existingTrusted = existingTrusted + "," + targetUUID + "}";

        String updateQuery = "UPDATE claims SET trusted = ? WHERE x = ? AND y = ? AND uuid = ?";
        PreparedStatement updateStatement = DatabaseConnections.getConnection().prepareStatement(updateQuery);
        updateStatement.setString(1, existingTrusted);
        updateStatement.setInt(2, x);
        updateStatement.setInt(3, y);
        updateStatement.setString(4, uuid);

        int rowsUpdated = updateStatement.executeUpdate();

        if (rowsUpdated > 0) {
          player.sendMessage("Player has been trusted successfully.");
        } else {
          player.sendMessage("Trusting player has failed.");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }

}
