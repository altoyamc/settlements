package cc.altoya.settlements;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;
public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        loadConfig();//Loads .yml

        // DatabaseConnections dbConnection = new DatabaseConnections();
        // Connection conn = dbConnection.getConnection();

        // try {
        //     conn.prepareStatement("Select *");
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }

        //How to register commands
        //this.getCommand("commandNameInYml").setExecutor(new ObjectWithOnCommandMethod()); 

        //How to register eventListeners
        //this.getServer().getPluginManager().registerEvents(new ObjectWith@EventHandlers(), this);
    }

    public void loadConfig() {
        //Get potential config file
        File configFile = new File(getDataFolder(), "config.yml");

        if(!configFile.exists()){
            getConfig().addDefault("databasePort", "3306");
            getConfig().addDefault("databaseUrl", "0.0.0.0");
            getConfig().addDefault("databaseUsername", "root");
            getConfig().addDefault("databasePassword", "password");
        }

        //Load config
        getConfig().options().copyDefaults(true);
        saveConfig();
        
    }

    
}