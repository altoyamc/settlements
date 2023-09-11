package cc.altoya.settlements.Claims;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cc.altoya.settlements.DatabaseConnections;

public class Claims implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!command.getName().equalsIgnoreCase("chunk")) {
      return false;
    }
    if (!(sender instanceof Player)) {
      return false;
    }
    switch (args[0].toLowerCase()) {
      case "claim":
        return handleClaim((Player) sender, args);
      case "unclaim":
        return handleUnclaim((Player) sender, args);
      case "unclaimall":
        return handleUnclaimAll((Player) sender, args);
      case "trust":
        return handleTrust((Player) sender, args);
      case "untrust":
        return handleUntrust((Player) sender, args);
      case "list":
        return handleList((Player) sender, args);

    }

    return true;
  }

  private boolean handleClaim(Player player, String[] args) {
    if (!player.hasPermission("settlements.claim")) {
      return false;
    }
    if (args.length != 1) {
      return false;
    }
    String uuid = player.getUniqueId().toString();
    int x = player.getLocation().getChunk().getX();
    int y = player.getLocation().getChunk().getZ();
    String trusted = "{" + uuid + "}";

    String query = "INSERT INTO claims (uuid, x, y, trusted) VALUES (?, ?, ?, ?)";
    try {
      PreparedStatement statement = DatabaseConnections.getConnection().prepareStatement(query);
      statement.setString(1, uuid);
      statement.setInt(2, x);
      statement.setInt(3, y);
      statement.setString(4, trusted);

      int rowsAffected = statement.executeUpdate();

      if (rowsAffected > 0) {
        player.sendMessage("Chunk claimed successfully.");
      }

    } catch (SQLIntegrityConstraintViolationException e) {
      player.sendMessage("You have already claimed this land.");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }

  private boolean handleUnclaim(Player player, String[] args) {
    if (!player.hasPermission("settlements.unclaim")) {
      return false;
    }
    if (args.length != 1) {
      return false;
    }
    String uuid = player.getUniqueId().toString();
    int x = player.getLocation().getChunk().getX();
    int y = player.getLocation().getChunk().getZ();

    String deleteQuery = "DELETE FROM claims WHERE uuid = ? AND x = ? AND y = ?";

    try {
      PreparedStatement deleteStatement = DatabaseConnections.getConnection().prepareStatement(deleteQuery);
      deleteStatement.setString(1, uuid);
      deleteStatement.setInt(2, x);
      deleteStatement.setInt(3, y);

      int rowsAffected = deleteStatement.executeUpdate();

      if (rowsAffected > 0) {
        player.sendMessage("Chunk unclaimed successfully.");
      } else {
        player.sendMessage("No claim found at your current location.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }

  private boolean handleUnclaimAll(Player player, String[] args) {
    if (!player.hasPermission("settlements.trust")) {
      return false;
    }
    if (args.length != 1) {
      return false;
    }

    String uuid = player.getUniqueId().toString();

    String deleteQuery = "DELETE FROM claims WHERE uuid = ?";

    try {
      PreparedStatement deleteStatement = DatabaseConnections.getConnection().prepareStatement(deleteQuery);
      deleteStatement.setString(1, uuid);

      int rowsAffected = deleteStatement.executeUpdate();

      if (rowsAffected > 0) {
        player.sendMessage("All your claims have been successfully removed.");
      } else {
        player.sendMessage("No claims found for your UUID.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }

  private boolean handleTrust(Player player, String[] args) {
    if (!player.hasPermission("settlements.trust")) {
      return false;
    }
    if (args.length != 2) {
      return false;
    }

    return true;
  }

  private boolean handleUntrust(Player player, String[] args) {
    if (!player.hasPermission("settlements.untrust")) {
      return false;
    }
    if (args.length != 2) {
      return false;
    }

    return true;
  }

  private boolean handleList(Player player, String[] args) {
    if (!player.hasPermission("settlements.list")) {
      return false;
    }
    if (args.length != 1) {
      return false;
    }

    return true;
  }

}
