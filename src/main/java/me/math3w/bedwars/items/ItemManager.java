package me.math3w.bedwars.items;

import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.items.hubitems.items.MapVoteItem;
import me.math3w.bedwars.items.hubitems.items.TeamSelectionItem;
import me.math3w.bedwars.items.hubitems.items.TeleporterItem;
import me.math3w.bedwars.items.specialitems.items.LuckyBlock;
import me.math3w.bedwars.items.specialitems.items.Mine;
import me.math3w.bedwars.items.specialitems.items.PortableShop;
import me.math3w.bedwars.items.specialitems.items.RandomSpawner;

public class ItemManager {
    private final RandomSpawner randomSpawner;
    private final PortableShop portableShop;
    private final Mine mine;
    private final LuckyBlock luckyBlock;

    private final MapVoteItem mapVoteItem;
    private final TeamSelectionItem teamSelectionItem;
    private final TeleporterItem teleporterItem;

    public ItemManager(Game game) {
        randomSpawner = new RandomSpawner(game);
        portableShop = new PortableShop(game);
        mine = new Mine(game);
        luckyBlock = new LuckyBlock(game);

        mapVoteItem = new MapVoteItem(game);
        teamSelectionItem = new TeamSelectionItem(game);
        teleporterItem = new TeleporterItem(game);
    }

    public RandomSpawner getRandomSpawner() {
        return randomSpawner;
    }

    public PortableShop getPortableShop() {
        return portableShop;
    }

    public Mine getMine() {
        return mine;
    }

    public LuckyBlock getLuckyBlock() {
        return luckyBlock;
    }

    public MapVoteItem getMapVoteItem() {
        return mapVoteItem;
    }

    public TeamSelectionItem getTeamSelectionItem() {
        return teamSelectionItem;
    }

    public TeleporterItem getTeleporterItem() {
        return teleporterItem;
    }
}
