package cc.altoya.settlements.Commands.Settlement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.DatabaseUtil;
import cc.altoya.settlements.Util.SettlementsUtil;

public class CommandInvite {
  public static boolean handle(Player player, String[] args) {
    if (!player.hasPermission("settlements.invite")) {
      ChatUtil.sendErrorMessage(player, "You don't have permission to run this command.");
      return true;
    }
    if (args.length != 2) {
      ChatUtil.sendErrorMessage(player, "This command only requires two argument. /settlement invite {username}");
      return true;
    }

    UUID playerUUID = player.getUniqueId();
    ResultSet settlement = SettlementsUtil.getSettlement(playerUUID);

    try {
      String invites = settlement.getString("invited_uuids");
      if(invites.contains(playerUUID.toString())){
        ChatUtil.sendErrorMessage(player, "Player already has an invite.");
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      String name = settlement.getString("name");
      String[] allowedVoters = DatabaseUtil.getListFromJson(settlement.getString("uuids"));
      boolean success = SettlementsUtil.createVoteFor(name, 0, allowedVoters);
      if(success){
        ChatUtil.sendSuccessMessage(player, "Vote has been added to the votelist.");
      } else {
        ChatUtil.sendErrorMessage(player, "Vote has not been created.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    //Get settlement from player
    //Check has no invites currently for user
    //Start vote for user being added to invites

    return true;
  }

}
