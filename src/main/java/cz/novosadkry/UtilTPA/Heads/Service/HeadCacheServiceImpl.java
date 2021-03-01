package cz.novosadkry.UtilTPA.Heads.Service;

import cz.novosadkry.UtilTPA.Heads.Cache.HeadCache;
import cz.novosadkry.UtilTPA.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class HeadCacheServiceImpl implements HeadCacheService {
    public HeadCacheServiceImpl(long cacheDataTTL, long cacheRefreshTick, long cacheQueueTick) {
        cache = new HeadCache(cacheDataTTL);
        queue = new LinkedList<>();

        this.cacheRefreshTick = cacheRefreshTick;
        this.cacheQueueTick = cacheQueueTick;
    }

    private final HeadCache cache;
    private final Queue<Player> queue;

    private BukkitTask cacheRefreshTask;
    private BukkitTask cacheQueueTask;

    public final long cacheRefreshTick;
    public final long cacheQueueTick;

    @Override
    public ItemStack getHead(Player player) {
        synchronized (cache) {
            return cache.getHead(player);
        }
    }

    @Override
    public void enqueueHead(Player player) {
        synchronized (queue) {
            queue.add(player);
        }
    }

    @Override
    public void initialize() {
        startCacheQueue();
        startCacheRefresh();
    }

    public void startCacheQueue() {
        cacheQueueTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(Main.class), () -> {
            synchronized (queue) {
                if (queue.peek() != null) {
                    Player player = queue.poll();

                    synchronized (cache) {
                        if (!cache.getData().containsKey(player.getUniqueId()))
                            cache.cacheHead(player);
                    }
                }
            }
        }, 0, cacheQueueTick);
    }

    public void startCacheRefresh() {
        cacheRefreshTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(Main.class), () -> {
            synchronized (cache) {
                LinkedList<UUID> toRemove = new LinkedList<UUID>();

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
