package me.math3w.bedwars.game.state;

import me.math3w.bedwars.BedWarsPlugin;
import me.math3w.bedwars.game.Game;
import me.math3w.bedwars.game.state.tasks.countdowns.statecountdowns.GameCountdownTask;
import me.math3w.bedwars.game.team.Team;
import me.math3w.bedwars.game.team.TeamColor;
import me.math3w.bedwars.listeners.SpectatorListeners;
import me.math3w.bedwars.player.BedwarsPlayer;
import me.math3w.bedwars.shop.ShopMenu;
import me.math3w.bedwars.shop.ShopUtils;
import me.math3w.bedwars.spawner.Resource;
import me.math3w.bedwars.spawner.Spawner;
import me.math3w.bedwars.spawner.SpawnerType;
import me.math3w.bedwars.statistics.Statistic;
import me.math3w.bedwars.ui.text.Sidebar;
import me.math3w.bedwars.ui.text.UI;
import me.math3w.bedwars.utils.StringUtils;
import me.math3w.bedwars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.function.Supplier;

import static org.bukkit.ChatColor.*;

public class PlayingState extends State {
    private final Map<UUID, Set<EnderPearl>> pearls = new HashMap<>();
    private final SpectatorListeners spectatorListeners;

    public PlayingState(Game game) {
        super(game);
        this.spectatorListeners = new SpectatorListeners(game);
    }

