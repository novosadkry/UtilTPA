package cz.novosadkry.UtilTPA.Heads;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class HeadCache {
    public static class HeadCacheData {
        public String value;
        public long ttl;
    }

    private Map<UUID, HeadCacheData> cache;
    public final long cacheDataTTL;

    public HeadCache(long cacheDataTTL){
        cache = new HashMap<>();

        this.cacheDataTTL = cacheDataTTL;
    }

    public Map<UUID, HeadCacheData> getData() {
        return cache;
    }

    @SuppressWarnings("deprecation")
    public ItemStack getHead(Player player) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        HeadCacheData cacheData = cache.get(player.getUniqueId());

        if (cacheData != null && cacheData.value != null) {
            UUID hashAsId = new UUID(cacheData.value.hashCode(), cacheData.value.hashCode());

            return Bukkit.getUnsafe().modifyItemStack(head,
                    "{display:{Name:'{\"text\":\"" + player.getDisplayName() + "\"}'}, SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + cacheData.value + "\"}]}}}"
            );
        }

        else {
            String defaultSkin = (player.getUniqueId().hashCode() % 2 != 0)
                    ? "MHF_Alex"
                    : "MHF_Steve";

            return Bukkit.getUnsafe().modifyItemStack(head,
                    "{display:{Name:'{\"text\":\"" + player.getDisplayName() + "\"}'},SkullOwner:\"" + defaultSkin + "\"}"
            );
        }
    }

    void cacheHead(Player player) {
        if (!cache.containsKey(player.getUniqueId()))
            cache.put(player.getUniqueId(), new HeadCacheData());

        HeadCacheData cacheData = cache.get(player.getUniqueId());
        cacheData.value = getHeadValue(player.getName());
        cacheData.ttl = System.currentTimeMillis() + cacheDataTTL;
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

            String decoded = new String(Base64.decode(value));
            json = gson.fromJson(decoded, JsonObject.class);

            String skinUrl = json.getAsJsonObject("textures")
                    .getAsJsonObject("SKIN")
                    .get("url").getAsString();

            byte[] headValue = ("{\"textures\":{\"SKIN\":{\"url\":\"" + skinUrl + "\"}}}").getBytes();
            return Base64.encode(headValue);
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
