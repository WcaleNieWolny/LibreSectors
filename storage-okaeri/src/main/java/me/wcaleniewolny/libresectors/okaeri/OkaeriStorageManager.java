package me.wcaleniewolny.libresectors.okaeri;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import eu.okaeri.configs.json.simple.JsonSimpleConfigurer;
import eu.okaeri.persistence.PersistenceCollection;
import eu.okaeri.persistence.PersistencePath;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.persistence.mongo.MongoPersistence;
import eu.okaeri.persistence.repository.RepositoryDeclaration;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import me.wcaleniewolny.libresectors.api.config.LibreConfiguration;
import me.wcaleniewolny.libresectors.api.storage.StorageManager;
import me.wcaleniewolny.libresectors.api.user.LibreUser;



public class OkaeriStorageManager implements StorageManager {

    private final LibreConfiguration configuration;
    private DocumentPersistence documentPersistence;
    private PersistenceCollection collection;
    private LibreUserRepository repository;

    private final ConcurrentHashMap<UUID, byte[]> uuidToSerializedInventoryMap = new ConcurrentHashMap<>();

    public OkaeriStorageManager(LibreConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setupDatabase() {
        PersistencePath basePath = PersistencePath.of(this.configuration.getPrefix());

        MongoClientURI mongoUri = new MongoClientURI(this.configuration.getDatabaseUri());

        System.out.println(this.configuration.getDatabaseUri());

        MongoClient mongoClient = new MongoClient(mongoUri);

        if (mongoUri.getDatabase() == null) {
            throw new IllegalArgumentException("Mongo URI needs to specify the database: " + mongoUri.getURI());
        }

        this.documentPersistence = new DocumentPersistence(new MongoPersistence(basePath, mongoClient, mongoUri.getDatabase()), JsonSimpleConfigurer::new);
        this.collection = PersistenceCollection.of(LibreUserRepository.class);

        this.documentPersistence.registerCollection(this.collection);
        this.repository = RepositoryDeclaration.of(LibreUserRepository.class).newProxy(this.documentPersistence, this.collection, this.getClass().getClassLoader());
    }

    @Override
    public Optional<LibreUser> loadUser(UUID uuid) {
        Optional<LibreUserDocumentWrapper> wrapper = this.repository.findByPath(uuid);
        wrapper.ifPresent(it -> {
            if (it.getSerializedInventory() != null && !it.getSerializedInventory().isEmpty()) {
                this.uuidToSerializedInventoryMap.put(it.getUUID(), Base64.getDecoder().decode(it.getSerializedInventory()));
                return;
            }
            this.uuidToSerializedInventoryMap.put(it.getUUID(), new byte[0]);
        });

        return wrapper.map(LibreUserDocumentWrapper::convertToUser);
    }

    @Override
    public void saveUser(LibreUser user) {
        this.repository.save(this.repository.findOrCreateByPath(user.getUuid()).fromUser(user));
    }

    @Override
    public byte[] getInventoryData(LibreUser user) {
        return this.uuidToSerializedInventoryMap.get(user.getUuid());
    }

    @Override
    public void saveInventoryData(LibreUser user, byte[] data) {
        LibreUserDocumentWrapper wrapper = this.repository.findOrCreateByPath(user.getUuid()).fromUser(user);
        wrapper.setSerializedInventory(Base64.getEncoder().encodeToString(data));
        this.repository.save(wrapper);
    }
}
