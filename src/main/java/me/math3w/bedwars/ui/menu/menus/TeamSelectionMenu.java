package me.math3w.bedwars.ui.menu.menus;

import me.math3w.bedwars.BedWarsPlugin;
import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.game.team.Team;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.ui.menu.ClickAction;
import me.math3w.bedwars.ui.menu.Menu;
import me.math3w.bedwars.ui.menu.MenuItem;
import me.math3w.bedwars.ui.menu.actions.CloseInventoryAction;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.bukkit.ChatColor.*;

public class TeamSelectionMenu extends Menu {
    private final Game game;

    public TeamSelectionMenu(Game game, BedwarsPlayer player) {
        super(player, 1);
        this.game = game;
    }

    @Override
    public String getMenuName() {
        return "" + RED + BOLD + "Select team";
    }

    @Override
    protected void setMenuItems() {
        Player bukkitPlayer = player.getBukkitPlayer();

        Function<Team, MenuItem> woolCreator = (team) -> {
            ItemStack wool = new ItemStack(Material.WOOL, 1, team.getColor().getDyeColor().getData());
            ItemMeta woolMeta = wool.getItemMeta();
            List<String> woolLore = new ArrayList<>();

            woolMeta.setDisplayName(team.getColor().getName());
            woolLore.add(team.getColor().getChatColor() + "Players in team: " + BOLD + team.getPlayers().size());
            woolLore.add(GRAY + "---------------");
            team.getPlayers().stream()
                    .map(bwPlayer -> bwPlayer.getOfflinePlayer().getName())
                    .forEach((teamPlayerName) -> woolLore.add(team.getColor().getChatColor() + teamPlayerName));

            if (team.equals(player.getTeam())) {
                woolMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                woolMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            woolMeta.setLore(woolLore);
            wool.setItemMeta(woolMeta);

            ClickAction joinTeam = (event) -> bukkitPlayer.sendMessage(BedWarsPlugin.PREFIX + " " + team.addPlayer(player));
            ClickAction closeInventory =
                    new CloseInventoryAction(() -> game.getState().getInventoryPreset(player), player.getBukkitPlayer());
            ClickAction playSound = event -> bukkitPlayer.playSound(bukkitPlayer.getLocation(), Sound.ORB_PICKUP, 1, 1);

            return new MenuItem(wool)
                    .addClickAction(joinTeam)
                    .addClickAction(closeInventory)
                    .addClickAction(playSound);
        };

        for (int i = 0; i < game.getTeams().size(); i++) {
            Team team = game.getTeams().get(i);
            this.setItem(i, woolCreator.apply(team));
        }
    }
}
