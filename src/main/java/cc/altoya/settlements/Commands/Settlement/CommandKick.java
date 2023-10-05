package cc.altoya.settlements.Commands.Settlement;

import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.GeneralUtil;

public class CommandKick {
  public static boolean handle(Player player, String[] args) {
    if(!GeneralUtil.handlePermissionsAndArguments(player, "settlements", "kick", args, 2, "/settlement kick {username}")){
      return true;
    }

    //Get settlement from user
    //Check if user exists in settlement
    //Start vote on kick

    return true;
  }

}
