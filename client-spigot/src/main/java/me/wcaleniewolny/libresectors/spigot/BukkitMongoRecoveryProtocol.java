package me.wcaleniewolny.libresectors.spigot;

import com.google.common.collect.ImmutableList;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.persistence.flat.FlatPersistence;
import java.io.File;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;
import me.wcaleniewolny.libresectors.api.user.LibreUser;
import me.wcaleniewolny.libresectors.okaeri.recovery.FlatRecoveryStorageManager;
import me.wcaleniewolny.libresectors.okaeri.recovery.MongoRecoveryProtocol;
import me.wcaleniewolny.libresectors.spigot.inventory.InventoryPacker;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BukkitMongoRecoveryProtocol extends MongoRecoveryProtocol {

    private final JavaPlugin plugin;

    private File datafolder;

    public BukkitMongoRecoveryProtocol(JavaPlugin plugin, File datafolder) {
        super(new FlatRecoveryStorageManager(new File(plugin.getDataFolder(), "storage")));
        this.plugin = plugin;
        this.datafolder = datafolder;
    }

    @Override
    public void startRecoveryProtocol(@NotNull AtomicBoolean recoveryInProgress) {
        System.out.println("[LibreSectors] Started recovery protocol!");
        if (recoveryInProgress.get()) {
            System.out.println("[LibreSectors] Recovery protocol is in progress!");
            return; //We have already handled sector recovery
        }
        recoveryInProgress.set(true);
        this.saveAllPlayers(recoveryInProgress);

    }

    @Override
    public void stopRecoveryProtocol(AtomicBoolean recoveryInProgress) {
        System.out.println("[LibreSectors] Stopped recovery protocol!");
        if (recoveryInProgress.get()) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () -> this.stopRecoveryProtocol(recoveryInProgress), 50);
            System.out.println("[LibreSectors] Recovery protocol is in progress! Will do later!");
            return;
        }


    }

    @Override
    public File getDataFolder() {
        return this.datafolder;
    }

    private void saveAllPlayers(AtomicBoolean recoveryInProgress) {
        //NOTE: We do not know calling thread for this method. It might have some TTL. It is better if we ensure stable execution.
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            int delay = 1;
            for (Player player : ImmutableList.copyOf(Bukkit.getServer().getOnlinePlayers())) {
                this.savePlayer(player, delay);
                delay++;
            }

            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                Bukkit.getWorlds().forEach(World::save);
                recoveryInProgress.set(false);
            }, delay + 1);
        });


    }

    private void savePlayer(Player player, int delay) {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {

            LibreUser user = LibreUser.create(player.getUniqueId(), player.getName());
            player.kick();
            byte[] packedInventory = InventoryPacker.packInventory(player.getInventory());
            user.setSerializedInventory(Base64.getEncoder().encodeToString(packedInventory));

            getRecoveryStorageManager().saveUser(user);

            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {

                getRecoveryStorageManager().saveInventoryData(user, packedInventory);
            });
        }, delay);
    }
}
