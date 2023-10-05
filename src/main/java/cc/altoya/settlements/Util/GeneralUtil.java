package cc.altoya.settlements.Util;

import org.bukkit.entity.Player;

public class GeneralUtil {
  public static boolean handlePermissionsAndArguments(Player player, String permissionRoot, String permissionChild,
      String[] args, int argsRequired, String commandString) {
    if (!player.hasPermission(permissionRoot + "." + permissionChild)) {
      ChatUtil.sendErrorMessage(player, "You don't have permission to run this command.");
      return false;
    }
    if (args.length != argsRequired) {
      ChatUtil.sendErrorMessage(player, "This command only requires " + argsRequired + " argument. " + commandString);
      return false;
    }

    return true;
  }
}
