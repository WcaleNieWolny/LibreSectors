package me.wcaleniewolny.libresectors.okaeri.recovery;

import eu.okaeri.configs.json.simple.JsonSimpleConfigurer;
import eu.okaeri.persistence.PersistenceCollection;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.persistence.flat.FlatPersistence;
import eu.okaeri.persistence.repository.RepositoryDeclaration;
import java.io.File;
import java.util.Base64;
import me.wcaleniewolny.libresectors.api.storage.RecoveryStorageManager;
import me.wcaleniewolny.libresectors.api.user.LibreUser;
import me.wcaleniewolny.libresectors.okaeri.LibreUserDocumentWrapper;
import me.wcaleniewolny.libresectors.okaeri.LibreUserRepository;

public class FlatRecoveryStorageManager implements RecoveryStorageManager {

    private DocumentPersistence persistence;
    private LibreUserRepository repository;
    private final File dataFolder;

    public FlatRecoveryStorageManager(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    @Override
    public void initRecovery() {
        this.persistence = new DocumentPersistence(new FlatPersistence(new File(this.dataFolder, "storage"), ".yml"), JsonSimpleConfigurer::new);

        PersistenceCollection collection = PersistenceCollection.of(LibreUserRepository.class);
        this.persistence.registerCollection(collection);
        this.repository = RepositoryDeclaration.of(LibreUserRepository.class).newProxy(this.persistence, collection, this.getClass().getClassLoader());
    }

    @Override
    public void saveUser(LibreUser user) {
        this.repository.save(this.repository.findOrCreateByPath(user.getUuid()).fromUser(user));
    }

    @Override
    public void saveInventoryData(LibreUser user, byte[] data) {
        LibreUserDocumentWrapper wrapper = this.repository.findOrCreateByPath(user.getUuid()).fromUser(user);
        wrapper.setSerializedInventory(Base64.getEncoder().encodeToString(data));
        this.repository.save(wrapper);
    }

    @Override
    public void recoverData() {
        //TODO!
    }
}
