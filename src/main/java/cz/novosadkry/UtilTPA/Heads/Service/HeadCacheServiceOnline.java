package cz.novosadkry.UtilTPA.Heads.Service;

import cz.novosadkry.UtilTPA.BungeeCord.Drivers.BungeeDriver;
import cz.novosadkry.UtilTPA.Heads.Cache.HeadCache;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HeadCacheServiceOnline implements HeadCacheService {
    public HeadCacheServiceOnline(long cacheDataTTL, long cacheRefreshTick, long cacheQueueTick) {
        cache = new HeadCache(cacheDataTTL);
        queue = new ConcurrentLinkedQueue<>();

        this.cacheRefreshTick = cacheRefreshTick;
        this.cacheQueueTick = cacheQueueTick;
    }

    private final HeadCache cache;
    private final Queue<String> queue;

    private BukkitTask cacheRefreshTask;
    private BukkitTask cacheQueueTask;

    public final long cacheRefreshTick;
    public final long cacheQueueTick;

    @Override
    public ItemStack getHead(String player) {
        return cache.getHead(player);

    }

    public void removeHead(String player) {
        cache.removeHead(player);
    }

    @Override
    public void enqueueHead(String player) {
        if (!queue.contains(player))
            queue.add(player);
    }

    @Override
    public void initialize() {
        startCacheQueue();
        startCacheRefresh();
    }

    public void startCacheQueue() {
        cacheQueueTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(Main.class), () -> {
            if (queue.peek() != null) {
                String player = queue.poll();
                cache.cacheHead(player);
            }
        }, 0, cacheQueueTick);
    }

    public void startCacheRefresh() {
        cacheRefreshTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(Main.class), () -> {
            BungeeDriver bungeeDriver = Main.getInstance().getBungeeDriver();

            List<String> playerList = bungeeDriver.getPlayerList();
            List<String> removed = cache.removeExpired();

            for (String player : removed)
            {
                if (playerList.contains(player))
                    enqueueHead(player);
            }
        }, 0, cacheRefreshTick);
    }

    public void stopCacheQueue() {
        cacheQueueTask.cancel();
    }

    public void stopCacheRefresh() {
        cacheRefreshTask.cancel();
    }

    @Override
    public void terminate() {
        stopCacheQueue();
        stopCacheRefresh();
    }
}
