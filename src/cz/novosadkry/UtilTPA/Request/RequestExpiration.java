package cz.novosadkry.UtilTPA.Request;

import java.util.TimerTask;

public class RequestExpiration extends TimerTask {
    Request request;

    public RequestExpiration(Request request) {
        this.request = request;
    }

    public void run() {
        RequestManager.getInstance().get(request.to).remove(request);
        request.from.sendMessage("§cHráč §e"+ request.to.getName() +"§c neodpověděl na tvůj request.");
        request.to.sendMessage("§cNeodpověděl si na request hráče §e"+ request.from.getName());
    }
}

