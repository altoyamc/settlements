package cc.altoya.settlements.Commands.Settlement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DatabaseUtil;
import cc.altoya.settlements.Util.SettlementsUtil;

public class CommandDelete {
  public static boolean handle(Player player, String[] args) {
    if (!player.hasPermission("settlements.delete")) {
      ChatUtil.sendErrorMessage(player, "You don't have permission to run this command.");
      return true;
    }
    if (args.length != 1) {
      ChatUtil.sendErrorMessage(player, "This command only requires one argument. /settlement delete");
      return true;
    }

    UUID playerUUID = player.getUniqueId();
    ResultSet settlement = SettlementsUtil.getSettlement(playerUUID);
    try {
      String uuids = settlement.getString("uuids");
      if(DatabaseUtil.getListFromJson(uuids).length != 1){
        ChatUtil.sendErrorMessage(player, "You must be the last player in your settlement to delete it!");
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    String deleteQuery = "DELETE FROM settlements WHERE id = ?";

    try {
      PreparedStatement deleteStatement = DatabaseUtil.getConnection().prepareStatement(deleteQuery);
      deleteStatement.setInt(1, settlement.getInt("id"));

      int rowsAffected = deleteStatement.executeUpdate();

      if (rowsAffected > 0) {
        ChatUtil.sendSuccessMessage(player, "Settlement deleted successfully.");
      } else {
        ChatUtil.sendErrorMessage(player, "Settlement not deleted, could not find settlement.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }
}
