package me.wcaleniewolny.libresectors.spigot;

import me.wcaleniewolny.libresectors.api.config.LibreConfiguration;
import me.wcaleniewolny.libresectors.api.storage.StorageManager;
import me.wcaleniewolny.libresectors.okaeri.OkaeriStorageManager;

public class StorageFactory {

    private StorageManager manager = null;
    private final LibreConfiguration libreConfiguration;

    public StorageFactory(LibreConfiguration libreConfiguration) {
        this.libreConfiguration = libreConfiguration;
    }

    public StorageManager getManager() {
        if (this.manager == null) {
            this.manager = new OkaeriStorageManager(this.libreConfiguration);
        }
        return this.manager;
    }
}
