package me.math3w.bedwars.ui.text;

import me.math3w.bedwars.player.BedwarsPlayer;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

public class Actionbar {
    private final BedwarsPlayer player;
    private UpdatableText text;

    public Actionbar(BedwarsPlayer player) {
        this.player = player;
    }

    public Actionbar(BedwarsPlayer player, UpdatableText text) {
        this.player = player;
        this.text = text;
    }

    public void update() {
        if (!player.isOnline()) return;

        String text = this.text.replacePlaceholders(player);
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(text), (byte) 2);
        ((CraftPlayer) player.getBukkitPlayer()).getHandle().playerConnection.sendPacket(packet);
    }

    public BedwarsPlayer getPlayer() {
        return player;
    }

    public UpdatableText getText() {
        return text;
    }

    public void setText(UpdatableText text) {
        this.text = text;
        update();
    }
}
