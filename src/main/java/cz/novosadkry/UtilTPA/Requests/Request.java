package cz.novosadkry.UtilTPA.Requests;

public class Request {
    private final RequestPlayer from, to;

    public RequestPlayer getFrom() {
        return from;
    }

    public RequestPlayer getTo() {
        return to;
    }

    public Request(RequestPlayer from, RequestPlayer to) {
        this.from = from;
        this.to = to;
    }

    public Request(String from, String to) {
        this(new RequestPlayer(from), new RequestPlayer(to));
    }

    public Request(Request request) {
        this.from = request.from;
        this.to = request.to;
    }

    public boolean valid() {
        return !from.equals(to);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request req = (Request) o;

        return from.equals(req.from)
                && to.equals(req.to);
    }

    @Override
    public String toString() {
        return "Request{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }

    public void onSent() { }

    public void onResolved() { }
}