package me.wcaleniewolny.libresectors.api.storage;

import java.util.Optional;
import java.util.UUID;
import me.wcaleniewolny.libresectors.api.user.LibreUser;

public interface StorageManager {

    void setupDatabase();

    Optional<LibreUser> loadUser(UUID uuid);

    void saveUser(LibreUser user);

    byte[] getInventoryData(LibreUser user);

    void saveInventoryData(LibreUser user, byte[] data);
}
