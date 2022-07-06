package me.wcaleniewolny.libresectors.okaeri.recovery;

import com.mongodb.event.ServerHeartbeatFailedEvent;
import com.mongodb.event.ServerHeartbeatSucceededEvent;
import com.mongodb.event.ServerMonitorListener;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import me.wcaleniewolny.libresectors.api.storage.RecoveryStorageManager;

public abstract class MongoRecoveryProtocol implements ServerMonitorListener {
    private final AtomicBoolean databaseAvailable = new AtomicBoolean(true);
    private final AtomicBoolean recoveryInProgress = new AtomicBoolean(false);

    private final FlatRecoveryStorageManager recoveryStorageManager;

    protected MongoRecoveryProtocol(FlatRecoveryStorageManager recoveryStorageManager) {
        this.recoveryStorageManager = recoveryStorageManager;
    }

    public void init() {
        this.recoveryStorageManager.initRecovery();
    }

    public boolean databaseAvailable() {
        return this.databaseAvailable.get();
    }

    @Override
    public void serverHeartbeatFailed(ServerHeartbeatFailedEvent event) {
        this.changeDatabaseAvailability(false);
        ServerMonitorListener.super.serverHeartbeatFailed(event);
    }

    @Override
    public void serverHeartbeatSucceeded(ServerHeartbeatSucceededEvent event) {
        this.changeDatabaseAvailability(true);
        ServerMonitorListener.super.serverHeartbeatSucceeded(event);
    }

    private void changeDatabaseAvailability(Boolean newState) {
        if (this.databaseAvailable() != newState) {
            this.databaseAvailable.set(newState);

            if (!newState) {
                System.out.println("[LibreSectors] Starting recovery protocol!");
                this.startRecoveryProtocol(this.recoveryInProgress);
            }
            else {
                System.out.println("[LibreSectors] Stopping recovery protocol!");
                this.stopRecoveryProtocol(this.recoveryInProgress);
            }

        }
    }

    public RecoveryStorageManager getRecoveryStorageManager() {
        return this.recoveryStorageManager;
    }

    public abstract void startRecoveryProtocol(AtomicBoolean recoveryInProgress);

    public abstract void stopRecoveryProtocol(AtomicBoolean recoveryInProgress);

    public abstract File getDataFolder();
}
