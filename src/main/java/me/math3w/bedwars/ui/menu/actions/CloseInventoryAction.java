package me.math3w.bedwars.ui.menu.actions;

import me.math3w.bedwars.ui.menu.ClickAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class CloseInventoryAction implements ClickAction {
    private final Supplier<ItemStack[]> inventorySupplier;
    private final Player player;

    public CloseInventoryAction(Supplier<ItemStack[]> inventorySupplier, Player player) {
        this.inventorySupplier = inventorySupplier;
        this.player = player;
    }

    @Override
    public void execute(InventoryClickEvent event) {
        player.getInventory().setContents(inventorySupplier.get());
        player.closeInventory();
        player.updateInventory();
    }
}
