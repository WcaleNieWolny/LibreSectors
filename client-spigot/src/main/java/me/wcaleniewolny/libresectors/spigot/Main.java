package me.wcaleniewolny.libresectors.spigot;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import java.io.File;
import java.util.logging.Level;
import me.wcaleniewolny.libresectors.api.config.LibreConfiguration;
import me.wcaleniewolny.libresectors.spigot.inventory.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    private LibreConfiguration config;
    private StorageFactory storageFactory;

    @Override
    public void onEnable() {
        try {
            this.config = ConfigManager.create(LibreConfiguration.class, (it) -> {
                it.withConfigurer(new YamlBukkitConfigurer());
                it.withBindFile(new File(this.getDataFolder(), "config.yml"));
                it.saveDefaults();
                it.load(true);
            });
        }
        catch (Exception exception) {
            this.getLogger().log(Level.SEVERE, "Error loading config.yml", exception);
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        this.storageFactory = new StorageFactory(this.config, this);
        this.storageFactory.getManager().setupDatabase();

        Bukkit.getPluginManager().registerEvents(new InventoryManager(this.storageFactory, this), this);
    }
}
