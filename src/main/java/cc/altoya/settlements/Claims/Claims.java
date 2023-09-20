package cc.altoya.settlements.Claims;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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

    String targetName = args[1];
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetName);

    if (targetPlayer == null) {
      player.sendMessage("This player couldn't be found.");
      return false;
    }

    UUID targetUUID = targetPlayer.getUniqueId();

    String uuid = player.getUniqueId().toString();
    int x = player.getLocation().getChunk().getX();
    int y = player.getLocation().getChunk().getZ();

    String query = "SELECT * FROM claims WHERE x = ? AND y = ?";
    try {
      PreparedStatement selectStatement = DatabaseConnections.getConnection().prepareStatement(query);
      selectStatement.setInt(1, x);
      selectStatement.setInt(2, y);
      ResultSet resultSet = selectStatement.executeQuery();

      if (resultSet.next()) {
        String existingTrusted = resultSet.getString("trusted").replaceAll("}", "");

        if (existingTrusted.contains(targetUUID.toString())) {
          player.sendMessage("This person is already trusted.");
          return false;
        }

        existingTrusted = existingTrusted + "," + targetUUID + "}";

        String updateQuery = "UPDATE claims SET trusted = ? WHERE x = ? AND y = ? AND uuid = ?";
        PreparedStatement updateStatement = DatabaseConnections.getConnection().prepareStatement(updateQuery);
        updateStatement.setString(1, existingTrusted);
        updateStatement.setInt(2, x);
        updateStatement.setInt(3, y);
        updateStatement.setString(4, uuid);

        int rowsUpdated = updateStatement.executeUpdate();

        if (rowsUpdated > 0) {
          player.sendMessage("Player has been trusted successfully.");
        } else {
          player.sendMessage("Trusting player has failed.");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
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

    String targetName = args[1];
    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetName);

    if (targetPlayer == null) {
      player.sendMessage("This player couldn't be found.");
      return false;
    }

    UUID targetUUID = targetPlayer.getUniqueId();

    String uuid = player.getUniqueId().toString();
    int x = player.getLocation().getChunk().getX();
    int y = player.getLocation().getChunk().getZ();

    String query = "SELECT * FROM claims WHERE x = ? AND y = ?";

    try {
      PreparedStatement selectStatement = DatabaseConnections.getConnection().prepareStatement(query);
      selectStatement.setInt(1, x);
      selectStatement.setInt(2, y);
      ResultSet resultSet = selectStatement.executeQuery();

      if (resultSet.next()) {
        String existingTrusted = resultSet.getString("trusted").replaceAll("}", "");

        if (!existingTrusted.contains(targetUUID.toString())) {
          player.sendMessage("This person isn't trusted.");
          return false;
        }

        String[] trusted = existingTrusted.toString().split(",");

        String trustedTotal = "{" + trusted[0];

        for (int i = 1; i < trusted.length; i++) {
          if (!trusted[i].equals(targetUUID.toString())) {
            trustedTotal += "," + trusted[i];
          }
        }
        trustedTotal += "}";

        String updateQuery = "UPDATE claims SET trusted = ? WHERE x = ? AND y = ? AND uuid = ?";
        PreparedStatement updateStatement = DatabaseConnections.getConnection().prepareStatement(updateQuery);
        updateStatement.setString(1, trustedTotal);
        updateStatement.setInt(2, x);
        updateStatement.setInt(3, y);
        updateStatement.setString(4, uuid);

        int rowsUpdated = updateStatement.executeUpdate();

        if (rowsUpdated > 0) {
          player.sendMessage("Player has been untrusted successfully.");
        } else {
          player.sendMessage("Untrusting player has failed.");
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
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
