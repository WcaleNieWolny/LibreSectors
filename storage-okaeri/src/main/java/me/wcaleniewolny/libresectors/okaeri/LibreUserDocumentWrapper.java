package me.wcaleniewolny.libresectors.okaeri;

import eu.okaeri.persistence.document.Document;
import java.util.UUID;
import me.wcaleniewolny.libresectors.api.user.LibreUser;

public class LibreUserDocumentWrapper extends Document {
    private String name;

    private String serializedInventory;

    private LibreUserDocumentWrapper() {

    }

    public LibreUserDocumentWrapper(String name) {
        this.name = name;
    }

    public LibreUser convertToUser() {
        return LibreUser.create(this.getUUID(), this.name);
    }

    public UUID getUUID() {
        return this.getPath().toUUID();
    }

    public String getName() {
        return this.name;
    }

    public String getSerializedInventory() {
        return this.serializedInventory;
    }

    public void setSerializedInventory(String serializedInventory) {
        this.serializedInventory = serializedInventory;
    }

    public static LibreUserDocumentWrapper fromUser(LibreUser user) {
        return new LibreUserDocumentWrapper(user.getName());
    }

}
