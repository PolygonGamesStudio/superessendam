package server.message;

import server.Address;
import server.Subscriber;
import server.base.Frontend;
import server.message.Msg;

public abstract class MsgToFrontend extends Msg {
    public MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    public void exec(Subscriber subscriber) {
        if (subscriber instanceof Frontend) {
            exec((Frontend) subscriber);
        }
    }

    abstract void exec(Frontend frontend);


}
