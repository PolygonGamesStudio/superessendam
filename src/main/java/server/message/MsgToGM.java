package server.message;

import server.Address;
import server.Subscriber;
import server.base.GameMechanics;
import server.message.Msg;

public abstract class MsgToGM extends Msg {
    public MsgToGM(Address from, Address to) {
        super(from, to);
    }

    public void exec(Subscriber subscriber) {
        if (subscriber instanceof GameMechanics) {
            exec((GameMechanics) subscriber);
        }
    }

    abstract void exec(GameMechanics gameMechanics);


}
