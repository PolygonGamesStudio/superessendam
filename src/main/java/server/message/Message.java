package server.message;

import server.Address;

public abstract class Message {
    private Address from;
    private Address to;

    public Message(Address from, Address to) {
        this.from = from;
        this.to = to;
    }

    protected Address getFrom() {
        return from;
    }

    protected Address getTo() {
        return to;
    }

}
