package me.wcaleniewolny.libresectors.api.storage;

import me.wcaleniewolny.libresectors.api.user.LibreUser;

public interface RecoveryStorageManager {

    void initRecovery();

    void saveUser(LibreUser user);

    void saveInventoryData(LibreUser user, byte[] data);

    /**
     * Called when primary database become available again
     */
    void recoverData();

}
