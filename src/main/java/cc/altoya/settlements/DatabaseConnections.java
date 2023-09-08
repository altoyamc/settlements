package cc.altoya.settlements;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DatabaseConnections {
  public Connection getConnection() {
    File file = new File(Bukkit.getServer().getPluginManager().getPlugin("pluginname").getDataFolder(), "config.yml");
    FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    String port = config.getString("databasePort");
    String ip = config.getString("databaseUrl");
    String username = config.getString("databaseUsername");
    String password = config.getString("databasePassword");

    String url = "jdbc:mysql://" + ip + ":" + port;

    try {
      // Establish the connection using the provided username and password
      return DriverManager.getConnection(url, username, password);

    } catch (SQLException e) {
      System.out.println("Database connection failed.");
      e.printStackTrace();
    }
    return null;
  }
}
