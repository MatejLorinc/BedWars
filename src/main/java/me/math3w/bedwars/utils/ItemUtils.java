package me.math3w.bedwars.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.UUID;

public class ItemUtils {
    private ItemUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static String getItemName(ItemStack item) {
        if (item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().getDisplayName();
        }

        return item.getItemMeta().hasDisplayName()
                ? item.getItemMeta().getDisplayName()
                : CraftItemStack.asNMSCopy(item).getName();
    }

    public static ItemStack createPotionItem(PotionType type, int level, boolean splash, boolean extend,
                                             boolean description) {
        Potion potion = new Potion(type, level, splash);
        if (!type.isInstant())
            potion.setHasExtendedDuration(extend);

        ItemStack potionItem = potion.toItemStack(1);
        if (!description) {
            ItemMeta itemMeta = potionItem.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        }
        return potionItem;
    }

    public static ItemStack createPotionItem(PotionType type, int level, boolean splash, boolean extend) {
        return createPotionItem(type, level, splash, extend, true);
    }

    public static ItemStack createPotionItem(PotionType type, int level) {
        return createPotionItem(type, level, false, false);
    }

    public static ItemStack createSkull(String value) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (value.isEmpty())
            return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(
                ("{textures:{SKIN:{url:\"https://textures.minecraft.net/texture/" + value + "\"}}}").getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));

        Field profileField;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
        head.setItemMeta(headMeta);

        return head;
    }

    public static Item dropItem(Location location, ItemStack itemStack) {
        Item item = location.getWorld().dropItem(location.clone().add(0, 0.25, 0), itemStack);
        item.setVelocity(new Vector(0, 0, 0));

        return item;
    }
}