    @Override
    public void onEnable() {
        this.setCountdownTask(new GameCountdownTask(game, 1800));
        super.onEnable();
        Bukkit.getPluginManager().registerEvents(spectatorListeners, game.getPlugin());

        for (BedwarsPlayer player : game.getPlayerManager().getPlayers()) {
            if (player.getTeam() == null) {
                Optional<Team> smallestTeam = game.getTeams().stream()
                        .min(Comparator.comparingInt(team -> team.getPlayers().size()));
                smallestTeam.ifPresent(team -> team.addPlayer(player));
            }

            player.getBukkitPlayer().teleport(game.getMap().getSpawn(player.getTeam().getColor()));
        }

        game.getPlayerManager().broadcast(WHITE + "■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
        game.getPlayerManager().broadcast(" " + RED + BOLD + "BedWars");
        game.getPlayerManager().broadcast(GREEN + "  Map: " + game.getMap().getName());
        game.getPlayerManager().broadcast("");
        game.getPlayerManager().broadcast(GRAY + " Protect your bed and destroy the enemy team beds.");
        game.getPlayerManager().broadcast(WHITE + "■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");

        for (SpawnerType spawnerType : SpawnerType.values()) {
            for (Location location : game.getMap().getSpawners(spawnerType)) {
                Spawner spawner = new Spawner(game.getPlugin(), location, spawnerType);
                spawner.startSpawner();
            }
        }

        for (Team team : game.getTeams()) {
            ShopUtils.spawnTrader(game, game.getMap().getItemShop(team.getColor()));
        }

        for (Team deadTeam : game.getDeadTeams()) {
            deadTeam.breakBed();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        HandlerList.unregisterAll(spectatorListeners);

        if (game.getAliveTeams().size() == 1) {
            game.getAliveTeams().stream().findAny()
                    .ifPresent(team -> team.getPlayers().forEach(player -> game.getPlayerManager().addStatistic(player, Statistic.WINS)));
        } else {
            for (Team team : game.getAliveTeams()) {
                team.getPlayers().forEach(player -> game.getPlayerManager().addStatistic(player, Statistic.DRAWS));
            }
        }

        for (Team team : game.getDeadTeams()) {
            team.getPlayers().forEach(player -> game.getPlayerManager().addStatistic(player, Statistic.LOSES));
        }
    }

    @Override
    public UI getUi(BedwarsPlayer player) {
        UI ui = new UI(player);
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Sidebar.Builder sidebarBuilder = new Sidebar.Builder().player(player)
                .title(bwPlayer -> BedWarsPlugin.NAME + WHITE + " | " + StringUtils.secondsToTime(this.getRemainingSeconds()));

        sidebarBuilder.addLine(bwPlayer -> "" + GOLD + BOLD + "Teams");

        for (Team team : game.getTeams()) {
            sidebarBuilder.addLine(bwPlayer ->
                    " " + (team.hasBed()
                            ? GREEN + "✔ " + team.getColor().getName()
                            : RED + "✘ " + GRAY + team.getColor().getRawName()) +
                            (team.getSize() > 0 ? GRAY + " (" + team.getSize() + ")" : "") +
                            (team.getOnlinePlayers().contains(player) ? DARK_GRAY + " YOU" : ""));
        }

        sidebarBuilder.addLine()
                .addLine(bwPlayer -> GRAY + "Kills: " + WHITE + bwPlayer.getStatistic(Statistic.KILLS))
                .addLine(bwPlayer -> GRAY + "Deaths: " + WHITE + bwPlayer.getStatistic(Statistic.DEATHS))
                .addLine(bwPlayer -> GRAY + "Broken beds: " + WHITE + bwPlayer.getStatistic(Statistic.BED_BREAKS));

        Sidebar sidebar = sidebarBuilder.build();

        ui.setScoreboard(scoreboard);
        ui.setSidebar(sidebar);
        ui.setTablist(new WaitingState(game).getUi(player).getTablist());
        return ui;
    }

    @Override
    public ItemStack[] getInventoryPreset(BedwarsPlayer player) {
        return new ItemStack[36];
    }

    @Override
    public State getNextState() {
        return new PostGameState(game);
    }

    public void kill(BedwarsPlayer player, Event event) {
        if (!(event instanceof PlayerDeathEvent) && !(event instanceof PlayerQuitEvent))
            throw new IllegalArgumentException("BedwarsPlayer could be killed only in PlayerDeathEvent or PlayerQuitEvent");

        if (player.isSpectating()) return;

        boolean isFinal = !player.getTeam().hasBed();

        game.getPlayerManager().broadcast(this.getDeathMessage(player, event));

        if (player.isLastHitRelevant()) {
            BedwarsPlayer killer = player.getLastDamager();

            game.getPlayerManager().addStatistic(killer, Statistic.KILLS);
            killer.getBukkitPlayer().playSound(player.getBukkitPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
            if (isFinal) {
                game.getPlayerManager().addStatistic(killer, Statistic.FINAL_KILLS);
            }
            if (!game.wasFirstBlood()) {
                game.makeFirstBlood(killer);
            }
        }

        game.getPlayerManager().addStatistic(player, Statistic.DEATHS);

        if (isFinal) {
            eliminate(player);
        }

        player.setLastDamager(null);
    }

    private String getDeathMessage(BedwarsPlayer player, Event event) {
        boolean isFinal = !player.getTeam().hasBed();
        String finalKillText = "" + AQUA + BOLD + " FINAL KILL";

        StringBuilder message = new StringBuilder(player.getName() + GRAY);

        if (player.isLastHitRelevant()) {
            message.append(" was killed by ").append(player.getLastDamager().getName());
        } else if (event instanceof PlayerQuitEvent) {
            message.append(" has left");
        } else if (player.getBukkitPlayer().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
            message.append(" fell into the ").append(RED).append("Void");
        } else {
            message.append(" died");
        }

        if (isFinal) {
            message.append(finalKillText);
        }

        return message.toString();
    }

    private void eliminate(BedwarsPlayer player) {
        player.setSpectator();

        if (player.getTeam().getSize() == 0) {
            game.getPlayerManager().broadcast("");
            game.getPlayerManager().broadcast("" + WHITE + BOLD + "TEAM ELIMINATION > " +
                    player.getTeam().getColor().getName() + " Team" + GRAY + " has been eliminated!");
            game.getPlayerManager().broadcast("");

            if (game.getAliveTeams().size() == 1) {
                game.setState(new PostGameState(game));
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        Player bukkitPlayer = event.getPlayer();
        BedwarsPlayer player = game.getPlayerManager().getOrCreatePlayer(bukkitPlayer);

        if (player.getTeam() == null || !player.getTeam().hasBed()) {
            //TODO teleport player on default map location (from swm)
            bukkitPlayer.teleport(game.getMap().getSpawn(TeamColor.RED));
            player.setSpectator();
            event.setJoinMessage(null);
            return;
        }

        bukkitPlayer.teleport(game.getMap().getSpawn(player.getTeam().getColor()));

        event.setJoinMessage(player.getName() + GRAY + " rejoined.");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        Player bukkitPlayer = event.getPlayer();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).isPresent()) return;
        BedwarsPlayer player = game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).get();

        if (!player.isSpectating()) {
            if (player.getTeam().getSize() <= 1) {
                player.getTeam().breakBed();
            }

            this.kill(player, event);
        }
        event.setQuitMessage(null);
    }

    //Prevent breaking blocks

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        block.setMetadata("playerPlaced", new FixedMetadataValue(game.getPlugin(), true));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.hasMetadata("playerPlaced")) {
            block.removeMetadata("playerPlaced", game.getPlugin());
            return;
        }

        event.setCancelled(true);
    }

