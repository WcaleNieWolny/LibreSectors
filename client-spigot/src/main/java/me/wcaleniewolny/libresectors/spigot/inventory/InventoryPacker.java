package me.wcaleniewolny.libresectors.spigot.inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;

public class InventoryPacker {

    public static byte[] packInventory(PlayerInventory inventory) {
        try (MessageBufferPacker packer = MessagePack.newDefaultBufferPacker()) {
            for (int i = 0; i < 41; i++) {
                ItemStack itemStack = inventory.getItem(i);

                if (itemStack == null) {
                    packer.packArrayHeader(0);
                    continue;
                }

                byte[] data = itemStack.serializeAsBytes();
                packer.packArrayHeader(data.length);
                packer.writePayload(data);
            }
            return packer.toByteArray();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ItemStack> unpackInventory(byte[] packed) {
        ArrayList<ItemStack> items = new ArrayList<>(41);
        if (packed.length == 0) {
            for (int i = 0; i < 41; i++) {
                items.add(null);
            }

            return items;
        }

        try (MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(packed)) {
            for (int i = 0; i < 41; i++) {
                int size = unpacker.unpackArrayHeader();

                if (size == 0) {
                    items.add(null);
                    continue;
                }

                ItemStack unpackedItemStack = ItemStack.deserializeBytes(unpacker.readPayload(size));
                items.add(unpackedItemStack);
            }
            return items;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
