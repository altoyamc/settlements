package cc.altoya.settlements.Commands.Settlement;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainSettlement implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!command.getName().equalsIgnoreCase("settlement")) {
      return true;
    }
    if (!(sender instanceof Player)) {
      return true;
    }
    switch (args[0].toLowerCase()) {
      case "new":
        return CommandNew.handle((Player) sender, args);
    }

    return true;
  }
}