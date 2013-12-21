package server.message;


import server.Address;
import server.base.GameMechanics;

public class MsgSendEvent extends MsgToGM {
    private String event;
    private Long userId;

    public MsgSendEvent(Address from, Address to, String event, Long userId) {
        super(from, to);
        this.event = event;
        this.userId = userId;
    }

    @Override
    void exec(GameMechanics gameMechanics) {
        String response = gameMechanics.handleEvent(event, userId);
        // TODO: send message here
        gameMechanics.getMessageSystem().sendMessage(new MsgBroadcastResponse(getTo(), getFrom(), response, userId));
//        здесь сообщение отправляется тому, от кого оно пришло
    }
}
