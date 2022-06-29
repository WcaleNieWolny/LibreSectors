package me.wcaleniewolny.libresectors.okaeri;

import eu.okaeri.persistence.document.Document;
import java.util.UUID;
import me.wcaleniewolny.libresectors.api.user.LibreUser;

public class LibreUserDocumentWrapper extends Document {
    private UUID uuid;
    private String name;

    private byte[] serializedInventory = new byte[0];

    private LibreUserDocumentWrapper() {

    }

    public LibreUserDocumentWrapper(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public LibreUser convertToUser() {
        return LibreUser.create(this.uuid, this.name);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public byte[] getSerializedInventory() {
        return this.serializedInventory;
    }

    public void setSerializedInventory(byte[] serializedInventory) {
        this.serializedInventory = serializedInventory;
    }

    public static LibreUserDocumentWrapper fromUser(LibreUser user) {
        return new LibreUserDocumentWrapper(user.getUuid(), user.getName());
    }

}
