package me.wcaleniewolny.libresectors.spigot;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import me.wcaleniewolny.libresectors.api.user.LibreUser;
import me.wcaleniewolny.libresectors.okaeri.recovery.MongoRecoveryProtocol;
import me.wcaleniewolny.libresectors.spigot.inventory.InventoryPacker;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMongoRecoveryProtocol extends MongoRecoveryProtocol {

    private final JavaPlugin plugin;

    public BukkitMongoRecoveryProtocol(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void startRecoveryProtocol(AtomicBoolean recoveryInProgress) {
        if (recoveryInProgress.get()) {
            return; //We have already handled sector recovery
        }


    }

    @Override
    public void stopRecoveryProtocol(AtomicBoolean recoveryInProgress) {
        if (recoveryInProgress.get()) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () -> this.stopRecoveryProtocol(recoveryInProgress), 50);
            return;
        }


    }

    @Override
    public File getDataFolder() {
        return this.plugin.getDataFolder();
    }

    private void savePlayer(Player player, int delay) {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {

            LibreUser user = LibreUser.create(player.getUniqueId(), player.getName());
            player.kick();
            byte[] packedInventory = InventoryPacker.packInventory(player.getInventory());

            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {

                getRecoveryStorageManager().saveInventoryData(user, packedInventory);
            });
        }, delay);
    }
}
