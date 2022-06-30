package me.wcaleniewolny.libresectors.spigot.inventory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.wcaleniewolny.libresectors.api.user.LibreUser;
import me.wcaleniewolny.libresectors.spigot.StorageFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryManager implements Listener {

    private final StorageFactory storageFactory;
    private final JavaPlugin plugin;

    public InventoryManager(StorageFactory storageFactory, JavaPlugin plugin) {
        this.storageFactory = storageFactory;
        this.plugin = plugin;
    }

    @EventHandler()
    void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String name = player.getName();

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {

            Optional<LibreUser> user = this.storageFactory.getManager().loadUser(uuid);
            if (!user.isPresent()) {
                Bukkit.getLogger().info(String.format("[LibreSectors] %s is not present! Creating in database", player.name()));
                this.storageFactory.getManager().saveUser(LibreUser.create(uuid, name));
            }
            else {
                List<ItemStack> items = InventoryPacker.unpackInventory(this.storageFactory.getManager().getInventoryData(user.get()));

                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    PlayerInventory inventory = player.getInventory();
                    for (int i = 0; i < 41; i++) {
                        ItemStack itemToSet = items.get(i);

                        if (itemToSet != null) {
                            inventory.setItem(i, items.get(i));
                        }
                        else {
                            inventory.clear(i);
                        }

                    }
                }, 1);
            }
        });
    }

    @EventHandler()
    void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        byte[] packedInventory = InventoryPacker.packInventory(player.getInventory());
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            this.storageFactory.getManager().saveInventoryData(LibreUser.create(player.getUniqueId(), player.getName()), packedInventory);
        });
    }
}
