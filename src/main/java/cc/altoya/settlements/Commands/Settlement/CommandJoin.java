package cc.altoya.settlements.Commands.Settlement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;
import cc.altoya.settlements.Util.GeneralUtil;
import cc.altoya.settlements.Util.SettlementsUtil;

public class CommandJoin {
  public static boolean handle(Player player, String[] args) {
    if(!GeneralUtil.handlePermissionsAndArguments(player, "settlements", "join", args, 2, "/settlement join {settlement-name}")){
      return true;
    }

    UUID playerUUID = player.getUniqueId();
    ResultSet currentSettlement = SettlementsUtil.getSettlementViaUUID(playerUUID);
    ResultSet newSettlement = SettlementsUtil.getSettlementViaName(args[1]);

    if(currentSettlement != null){
      ChatUtil.sendErrorMessage(player, "You are already in a settlement");
      return true;
    }

    if(newSettlement == null){
      ChatUtil.sendErrorMessage(player, "This settlement doesn't exist");
    }

    try {
      String invites = newSettlement.getString("invited_uuids");
      if(invites.contains(playerUUID.toString())){
        ChatUtil.sendErrorMessage(player, "You don't have an invite to this settlement");
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return true;
    }


    //Get settlement
    //Check user has no current settlement
    //Check user has invite in invites
    //Add user to settlement
    //Remove invite from settlement

    return true;
  }

}
