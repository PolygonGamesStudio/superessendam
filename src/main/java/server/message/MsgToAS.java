package server.message;

import server.AccountService;
import server.Address;
import server.Subscriber;

public abstract class MsgToAS extends Message {

    public MsgToAS(Address from, Address to) {
        super(from, to);
    }

    void exec(Subscriber subscriber) {
        if (subscriber instanceof AccountService) {
            exec((AccountService) subscriber);
        }
    }

    abstract void exec(AccountService accountService);
}
