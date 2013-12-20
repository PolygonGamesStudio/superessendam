package server.message;

import server.Address;
import server.Subscriber;
import server.base.AccountService;
import server.message.Msg;

public abstract class MsgToAS extends Msg {

    public MsgToAS(Address from, Address to) {
        super(from, to);
    }

    void exec(Subscriber subscriber) {
        if (subscriber instanceof AccountService) {
            exec((AccountService) subscriber);
        }
    }

    abstract void exec(AccountService accountServiceImpl);
}
