package cc.altoya.settlements.Commands.Settlement;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.ChatUtil;

public class CommandNew {
  public static boolean handle(Player player, String[] args) {
    if (!player.hasPermission("settlements.new")) {
      ChatUtil.sendErrorMessage(player, "You don't have permission to run this command.");
      return true;
    }
    if (args.length != 1) {
      ChatUtil.sendErrorMessage(player, "This command only requires one argument. /settlement new");
      return true;
    }

    return true;
  }
}

