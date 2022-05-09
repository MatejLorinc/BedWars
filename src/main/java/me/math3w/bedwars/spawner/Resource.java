package me.math3w.bedwars.spawner;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.RED;

public enum Resource {
    BRONZE(new ItemStack(Material.CLAY_BRICK) {{
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setDisplayName(RED + "Bronze");
        this.setItemMeta(itemMeta);
    }}),
    IRON(new ItemStack(Material.IRON_INGOT) {{
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setDisplayName(GRAY + "Iron");
        this.setItemMeta(itemMeta);
    }}),
    GOLD(new ItemStack(Material.GOLD_INGOT) {{
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GOLD + "Gold");
        this.setItemMeta(itemMeta);
    }});

    private final ItemStack item;

    Resource(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item.clone();
    }
}
