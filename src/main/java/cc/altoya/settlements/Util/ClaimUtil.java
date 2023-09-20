package cc.altoya.settlements.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ClaimUtil {
  public static boolean isPlayerTrusted(Player player, Location location){

    String uuid = player.getUniqueId().toString();
    int x = location.getChunk().getX();
    int y = location.getChunk().getZ();

    String query = "SELECT * FROM claims WHERE x = ? AND y = ?";

    try {
      PreparedStatement selectStatement = DatabaseConnections.getConnection().prepareStatement(query);
      selectStatement.setInt(1, x);
      selectStatement.setInt(2, y);
      ResultSet resultSet = selectStatement.executeQuery();

      if (resultSet.next()) {
        String trusted = resultSet.getString("trusted");

        if (trusted.contains(uuid)) {
          return true;
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }
}
