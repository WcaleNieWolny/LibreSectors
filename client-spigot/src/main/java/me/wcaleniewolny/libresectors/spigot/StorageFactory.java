package me.wcaleniewolny.libresectors.spigot;

import me.wcaleniewolny.libresectors.api.config.LibreConfiguration;
import me.wcaleniewolny.libresectors.api.storage.StorageManager;
import me.wcaleniewolny.libresectors.okaeri.OkaeriStorageManager;
import org.bukkit.plugin.java.JavaPlugin;

public class StorageFactory {

    private StorageManager manager = null;
    private BukkitMongoRecoveryProtocol recoveryProtocol = null;
    private final LibreConfiguration libreConfiguration;
    private final JavaPlugin plugin;

    public StorageFactory(LibreConfiguration libreConfiguration, JavaPlugin plugin) {
        this.libreConfiguration = libreConfiguration;
        this.plugin = plugin;
    }

    public StorageManager getManager() {
        if (this.manager == null) {
            if (this.recoveryProtocol == null) {
                this.recoveryProtocol = new BukkitMongoRecoveryProtocol(this.plugin);
            }

            this.manager = new OkaeriStorageManager(this.libreConfiguration, this.recoveryProtocol);
        }
        return this.manager;
    }
}
