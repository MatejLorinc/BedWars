package me.math3w.bedwars.utils;

import com.google.common.io.ByteStreams;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

public class Utils {
    private Utils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static void resetPlayer(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setLevel(0);
        player.setExp(0);
        player.setFoodLevel(20);
        player.setHealth(20);

        player.setFireTicks(0);
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
    }

    public static void setTitle(Player player, String title, String subtitle,
                                int fadeIn, int duration, int fadeOut) {
        PacketPlayOutTitle durationPacket = new PacketPlayOutTitle(fadeIn, duration, fadeOut);
        PacketPlayOutTitle titlePacket =
                new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
                        IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + title + "\"}"));
        PacketPlayOutTitle subtitlePacket =
                new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                        IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + subtitle + "\"}"));

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(durationPacket);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlePacket);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitlePacket);
    }

    public static void setTitle(Player player, String text,
                                int fadeIn, int duration, int fadeOut) {
        setTitle(player, text, "", fadeIn, duration, fadeOut);
    }

    public static void setSubtitle(Player player, String text,
                                   int fadeIn, int duration, int fadeOut) {
        setTitle(player, "", text, fadeIn, duration, fadeOut);
    }

    public static ArmorStand spawnStand(Location location) {
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        stand.setGravity(false);
        stand.setVisible(false);
        stand.setBasePlate(false);
        stand.setCustomNameVisible(true);

        return stand;
    }

    public static InputStream convertUniqueId(UUID uuid) {
        byte[] bytes = new byte[16];
        ByteBuffer.wrap(bytes)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits());
        return new ByteArrayInputStream(bytes);
    }

    public static UUID convertBinaryStream(InputStream stream) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        try {
            buffer.put(ByteStreams.toByteArray(stream));
            buffer.flip();
            return new UUID(buffer.getLong(), buffer.getLong());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
