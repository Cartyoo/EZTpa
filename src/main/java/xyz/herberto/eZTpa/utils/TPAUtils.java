package xyz.herberto.eZTpa.utils;

import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.herberto.eZTpa.EZTpa;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TPAUtils {
    @Getter
    public static final Map<UUID, TPAData> requests = new HashMap<>();
    @Getter
    public static final Map<UUID, BukkitRunnable> tasks = new HashMap<>();


    public static void startClearTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                requests.entrySet().removeIf(entry -> (time - entry.getValue().getSentTime()) > (EZTpa.getInstance().getConfig().getLong("requests-clear-interval") * 1000L));
            }
        }.runTaskTimer(EZTpa.getInstance(), 0, (20 * 10));
    }

}
