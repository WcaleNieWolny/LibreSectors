package me.wcaleniewolny.libresectors.okaeri.recovery;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import me.wcaleniewolny.libresectors.api.storage.RecoveryStorageManager;
import me.wcaleniewolny.libresectors.api.user.LibreUser;
import me.wcaleniewolny.libresectors.okaeri.LibreUserDocumentWrapper;

public class FlatRecoveryStorageManager implements RecoveryStorageManager {
    private File saveFolder;
    private Gson gson;

    public FlatRecoveryStorageManager(File saveFolder) {
        this.saveFolder = saveFolder;
    }

    @Override
    public void initRecovery() {
        this.createFolder(this.saveFolder);
        this.gson = new Gson();
    }

    @Override
    public void saveUser(LibreUser user) {
        File saveFile = new File(this.saveFolder, String.format("%s.json", user.getUuid().toString()));
        this.createFile(saveFile);
        try {
            Files.write(saveFile.toPath(), this.gson.toJson(user).getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveInventoryData(LibreUser user, byte[] data) {
        user.setSerializedInventory(Base64.getEncoder().encodeToString(data));
    }

    @Override
    public void recoverData() {
        //TODO!
    }

    private void createFolder(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void createFile(File file) {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            file.createNewFile();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
