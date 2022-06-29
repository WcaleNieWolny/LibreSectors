package me.wcaleniewolny.libresectors.api.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;

@Header("================================")
@Header("       LibreSectors configuration      ")
@Header("================================")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class LibreConfiguration extends OkaeriConfig {

    @Comment("Prefix for the storage: allows to have multiple instances using same database")
    private String prefix = "ls";

    @Comment("MONGO  : mongodb://localhost:27017/db")
    private String databaseUri = "mongodb://localhost:27017/db";

    public String getDatabaseUri() {
        return this.databaseUri;
    }

    public String getPrefix() {
        return this.prefix;
    }
}
