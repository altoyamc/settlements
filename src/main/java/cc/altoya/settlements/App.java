package cc.altoya.settlements;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;

import cc.altoya.settlements.Commands.MainChunk;
import cc.altoya.settlements.Events.EventProtectBlocks;
import cc.altoya.settlements.Events.EventProtectEntities;
import cc.altoya.settlements.Util.DatabaseConnections;
public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        initializeConfig();
        try {
            initializeDatabase();
        } catch (SQLException e) {
        }

        //How to register commands
        this.getCommand("chunk").setExecutor(new MainChunk()); 

        //How to register eventListeners
        this.getServer().getPluginManager().registerEvents(new EventProtectBlocks(), this);
        this.getServer().getPluginManager().registerEvents(new EventProtectEntities(), this);

    }

    private void initializeDatabase() throws SQLException{
        DatabaseConnections.initializeConnection();

        String query = "CREATE TABLE `claims` (`id` INT PRIMARY KEY AUTO_INCREMENT,`uuid` VARCHAR(36) NOT NULL,`x` INT,`y` INT, `trusted` TEXT, UNIQUE KEY `unique_claim` (`uuid`, `x`, `y`))";

        try (PreparedStatement statement = DatabaseConnections.getConnection().prepareStatement(query)) {
          // Execute the query to create the table
          statement.executeUpdate();
        }
    
    }

    private void initializeConfig(){
        File configFile = new File(getDataFolder(), "config.yml");

        if(configFile.exists()){
            return;
        }

        getConfig().addDefault("databasePort", "3306");
        getConfig().addDefault("databaseUrl", "0.0.0.0");
        getConfig().addDefault("databaseUsername", "root");
        getConfig().addDefault("databasePassword", "password");
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    
}