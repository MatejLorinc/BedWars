package me.math3w.bedwars.ui.menu.actions;

import me.math3w.bedwars.ui.menu.ClickAction;
import me.math3w.bedwars.ui.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class RefreshInventoryAction implements ClickAction {
    private final Supplier<ItemStack[]> inventorySupplier;
    private final Menu menu;

    public RefreshInventoryAction(Supplier<ItemStack[]> inventorySupplier, Menu menu) {
        this.inventorySupplier = inventorySupplier;
        this.menu = menu;
    }

    @Override
    public void execute(InventoryClickEvent event) {
        Player bukkitPlayer = menu.getViewer().getBukkitPlayer();
        bukkitPlayer.getInventory().setContents(inventorySupplier.get());
        bukkitPlayer.updateInventory();
        menu.open();
    }
}
