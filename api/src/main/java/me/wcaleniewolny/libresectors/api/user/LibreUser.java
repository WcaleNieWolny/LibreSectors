package me.wcaleniewolny.libresectors.api.user;

import java.util.UUID;

public class LibreUser {

    private final UUID uuid;
    private final String name;
    private int sectorID = 0;

    private LibreUser(UUID uuid, String name, int sectorID) {
        this.uuid = uuid;
        this.name = name;
        this.sectorID = sectorID;
    }

    public static LibreUser create(UUID uuid, String name) {
        return new LibreUser(uuid, name, 0);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getSectorID() {
        return sectorID;
    }
}
