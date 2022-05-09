package me.math3w.bedwars.spawner;

import me.math3w.bedwars.utils.ItemUtils;
import me.math3w.bedwars.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.ChatColor.*;

public class Spawner {
    private final Plugin plugin;

    private final Location location;
    private final SpawnerType spawnerType;

    private int remainingTime;

    public Spawner(Plugin plugin, Location location, SpawnerType type) {
        this.plugin = plugin;

        this.location = location;
        this.spawnerType = type;

        remainingTime = type.getInterval();

        if (spawnerType.isHologram()) {
            String spawnerName = ItemUtils.getItemName(type.getResource().getItem());
            spawnStand(2.65).setCustomName(getLastColors(spawnerName) +
                    BOLD + stripColor(spawnerName));

            spawnFloatingItem(1.75f, 0.2);
        }
    }

    public void startSpawner() {
        ArmorStand timeText = spawnerType.isHologram()
                ? spawnStand(2.25)
                : null;

        new BukkitRunnable() {
            @Override
            public void run() {
                remainingTime--;

                if (remainingTime <= 0) {
                    ItemUtils.dropItem(location, spawnerType.getResource().getItem());
                    remainingTime = spawnerType.getInterval();
                }

                if (timeText != null) {
                    timeText.setCustomName(GRAY + "Spawns in " + RED + remainingTime + GRAY + " seconds");
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private ArmorStand spawnFloatingItem(float rotationsPerCycle, double maxMargin) {
        ArmorStand stand = spawnStand(2);

        stand.setHelmet(new ItemStack(spawnerType.getBlock()));
        stand.setCustomNameVisible(false);

        double marginDivisor = (1 / maxMargin / 2);
        Location originalLocation = stand.getLocation();
        new BukkitRunnable() {
            double modifier = 0;

            @Override
            public void run() {
                Location newLocation = originalLocation.clone();

                double margin = ((modifier % 2 < 1
                        ? modifier % 1
                        : 1 - (modifier % 1)) - 0.5) / marginDivisor;

                newLocation.add(0, margin, 0);
                newLocation.setYaw((float) (Math.sin(modifier) * rotationsPerCycle * 180));

                stand.teleport(newLocation);

                modifier += 0.04;
            }
        }.runTaskTimer(plugin, 1, 1);

        return stand;
    }

    private ArmorStand spawnStand(double yModifier) {
        return Utils.spawnStand(location.clone().add(0, yModifier, 0));
    }

    public Location getLocation() {
        return location;
    }

    public SpawnerType getType() {
        return spawnerType;
    }
}
