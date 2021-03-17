package cz.novosadkry.UtilTPA.Heads.Cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import cz.novosadkry.UtilTPA.Log;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HeadCache {
    public final long cacheDataTTL;
    private final Map<String, HeadCacheItem> cache;

    public HeadCache(long cacheDataTTL) {
        this.cacheDataTTL = cacheDataTTL;
        cache = new ConcurrentHashMap<>();
    }

    public Map<String, HeadCacheItem> getData() {
        return cache;
    }

    public boolean hasHead(String player) {
        return cache.containsKey(player);
    }

    public ItemStack getHead(String player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        HeadCacheItem cacheItem = cache.get(player);

        if (cacheItem != null && cacheItem.getValue() != null) {
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);

            PropertyMap propertyMap = profile.getProperties();
            propertyMap.put("textures", new Property("textures", cacheItem.getValue()));

            ItemMeta headMeta = head.getItemMeta();
            headMeta.setDisplayName(player);

            try {
                Field headMetaProfileField = headMeta.getClass().getDeclaredField("profile");
                headMetaProfileField.setAccessible(true);
                headMetaProfileField.set(headMeta, profile);
            } catch (Exception e) {
                Log.severe(e);
            }

            head.setItemMeta(headMeta);
        }

        else {
            String defaultSkin = (player.hashCode() % 2 != 0)
                    ? "MHF_Alex"
                    : "MHF_Steve";

            GameProfile profile = new GameProfile(UUID.randomUUID(), defaultSkin);

            ItemMeta headMeta = head.getItemMeta();
            headMeta.setDisplayName(player);

            try {
                Field headMetaProfileField = headMeta.getClass().getDeclaredField("profile");
                headMetaProfileField.setAccessible(true);
                headMetaProfileField.set(headMeta, profile);
            } catch (Exception e) {
                Log.severe(e);
            }

            head.setItemMeta(headMeta);
        }

        return head;
    }

    public void removeHead(String player) {
        cache.remove(player);
    }

    public void cacheHead(String player) {
        String value = getHeadValue(player);
        long ttl = System.currentTimeMillis() + cacheDataTTL;

        cache.put(player, new HeadCacheItem(value, ttl));
    }

    public List<String> removeExpired() {
        List<String> toRemove = new ArrayList<>();

        cache.forEach((k, v) -> {
            if (v.getTTL() < System.currentTimeMillis())
                toRemove.add(k);
        });

        for (String k : toRemove)
            cache.remove(k);

        return toRemove;
    }

    private String getHeadValue(String name) {
        try {
            Gson gson = new GsonBuilder().create();
            String id = getPlayerId(name);

            if (id == null)
                return null;

            String signature = getPlayerSignature(id);

            JsonObject json = gson.fromJson(signature, JsonObject.class);
            String value = json.getAsJsonArray("properties")
                    .get(0)
                    .getAsJsonObject()
                    .get("value").getAsString();

            String decoded = new String(Base64.getDecoder().decode(value));
            json = gson.fromJson(decoded, JsonObject.class);

            String skinUrl = json.getAsJsonObject("textures")
                    .getAsJsonObject("SKIN")
                    .get("url").getAsString();

            byte[] headValue = ("{\"textures\":{\"SKIN\":{\"url\":\"" + skinUrl + "\"}}}").getBytes();
            return new String(Base64.getEncoder().encode(headValue));
        } catch (Exception ignored) { }

        return null;
    }

    private String getPlayerId(String name) {
        try {
            Gson gson = new GsonBuilder().create();

            String content = getAPIContent("https://api.mojang.com/users/profiles/minecraft/" + name);

            JsonObject json = gson.fromJson(content, JsonObject.class);
            return json.get("id").getAsString().replaceAll("\"", "");
        } catch (Exception ignored) { }

        return null;
    }

    private String getPlayerSignature(String id) {
        try {
            return getAPIContent("https://sessionserver.mojang.com/session/minecraft/profile/" + id);
        } catch (Exception ignored) { }

        return null;
    }

    private String getAPIContent(String url) throws IOException {
        URL _url = new URL(url);

        try (Scanner scanner = new Scanner(_url.openStream())){
            StringBuilder buffer = new StringBuilder();

            while (scanner.hasNext())
                buffer.append(scanner.next());

            return buffer.toString();
        }
    }
}
