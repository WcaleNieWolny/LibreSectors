package me.wcaleniewolny.libresectors.okaeri;

import eu.okaeri.persistence.document.Document;
import java.util.Optional;
import java.util.UUID;
import me.wcaleniewolny.libresectors.api.user.LibreUser;

public class LibreUserDocumentWrapper extends Document {
    private String name = ""; //Make sure that null string is not in the database - can cause problems with base64 decoding

    private String serializedInventory = "";

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

    public LibreUserDocumentWrapper fromUser(LibreUser user) {
        this.name = user.getName();
        return this;
    }

}
