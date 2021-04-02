package cz.novosadkry.UtilTPA.Requests.Managers;

import cz.novosadkry.UtilTPA.Requests.ExpiringRequest;
import cz.novosadkry.UtilTPA.Requests.Request;
import cz.novosadkry.UtilTPA.Requests.RequestPlayer;
import cz.novosadkry.UtilTPA.Services.IService;

import java.util.LinkedList;
import java.util.Map;

public interface IRequestManager extends IService {
    Map<RequestPlayer, LinkedList<Request>> getAll();

    LinkedList<Request> getAllPlayer(RequestPlayer player);

    Request get(RequestPlayer to);

    Request getFrom(RequestPlayer to, RequestPlayer from);

    boolean hasRequests(RequestPlayer to);

    boolean hasRequest(RequestPlayer to, Request request);

    void sendRequest(Request request);

    void timeoutRequest(ExpiringRequest request);

    void acceptRequest(Request request);

    void denyRequest(Request request);
}
