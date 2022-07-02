package me.wcaleniewolny.libresectors.okaeri;

import eu.okaeri.persistence.repository.DocumentRepository;
import eu.okaeri.persistence.repository.annotation.DocumentCollection;
import eu.okaeri.persistence.repository.annotation.DocumentIndex;
import eu.okaeri.persistence.repository.annotation.DocumentPath;
import java.util.Optional;
import java.util.UUID;

@DocumentCollection(path = "user", keyLength = 36, indexes = {
        @DocumentIndex(path = "uuid", maxLength = 36),
        @DocumentIndex(path = "name", maxLength = 24),
})
public interface LibreUserRepository extends DocumentRepository<UUID, LibreUserDocumentWrapper> {

    @DocumentPath("name")
    Optional<LibreUserDocumentWrapper> findUserByName(String name);
}
