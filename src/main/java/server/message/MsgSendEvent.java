package server.message;

import server.Address;
import server.service.GameMechanics;

public class MsgSendEvent extends MsgToGM {
    private String event;

    public MsgSendEvent(Address from, Address to, String event) {
        super(from, to);
        this.event = event;
    }


    void exec(GameMechanics gameMechanics) {
        gameMechanics.printStuff(this.event);
//        gameMechanics.getMessageSystem().sendMessage(new MsgUpdateUserId(getTo(), getFrom(), sessionId, id));
//        здесь сообщение отправляется тому, от кого оно пришло
    }
}
