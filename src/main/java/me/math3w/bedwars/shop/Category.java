package me.math3w.bedwars.shop;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.spawner.Resource;
import me.math3w.bedwars.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.function.BiFunction;

import static org.bukkit.ChatColor.BOLD;
import static org.bukkit.ChatColor.RED;

public enum Category {
    BLOCKS(Material.QUARTZ_BLOCK, "Blocks", (game, customer) -> new ShopItem[]{
            new ShopItem(new ItemStack(Material.WOOL, 2, customer.getTeam().getColor().getDyeColor().getData()),
                    Resource.BRONZE, 1, customer),
            new ShopItem(new ItemStack(Material.STAINED_CLAY, 1, customer.getTeam().getColor().getDyeColor().getData()),
                    Resource.BRONZE, 2, customer),
            new ShopItem(new ItemStack(Material.ENDER_STONE), Resource.BRONZE, 4, customer),
            new ShopItem(new ItemStack(Material.IRON_BLOCK), Resource.IRON, 1, customer),
            new ShopItem(new ItemStack(Material.OBSIDIAN), Resource.GOLD, 2, customer),
    }),
    ARMOR(Material.IRON_CHESTPLATE, "Armor", (game, customer) -> new ShopItem[]{
            new ShopItem(new ItemStack(Material.CHAINMAIL_HELMET) {{
                this.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            }}, Resource.BRONZE, 3, customer),
            new ShopItem(new ItemStack(Material.CHAINMAIL_LEGGINGS) {{
                this.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            }}, Resource.BRONZE, 3, customer),
            new ShopItem(new ItemStack(Material.CHAINMAIL_BOOTS) {{
                this.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            }}, Resource.BRONZE, 3, customer),
            new ShopItem(new ItemStack(Material.IRON_CHESTPLATE) {{
                this.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            }}, Resource.IRON, 1, customer),
            new ShopItem(new ItemStack(Material.IRON_CHESTPLATE) {{
                this.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
            }}, Resource.IRON, 3, customer),
            new ShopItem(new ItemStack(Material.IRON_CHESTPLATE) {{
                this.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
            }}, Resource.IRON, 7, customer),
            new ShopItem(new ItemStack(Material.IRON_CHESTPLATE) {{
                this.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            }}, Resource.IRON, 11, customer)
    }),
    TOOLS(Material.STONE_PICKAXE, "Tools", (game, customer) -> new ShopItem[]{
            new ShopItem(new ItemStack(Material.SHEARS), Resource.IRON, 1, customer),
            new ShopItem(new ItemStack(Material.WOOD_PICKAXE) {{
                this.addEnchantment(Enchantment.DIG_SPEED, 1);
            }}, Resource.BRONZE, 7, customer),
            new ShopItem(new ItemStack(Material.STONE_PICKAXE) {{
                this.addEnchantment(Enchantment.DIG_SPEED, 2);
            }}, Resource.IRON, 3, customer),
            new ShopItem(new ItemStack(Material.IRON_PICKAXE) {{
                this.addEnchantment(Enchantment.DIG_SPEED, 3);
            }}, Resource.GOLD, 1, customer),
            new ShopItem(new ItemStack(Material.DIAMOND_PICKAXE) {{
                this.addEnchantment(Enchantment.DIG_SPEED, 3);
            }}, Resource.GOLD, 5, customer)
    }),
    MELEE(Material.IRON_SWORD, "Melee", (game, customer) -> new ShopItem[]{
            new ShopItem(new ItemStack(Material.STICK) {{
                this.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
            }}, Resource.BRONZE, 10, customer),
            new ShopItem(new ItemStack(Material.WOOD_SWORD) {{
                this.addEnchantment(Enchantment.DAMAGE_ALL, 1);
            }}, Resource.BRONZE, 15, customer),
            new ShopItem(new ItemStack(Material.STONE_SWORD) {{
                this.addEnchantment(Enchantment.DAMAGE_ALL, 2);
            }}, Resource.IRON, 2, customer),
            new ShopItem(new ItemStack(Material.IRON_SWORD) {{
                this.addEnchantment(Enchantment.DAMAGE_ALL, 3);
            }}, Resource.GOLD, 4, customer),
            new ShopItem(new ItemStack(Material.DIAMOND_SWORD) {{
                this.addEnchantment(Enchantment.DAMAGE_ALL, 4);
            }}, Resource.GOLD, 8, customer),
    }),
    ARCHERY(Material.BOW, "Archery", (game, customer) -> new ShopItem[]{
            new ShopItem(new ItemStack(Material.BOW) {{
                this.addEnchantment(Enchantment.ARROW_INFINITE, 1);
            }}, Resource.GOLD, 3, customer),
            new ShopItem(new ItemStack(Material.BOW) {{
                this.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                this.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
            }}, Resource.GOLD, 5, customer),
            new ShopItem(new ItemStack(Material.BOW) {{
                this.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                this.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
            }}, Resource.GOLD, 8, customer),
            new ShopItem(new ItemStack(Material.BOW) {{
                this.addEnchantment(Enchantment.ARROW_INFINITE, 1);
                this.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
                this.addEnchantment(Enchantment.ARROW_FIRE, 1);
            }}, Resource.GOLD, 12, customer),
            new ShopItem(new ItemStack(Material.ARROW), Resource.IRON, 3, customer)
    }),
    FOOD(Material.COOKED_BEEF, "Food", (game, customer) -> new ShopItem[]{
            new ShopItem(new ItemStack(Material.APPLE), Resource.BRONZE, 1, customer),
            new ShopItem(new ItemStack(Material.COOKED_CHICKEN), Resource.BRONZE, 2, customer),
            new ShopItem(new ItemStack(Material.COOKED_BEEF), Resource.BRONZE, 3, customer),
            new ShopItem(new ItemStack(Material.GOLDEN_APPLE), Resource.IRON, 5, customer)
    }),
    POTIONS(ItemUtils.createPotionItem(PotionType.STRENGTH, 1, false, false, false),
            "Potions", (game, customer) -> new ShopItem[]{
            new ShopItem(ItemUtils.createPotionItem(PotionType.INSTANT_HEAL, 2), Resource.IRON, 1, customer),
            new ShopItem(ItemUtils.createPotionItem(PotionType.SPEED, 1), Resource.IRON, 5, customer),
            new ShopItem(ItemUtils.createPotionItem(PotionType.REGEN, 1), Resource.IRON, 15, customer),
            new ShopItem(ItemUtils.createPotionItem(PotionType.STRENGTH, 1), Resource.GOLD, 6, customer),
    }),
    UTILITY(Material.TNT, "Utility", (game, customer) -> new ShopItem[]{
            new ShopItem(game.getItemManager().getPortableShop().getItem(), Resource.BRONZE, 8, customer),
            new ShopItem(new ItemStack(Material.TNT), Resource.GOLD, 2, customer),
            new ShopItem(game.getItemManager().getRandomSpawner().getItem(), Resource.GOLD, 4, customer),
            new ShopItem(new ItemStack(Material.ENDER_PEARL), Resource.GOLD, 12, customer),
            new ShopItem(new ItemStack(Material.WEB), Resource.IRON, 1, customer),
            new ShopItem(game.getItemManager().getMine().getItem(), Resource.IRON, 2, customer),
            new ShopItem(game.getItemManager().getLuckyBlock().getItem(), Resource.IRON, 8, customer)
    });

    private final ItemStack item;
    private final String name;
    private final BiFunction<Game, BedwarsPlayer, ShopItem[]> items;

    Category(ItemStack item, String name, BiFunction<Game, BedwarsPlayer, ShopItem[]> items) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("" + RED + BOLD + name);
        item.setItemMeta(itemMeta);
        this.item = item;
        this.name = name;
        this.items = items;
    }

    Category(Material material, String name, BiFunction<Game, BedwarsPlayer, ShopItem[]> items) {
        this(new ItemStack(material), name, items);
    }

    public ItemStack getItem() {
        return item;
    }

    public String getName() {
        return name;
    }

    public ShopItem[] getItems(Game game, BedwarsPlayer customer) {
        ShopItem[] items = this.items.apply(game, customer);
        Arrays.stream(items).forEach(item -> item.addClickAction(event -> new ShopMenu(game, customer, this).open()));
        return items;
    }
}
