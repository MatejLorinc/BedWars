package me.math3w.bedwars.ui.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {
    private final ItemStack itemStack;
    private final List<ClickAction> clickActions = new ArrayList<>();

    public MenuItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public MenuItem addClickAction(ClickAction action) {
        this.clickActions.add(action);
        return this;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void doClickActions(InventoryClickEvent event) {
        clickActions.forEach(action -> action.execute(event));
    }
}
