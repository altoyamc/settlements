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

  public static boolean createVoteFor(String settlementName, int actionId, String[] allowedVotersList){
    String insertStatement = "INSERT INTO votes (action_id, allowed_voters, yes_count, no_count) VALUES (?, ?, 0, 0)";

    try {
      PreparedStatement statement = DatabaseUtil.getConnection().prepareStatement(insertStatement);
      statement.setInt(1, actionId);
      statement.setString(2, DatabaseUtil.getStringFromJson(allowedVotersList));

      int rowsAffected = statement.executeUpdate();

      if (rowsAffected <= 0) {
        return false;
      } else {
        return true;
      }

    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}
