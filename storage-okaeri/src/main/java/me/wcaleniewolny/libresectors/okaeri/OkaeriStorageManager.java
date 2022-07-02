package me.wcaleniewolny.libresectors.okaeri;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
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
import me.wcaleniewolny.libresectors.okaeri.recovery.MongoRecoveryProtocol;


public class OkaeriStorageManager implements StorageManager {

    private final LibreConfiguration configuration;
    private DocumentPersistence documentPersistence;
    private PersistenceCollection collection;
    private LibreUserRepository repository;
    private final ConcurrentHashMap<UUID, byte[]> uuidToSerializedInventoryMap = new ConcurrentHashMap<>();
    private final MongoRecoveryProtocol mongoRecoveryProtocol;

    public OkaeriStorageManager(LibreConfiguration configuration, MongoRecoveryProtocol mongoRecoveryProtocol) {
        this.configuration = configuration;
        this.mongoRecoveryProtocol = mongoRecoveryProtocol;
    }

    @Override
    public void setupDatabase() {
        PersistencePath basePath = PersistencePath.of(this.configuration.getPrefix());

        MongoClient mongoClient;
        ConnectionString connectionString = new ConnectionString(this.configuration.getDatabaseUri());

        mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .applyToServerSettings(builder -> builder.addServerMonitorListener(this.mongoRecoveryProtocol))
                        .build()
        );

        this.mongoRecoveryProtocol.init();

        if (connectionString.getDatabase() == null) {
            throw new IllegalArgumentException("Mongo URI needs to specify the database!");
        }

        this.documentPersistence = new DocumentPersistence(new MongoPersistence(basePath, mongoClient, connectionString.getDatabase()), JsonSimpleConfigurer::new);
        this.collection = PersistenceCollection.of(LibreUserRepository.class);

        this.documentPersistence.registerCollection(this.collection);
        this.repository = RepositoryDeclaration.of(LibreUserRepository.class).newProxy(this.documentPersistence, this.collection, this.getClass().getClassLoader());
    }

    @Override
    public Optional<LibreUser> loadUser(UUID uuid) {

        if (!this.databaseAvailable()) {
            return Optional.empty();
        }

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
        if (!this.databaseAvailable()) {
            this.mongoRecoveryProtocol.getRecoveryStorageManager().saveUser(user);
        }

        this.repository.save(this.repository.findOrCreateByPath(user.getUuid()).fromUser(user));
    }

    @Override
    public byte[] getInventoryData(LibreUser user) {
        if (!this.databaseAvailable()) {
            return new byte[0];
        }

        byte[] toReturn = this.uuidToSerializedInventoryMap.get(user.getUuid());
        this.uuidToSerializedInventoryMap.remove(user.getUuid()); //prevent duping
        return toReturn;
    }

    @Override
    public void saveInventoryData(LibreUser user, byte[] data) {
        if (!this.databaseAvailable()) {
            this.mongoRecoveryProtocol.getRecoveryStorageManager().saveInventoryData(user, data);
        }

        LibreUserDocumentWrapper wrapper = this.repository.findOrCreateByPath(user.getUuid()).fromUser(user);
        wrapper.setSerializedInventory(Base64.getEncoder().encodeToString(data));
        this.repository.save(wrapper);
    }

    @Override
    public boolean databaseAvailable() {
        return this.mongoRecoveryProtocol.databaseAvailable();
    }
}
