package cc.altoya.settlements.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SettlementsUtil {
  public static ResultSet getSettlement(UUID playerUUID){
    ResultSet resultSet = null;
    String query = "SELECT * FROM settlements WHERE uuids LIKE '%?%'";
    Connection conn = DatabaseUtil.getConnection();
    try {
      PreparedStatement statement = conn.prepareStatement(query);
      statement.setString(0, playerUUID.toString());

      resultSet = statement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    
    return resultSet;
  }
}
