package cc.altoya.settlements.Commands.Settlement;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;

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

    return true;
  }

}