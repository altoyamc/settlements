package cc.altoya.settlements.Commands.Chunk;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DatabaseUtil;

public class CommandUntrust {
  public static boolean handle(Player player, String[] args) {
    if (!player.hasPermission("settlements.untrust")) {
      ChatUtil.sendErrorMessage(player, "You don't have permission to run this command.");
      return true;
    }
    if (args.length != 2) {
      ChatUtil.sendErrorMessage(player, "This command only requires two argument. /chunk untrust {username}");
      return true;
    }

    String targetName = args[1];
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetName);

    if (targetPlayer == null) {
      ChatUtil.sendErrorMessage(player, "This player couldn't be found.");
      return true;
    }

    UUID targetUUID = targetPlayer.getUniqueId();

    String uuid = player.getUniqueId().toString();
    int x = player.getLocation().getChunk().getX();
    int y = player.getLocation().getChunk().getZ();

    String query = "SELECT * FROM claims WHERE x = ? AND y = ?";

    try {
      PreparedStatement selectStatement = DatabaseUtil.getConnection().prepareStatement(query);
      selectStatement.setInt(1, x);
      selectStatement.setInt(2, y);
      ResultSet resultSet = selectStatement.executeQuery();

      if (resultSet.next()) {
        String existingTrusted = resultSet.getString("trusted").replaceAll("}", "");

        if (!existingTrusted.contains(targetUUID.toString())) {
          ChatUtil.sendErrorMessage(player, "This person isn't trusted.");
          return true;
        }

        String[] trusted = existingTrusted.toString().split(",");

        String trustedTotal = "{" + trusted[0];

        for (int i = 1; i < trusted.length; i++) {
          if (!trusted[i].equals(targetUUID.toString())) {
            trustedTotal += "," + trusted[i];
          }
        }
        trustedTotal += "}";

        String updateQuery = "UPDATE claims SET trusted = ? WHERE x = ? AND y = ? AND uuid = ?";
        PreparedStatement updateStatement = DatabaseUtil.getConnection().prepareStatement(updateQuery);
        updateStatement.setString(1, trustedTotal);
        updateStatement.setInt(2, x);
        updateStatement.setInt(3, y);
        updateStatement.setString(4, uuid);

        int rowsUpdated = updateStatement.executeUpdate();

        if (rowsUpdated > 0) {
          ChatUtil.sendSuccessMessage(player, "Player has been untrusted successfully.");
        } else {
          ChatUtil.sendErrorMessage(player, "Untrusting player has failed.");
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }

}
