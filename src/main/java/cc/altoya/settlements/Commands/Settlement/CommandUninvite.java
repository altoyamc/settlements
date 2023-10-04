package cc.altoya.settlements.Commands.Settlement;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;

public class CommandUninvite {
  public static boolean handle(Player player, String[] args) {
    if (!player.hasPermission("settlements.uninvite")) {
      ChatUtil.sendErrorMessage(player, "You don't have permission to run this command.");
      return true;
    }
    if (args.length != 2) {
      ChatUtil.sendErrorMessage(player, "This command only requires two argument. /settlement uninvite {username}");
      return true;
    }

    //Get settlement from player
    //Check has an invite currently for user
    //Start vote for user being removed from invites

    return true;
  }

}
