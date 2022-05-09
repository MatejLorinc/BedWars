package me.math3w.bedwars.shop;

import me.math3w.bedwars.game.Game;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityTypes;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShopUtils {
    private ShopUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    public static Trader spawnTrader(Game game, Location location) {
        Trader trader = new Trader(game, location);
        ((CraftWorld) location.getWorld()).getHandle().addEntity(trader, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return trader;
    }

    //Override default entity with custom entity
    public static void registerEntity(String name, int id, Class<? extends EntityInsentient> customClass) {
        try {
            List<Map<?, ?>> dataMap = new ArrayList<>();
            for (Field f : EntityTypes.class.getDeclaredFields()) {
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
                    f.setAccessible(true);
                    dataMap.add((Map<?, ?>) f.get(null));
                }
            }

            if (dataMap.get(2).containsKey(id)) {
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }

            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }
}
