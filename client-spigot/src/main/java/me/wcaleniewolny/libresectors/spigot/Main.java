package me.wcaleniewolny.libresectors.spigot;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import java.io.File;
import java.util.Optional;
import java.util.logging.Level;
import me.wcaleniewolny.libresectors.api.config.LibreConfiguration;
import me.wcaleniewolny.libresectors.api.user.LibreUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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

        this.storageFactory = new StorageFactory(this.config);
        this.storageFactory.getManager().setupDatabase();

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler()
    void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Optional<LibreUser> user = this.storageFactory.getManager().loadUser(player.getUniqueId());
        if (!user.isPresent()) {
            Bukkit.getLogger().info(String.format("[LibreSectors] %s is not present! Creating in database", player.name()));
            this.storageFactory.getManager().saveUser(LibreUser.create(player.getUniqueId(), player.getName()));
        }
    }

    @EventHandler()
    void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.storageFactory.getManager().saveUser(LibreUser.create(player.getUniqueId(), player.getName()));
    }
}
