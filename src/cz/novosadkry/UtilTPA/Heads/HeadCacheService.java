package cz.novosadkry.UtilTPA.Heads;

import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class HeadCacheService {
    public HeadCacheService(long cacheDataTTL, long cacheRefreshTick, long cacheQueueTick) {
        cache = new HeadCache(cacheDataTTL);
        queue = new LinkedList<>();

        this.cacheRefreshTick = cacheRefreshTick;
        this.cacheQueueTick = cacheQueueTick;
    }

    public HeadCache getCache() {
        return cache;
    }

    private final HeadCache cache;
    private final Queue<Player> queue;

    private BukkitTask cacheRefreshTask;
    private BukkitTask cacheQueueTask;

    public final long cacheRefreshTick;
    public final long cacheQueueTick;

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
