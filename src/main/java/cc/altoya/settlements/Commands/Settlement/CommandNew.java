package cc.altoya.settlements.Commands.Settlement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DatabaseUtil;

public class CommandNew {
  public static boolean handle(Player player, String[] args) {
    if (!player.hasPermission("settlements.new")) {
      ChatUtil.sendErrorMessage(player, "You don't have permission to run this command.");
      return true;
    }
    if (args.length != 2) {
      ChatUtil.sendErrorMessage(player, "This command only requires two argument. /settlement new {settlement-name}");
      return true;
    }

    String settlementName = args[1];
    String description = "Default description, use /settlement description {descriptionText}";
    String uuids = DatabaseUtil.getStringFromJson(player.getUniqueId().toString());
    String votesIds = "{}";

    String query = "INSERT INTO settlements (name, description, uuids, votesIds) VALUES (?, ?, ?, ?)";
    try {
      PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(query);
      statement.setString(1, settlementName);
      statement.setString(2, description);
      statement.setString(3, uuids);
      statement.setString(4, votesIds);

      int rowsAffected = statement.executeUpdate();

      if (rowsAffected > 0) {
        ChatUtil.sendSuccessMessage(player, "Settlement created successfully.");
      }
    } catch (SQLIntegrityConstraintViolationException e) {
      ChatUtil.sendErrorMessage(player, "This Settlement already exists.");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }
}

