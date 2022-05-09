package me.math3w.bedwars.spawner;

import org.bukkit.Material;

public enum SpawnerType {
    BRONZE(1, Resource.BRONZE, Material.HARD_CLAY, false),
    IRON(10, Resource.IRON, Material.IRON_BLOCK, true),
    GOLD(20, Resource.GOLD, Material.GOLD_BLOCK, true);

    private final int interval;
    private final Resource resource;
    private final Material block;
    private final boolean hologram;

    SpawnerType(int interval, Resource resource, Material block, boolean hologram) {
        this.interval = interval;
        this.resource = resource;
        this.block = block;
        this.hologram = hologram;
    }

    public int getInterval() {
        return interval;
    }

    public Resource getResource() {
        return resource;
    }

    public Material getBlock() {
        return block;
    }

    public boolean isHologram() {
        return hologram;
    }
}
