package server.message;

import server.Address;
import server.Subscriber;
import server.service.GameMechanics;

public abstract class MsgToGM extends Msg {
    public MsgToGM(Address from, Address to) {
        super(from, to);
    }

    public void exec(Subscriber subscriber) {
        if (subscriber instanceof GameMechanics) {
            exec((GameMechanics) subscriber);
        }
    }

    abstract void exec(GameMechanics frontend);


}
