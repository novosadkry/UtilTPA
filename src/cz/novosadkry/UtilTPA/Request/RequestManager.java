package cz.novosadkry.UtilTPA.Request;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class RequestManager {
    private static RequestManager manager;
    private HashMap<Player, LinkedList<Request>> requests = new HashMap();

    public HashMap<Player, LinkedList<Request>> getAll() {
        return requests;
    }

    public LinkedList<Request> get(Player player) {
        return requests.get(player);
    }

    // Get: O(1), Remove: O(1)
    public Request pop(Player to) {
        return requests.get(to).pop();
    }

    // Get: O(n), Remove: O(1)
    public Request popFrom(Player to, Player from) {
        Request request = null;

        Iterator<Request> it = requests.get(to).iterator();
        while (it.hasNext()) {
            Request r = it.next();

            if (r.getFrom() == from) {
                request = r;
                it.remove();
                break;
            }
        }

        return request;
    }

    public boolean hasRequests(Player to) {
        return requests.containsKey(to) && !requests.get(to).isEmpty();
    }

    public static RequestManager getInstance() {
        if (manager == null)
            manager = new RequestManager();

        return manager;
    }
}
