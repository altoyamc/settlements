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
import cc.altoya.settlements.Util.GeneralUtil;

public class CommandTrust {
  public static boolean handle(Player player, String[] args) {
    if(!GeneralUtil.handlePermissionsAndArguments(player, "settlements", "trust", args, 2, "/chunk trust {username}")){
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

        if (existingTrusted.contains(targetUUID.toString())) {
          ChatUtil.sendErrorMessage(player, "This person is already trusted.");
          return true;
        }

        existingTrusted = existingTrusted + "," + targetUUID + "}";

        String updateQuery = "UPDATE claims SET trusted = ? WHERE x = ? AND y = ? AND uuid = ?";
        PreparedStatement updateStatement = DatabaseUtil.getConnection().prepareStatement(updateQuery);
        updateStatement.setString(1, existingTrusted);
        updateStatement.setInt(2, x);
        updateStatement.setInt(3, y);
        updateStatement.setString(4, uuid);

        int rowsUpdated = updateStatement.executeUpdate();

        if (rowsUpdated > 0) {
          ChatUtil.sendSuccessMessage(player, "Player has been trusted successfully.");
        } else {
          ChatUtil.sendErrorMessage(player, "Trusting player has failed.");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }
}
