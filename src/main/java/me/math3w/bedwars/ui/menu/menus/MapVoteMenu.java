package me.math3w.bedwars.ui.menu.menus;

import me.math3w.bedwars.config.ConfigManager;
import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.game.Map;
import me.math3w.bedwars.game.state.VotingState;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.ui.menu.ClickAction;
import me.math3w.bedwars.ui.menu.Menu;
import me.math3w.bedwars.ui.menu.MenuItem;
import me.math3w.bedwars.ui.menu.actions.RefreshInventoryAction;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.bukkit.ChatColor.*;

public class MapVoteMenu extends Menu {
    private final Game game;

    public MapVoteMenu(Game game, BedwarsPlayer player) {
        super(player, ConfigManager.getInstance().getMapsConfig().getConfigurationSection("maps")
                .getKeys(false).size() / 9 + 1);
        this.game = game;
    }

    @Override
    public String getMenuName() {
        return "" + RED + BOLD + "Vote map";
    }

    @Override
    protected void setMenuItems() {
        if (!(game.getState() instanceof VotingState)) return;

        Function<Map, ClickAction> voteMap = (map) -> (event) -> {
            if (game.getState() instanceof VotingState) {
                ((VotingState) game.getState()).voteMap(player, map);
            }
        };
        ClickAction refresh = new RefreshInventoryAction(() -> game.getState().getInventoryPreset(player), this);

        Function<Map, MenuItem> mapItemCreator = (map) -> {
            if (!(game.getState() instanceof VotingState)) return null;

            ItemStack mapItem = new ItemStack(Material.PAPER);
            ItemMeta mapItemMeta = mapItem.getItemMeta();
            List<String> mapItemLore = new ArrayList<>();

            mapItemMeta.setDisplayName(DARK_GREEN + map.getName());
            mapItemLore.add("");
            mapItemLore.add(GRAY + "Votes: " + GREEN + map.getVotes());

            Map playerVotedMap = ((VotingState) game.getState()).getPlayerVotedMap(player).orElse(null);
            if (map.equals(playerVotedMap)) {
                mapItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                mapItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            mapItemMeta.setLore(mapItemLore);
            mapItem.setItemMeta(mapItemMeta);

            return new MenuItem(mapItem)
                    .addClickAction(voteMap.apply(map))
                    .addClickAction(refresh);
        };

        Set<Map> maps = ((VotingState) game.getState()).getMaps();

        int i = 0;
        for (Map map : maps) {
            this.setItem(i, mapItemCreator.apply(map));
            i++;
        }

        ItemStack resetVoteItem = new ItemStack(Material.BARRIER);
        ItemMeta resetVoteItemMeta = resetVoteItem.getItemMeta();
        resetVoteItemMeta.setDisplayName(RED + "Reset vote");
        resetVoteItem.setItemMeta(resetVoteItemMeta);

        setItem((maps.size() / 9 + 1) * 9 - 1, new MenuItem(resetVoteItem)
                .addClickAction(voteMap.apply(null))
                .addClickAction(refresh));
    }
}
