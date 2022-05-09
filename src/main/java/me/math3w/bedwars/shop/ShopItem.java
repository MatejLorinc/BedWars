package me.math3w.bedwars.shop;

import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.spawner.Resource;
import me.math3w.bedwars.ui.menu.ClickAction;
import me.math3w.bedwars.ui.menu.MenuItem;
import me.math3w.bedwars.utils.ItemUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.bukkit.ChatColor.*;

public class ShopItem extends MenuItem {
    private final ItemStack price;
    private final ItemStack item;
    private final BedwarsPlayer customer;

    public ShopItem(ItemStack item, ItemStack price, BedwarsPlayer customer) {
        super(getMenuItem(item, price, customer));
        this.price = price;
        this.item = item;
        this.customer = customer;

        ClickAction buyItem = (event) -> {
            Player bukkitCustomer = customer.getBukkitPlayer();

            if (!event.isLeftClick()) return;

            if (!canAfford(price, bukkitCustomer)) {
                bukkitCustomer.sendMessage(RED + "Insufficient amount of " + ItemUtils.getItemName(price) +
                        RED + " to purchase " + WHITE + ItemUtils.getItemName(item));
                return;
            }

            if (bukkitCustomer.getInventory().firstEmpty() == -1) {
                bukkitCustomer.sendMessage(RED + "Your inventory is full!");
                return;
            }

            if (event.isShiftClick()) {
                int mulLimit = item.getMaxStackSize() / item.getAmount();
                int resourceAmount = Arrays.stream(bukkitCustomer.getInventory().getContents())
                        .filter(price::isSimilar)
                        .mapToInt(ItemStack::getAmount)
                        .sum();

                int mul = Math.min(resourceAmount / price.getAmount(), mulLimit);

                ItemStack newItem = item.clone();
                newItem.setAmount(item.getAmount() * mul);
                ItemStack newPrice = price.clone();
                newPrice.setAmount(price.getAmount() * mul);

                buy(newItem, newPrice, bukkitCustomer);
            } else {
                buy(item, price, bukkitCustomer);
            }

            bukkitCustomer.sendMessage(GRAY + "Purchased " + WHITE + ItemUtils.getItemName(item));
        };

        this.addClickAction(buyItem);
    }

    public ShopItem(ItemStack item, Resource priceType, int priceAmount, BedwarsPlayer customer) {
        this(item, ((Supplier<ItemStack>) () -> {
            ItemStack price = priceType.getItem();
            price.setAmount(priceAmount);
            return price;
        }).get(), customer);
    }

    private static ItemStack getMenuItem(ItemStack item, ItemStack price, BedwarsPlayer customer) {
        String priceName = ItemUtils.getItemName(price);

        ItemStack revampedItem = item.clone();
        ItemMeta itemMeta = revampedItem.getItemMeta();
        List<String> itemLore = itemMeta.hasLore() ? itemMeta.getLore() : new ArrayList<>();

        itemMeta.setDisplayName((canAfford(price, customer.getBukkitPlayer()) ? GREEN : RED) +
                stripColor(ItemUtils.getItemName(item)));
        itemLore.add(" ");
        itemLore.add(GRAY + "Cost: " + getLastColors(priceName) + price.getAmount() + " " + priceName);

        itemMeta.setLore(itemLore);
        revampedItem.setItemMeta(itemMeta);
        return revampedItem;
    }

    private static boolean canAfford(ItemStack price, Player bukkitCustomer) {
        return bukkitCustomer.getInventory().containsAtLeast(price, price.getAmount());
    }

    private static void buy(ItemStack item, ItemStack price, Player bukkitCustomer) {
        bukkitCustomer.getInventory().removeItem(price);
        bukkitCustomer.getInventory().addItem(item);
        bukkitCustomer.playSound(bukkitCustomer.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
    }

    public ItemStack getPrice() {
        return price;
    }

    public ItemStack getItem() {
        return item;
    }
}
