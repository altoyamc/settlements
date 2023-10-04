package cc.altoya.settlements.Commands.Settlement;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;

public class CommandJoin {
  public static boolean handle(Player player, String[] args) {
    if (!player.hasPermission("settlements.join")) {
      ChatUtil.sendErrorMessage(player, "You don't have permission to run this command.");
      return true;
    }
    if (args.length != 2) {
      ChatUtil.sendErrorMessage(player, "This command only requires two argument. /settlement join {settlement-name}");
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
