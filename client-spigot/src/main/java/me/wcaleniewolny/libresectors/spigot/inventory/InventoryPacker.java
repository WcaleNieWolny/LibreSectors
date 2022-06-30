package me.wcaleniewolny.libresectors.spigot.inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
        try (MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(packed)) {
            ArrayList<ItemStack> items = new ArrayList<>(41);
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

    private static void packVarInt(int var, MessagePacker packer) throws IOException {
        while ((var & ~0x7F) != 0) {
            packer.packByte((byte) ((var & 0x7F) | 0x80));
            var >>>= 7;
        }

        packer.packByte((byte) var);
    }

    private static void packByteArray(byte[] var, MessagePacker packer) throws IOException {
        packVarInt(var.length, packer);
        for (byte b : var) {
            packer.packByte(b);
        }
        //NOTE: Size is everything - I do not know internal implementation, so I used varint
    }

    private static int unpackVarInt(MessageUnpacker unpacker) throws IOException {
        int value = 0;
        int size = 0;
        int b;
        while (((b = unpacker.unpackByte()) & 0x80) == 0x80) {
            value |= (b & 0x7F) << (size++ * 7);
            if (size > 5) {
                throw new IOException("VarInt too long (length must be <= 5)");
            }
        }

        return value | ((b & 0x7F) << (size * 7));
    }

    private static byte[] unpackByteArray(MessageUnpacker unpacker) throws IOException {
        int size = unpackVarInt(unpacker);
        byte[] bytes = new byte[size];

        for (int i = 0; i < size; i++) {
            bytes[i] = unpacker.unpackByte();
        }
        //NOTE: Size is everything - I do not know internal implementation, so I used varint
        return bytes;
    }
}
