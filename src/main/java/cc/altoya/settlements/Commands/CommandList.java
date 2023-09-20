package cc.altoya.settlements.Commands;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import cc.altoya.settlements.Util.DatabaseConnections;

public class CommandList {
  public static boolean handleList(Player player, String[] args) {
    if (!player.hasPermission("settlements.list")) {
      return false;
    }
    if (args.length != 1) {
      return false;
    }

    String uuid = player.getUniqueId().toString();

    String query = "SELECT * FROM claims WHERE uuid = ?";
    try {
      PreparedStatement selectStatement = DatabaseConnections.getConnection().prepareStatement(query);
      selectStatement.setString(1, uuid);
      ResultSet resultSet = selectStatement.executeQuery();

      while (resultSet.next()) {
        String[] existingTrusted = resultSet.getString("trusted").replaceAll("[{}]", "").split(",");
        String names = "";
        for(String trusted : existingTrusted){
          names += Bukkit.getOfflinePlayer(UUID.fromString(trusted)).getName() + ",";
        }
        names = names.substring(0, names.length() - 1);

        int x = resultSet.getInt("x");
        int y = resultSet.getInt("y");

        player.sendMessage(x + ":" + y + ":" + names);
      }

      player.sendMessage("Test");

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return true;
  }

}