    //Prevent player from teleporting with ender pearl after he dies

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnderPearlThrow(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof EnderPearl)) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;

        EnderPearl pearl = (EnderPearl) event.getEntity();
        UUID thrower = ((Player) event.getEntity().getShooter()).getUniqueId();

        Set<EnderPearl> playerPearls = pearls.getOrDefault(thrower, new HashSet<>());
        playerPearls.add(pearl);

        pearls.put(thrower, playerPearls);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnderPearlHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof EnderPearl)) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;

        EnderPearl pearl = (EnderPearl) event.getEntity();
        UUID thrower = ((Player) event.getEntity().getShooter()).getUniqueId();

        if (!pearls.containsKey(thrower)) return;

        Set<EnderPearl> playerPearls = pearls.get(thrower);
        playerPearls.remove(pearl);

        pearls.put(thrower, playerPearls);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public boolean removePlayerPearls(PlayerDeathEvent event) {
        UUID thrower = event.getEntity().getUniqueId();

        if (!pearls.containsKey(thrower)) return false;

        pearls.get(thrower).forEach(Entity::remove);
        pearls.remove(thrower);

        return true;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelTeamHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        Player bukkitPlayer = (Player) event.getEntity();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).isPresent()) return;
        BedwarsPlayer player = game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).get();

        Player bukkitDamager = (Player) event.getDamager();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitDamager).isPresent()) return;
        BedwarsPlayer damager = game.getPlayerManager().getBedwarsPlayer(bukkitDamager).get();

        if (!player.getTeam().equals(damager.getTeam())) return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onHit(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        Player bukkitPlayer = (Player) event.getEntity();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).isPresent()) return;
        BedwarsPlayer player = game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).get();

        Player bukkitDamager = (Player) event.getDamager();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitDamager).isPresent()) return;
        BedwarsPlayer damager = game.getPlayerManager().getBedwarsPlayer(bukkitDamager).get();

        player.setLastDamager(damager);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDeath(PlayerDeathEvent event) {
        Player bukkitPlayer = event.getEntity();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).isPresent()) return;
        BedwarsPlayer player = game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).get();

        event.getDrops().clear();
        event.setDroppedExp(0);

        bukkitPlayer.spigot().respawn();
        this.kill(player, event);
        event.setDeathMessage(null);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRespawn(PlayerRespawnEvent event) {
        Player bukkitPlayer = event.getPlayer();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).isPresent()) return;
        BedwarsPlayer player = game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).get();

        if (player.getTeam() == null) return;

        event.setRespawnLocation(game.getMap().getSpawn(player.getTeam().getColor()));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBedBreak(BlockBreakEvent event) {
        if (!event.getBlock().getType().equals(Material.BED_BLOCK)) return;

        Player bukkitPlayer = event.getPlayer();
        if (!game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).isPresent()) return;
        BedwarsPlayer player = game.getPlayerManager().getBedwarsPlayer(bukkitPlayer).get();

        me.math3w.bedwars.game.Map map = game.getMap();
        Location location = event.getBlock().getLocation().getBlock().getLocation();

        Team bedTeam = null;
        for (Team team : game.getTeams()) {
            if (map.getBedLocation(team.getColor()).contains(location)) {
                bedTeam = team;
                break;
            }
        }

        if (bedTeam == null || bedTeam.equals(player.getTeam())) return;

        event.setCancelled(true);
        bedTeam.breakBed();

        game.getPlayerManager().addStatistic(player, Statistic.BED_BREAKS);
        bukkitPlayer.playSound(bukkitPlayer.getLocation(), Sound.LEVEL_UP, 1, 1);

        game.getPlayerManager().broadcast("");
        game.getPlayerManager().broadcast("" + WHITE + BOLD + "BED DESTRUCTION > " + bedTeam.getColor().getName() +
                " Bed " + GRAY + "was destroyed by " + player.getName());
        game.getPlayerManager().broadcast("");

        for (BedwarsPlayer bedPlayer : bedTeam.getOnlinePlayers()) {
            Utils.setTitle(bedPlayer.getBukkitPlayer(),
                    RED + "Bed destroyed!", GRAY + "You will no longer respawn!", 5, 60, 5);
            bedPlayer.getBukkitPlayer().playSound(player.getBukkitPlayer().getLocation(), Sound.EXPLODE, 1, 1);
        }
    }

    //When resource is picked shop ui is updated to show which item player can afford
    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemPickup(PlayerPickupItemEvent event) {
        Player bukkitPlayer = event.getPlayer();

        Supplier<ShopMenu> shopOpened = () -> {
            if (bukkitPlayer.getOpenInventory() == null) return null;
            if (bukkitPlayer.getOpenInventory().getTopInventory() == null) return null;

            InventoryHolder holder = bukkitPlayer.getOpenInventory().getTopInventory().getHolder();
            if (!(holder instanceof ShopMenu)) return null;

            return (ShopMenu) holder;
        };

        if (shopOpened.get() == null) return;

        if (Arrays.stream(Resource.values())
                .noneMatch(resource -> resource.getItem().isSimilar(event.getItem().getItemStack())))
            return;

        Bukkit.getScheduler().scheduleSyncDelayedTask(game.getPlugin(), () -> {
            ShopMenu shop = shopOpened.get();
            if (shop == null) return;

            shop.open();
        }, 1L);
    }
}
