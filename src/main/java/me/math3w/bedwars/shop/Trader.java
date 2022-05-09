package me.math3w.bedwars.shop;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.ui.menu.Menu;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftVillager;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.Villager;

import java.lang.reflect.Field;

import static org.bukkit.ChatColor.RED;

public class Trader extends EntityVillager {
    private Game game;

    public Trader(World world) {
        super(world);
    }

    public Trader(Game game, Location location) {
        super(((CraftWorld) location.getWorld()).getHandle());

        this.game = game;

        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);

            bField.set(goalSelector, new UnsafeList<>());
            bField.set(targetSelector, new UnsafeList<>());
            cField.set(goalSelector, new UnsafeList<>());
            cField.set(targetSelector, new UnsafeList<>());
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
        }

        CraftVillager craftVillager = ((CraftVillager) this.getBukkitEntity());
        craftVillager.setAdult();
        craftVillager.setRemoveWhenFarAway(false);
        craftVillager.setCustomNameVisible(true);
        craftVillager.setCanPickupItems(false);
        craftVillager.setProfession(Villager.Profession.BLACKSMITH);
        craftVillager.setCustomName(RED + "Shop");

        this.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.f(location.getYaw());
        this.g(location.getYaw());
    }

    @Override
    public void move(double d0, double d1, double d2) {
        if (game != null) return;

        super.move(d0, d1, d2);
    }

    @Override
    public void collide(Entity entity) {
        if (game != null) return;

        super.collide(entity);
    }

    @Override
    public void g(double d0, double d1, double d2) {
        if (game != null) return;

        super.g(d0, d1, d2);
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (game != null) return false;

        return super.damageEntity(damagesource, f);
    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        if (game == null) {
            return super.a(entityhuman);
        }

        EntityPlayer nmsPlayer = (EntityPlayer) entityhuman;
        CraftPlayer player = nmsPlayer.getBukkitEntity();

        Menu shop = new ShopMenu(game, game.getPlayerManager().getOrCreatePlayer(player), Category.BLOCKS);
        shop.open();

        return true;
    }
}
