package cz.novosadkry.UtilTPA.Heads;

import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class HeadCacheService {
    private static HeadCacheService service = new HeadCacheService();

    public static HeadCacheService getInstance() {
        return service;
    }

    public HeadCache getCache() {
        return cache;
    }

    private final HeadCache cache = new HeadCache();
    private final Queue<Player> queue = new LinkedList<>();

    private BukkitTask cacheRefreshTask;
    private BukkitTask cacheQueueTask;

    public static final long cacheRefreshTick = 6000; // every 6 000 ticks (approx. 5 min)
    public static final long cacheQueueTick   = 20;   // every 20 ticks (approx. 1 sec)
                                                      //
                                                      // API limit is 600 requests per 10 min

    public void startCacheRefresh() {
        cacheRefreshTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(Main.class), () -> {
            LinkedList<UUID> toRemove = new LinkedList<UUID>();

            synchronized (cache) {
                cache.getData().forEach((k, v) -> {
                    if (v.ttl < System.currentTimeMillis()) {
                        Player player = Bukkit.getPlayer(k);

                        if (player != null && player.isOnline())
                            cache.cacheHead(player);
                        else
                            toRemove.add(k);
                    }
                });

                for (UUID k : toRemove)
                    cache.getData().remove(k);
            }
        }, 0, cacheRefreshTick);
    }

    public void stopCacheRefresh() {
        cacheRefreshTask.cancel();
    }

    public void startCacheQueue() {
        cacheQueueTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(Main.class), () -> {
            if (queue.peek() != null) {
                Player player = queue.poll();

                synchronized (cache) {
                    if (!cache.getData().containsKey(player.getUniqueId()))
                        cache.cacheHead(player);
                }
            }
        }, 0, cacheQueueTick);
    }

    public void stopCacheQueue() {
        cacheQueueTask.cancel();
    }

    public void enqueueHead(Player player) {
        queue.add(player);
    }
}
