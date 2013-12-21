package server.message;

import server.Address;
import server.base.GameMechanics;

public class MsgUserAdded extends MsgToGM {

    private final String room;
    private final Long userId;

    public MsgUserAdded(Address from, Address to, String room, Long id) {
        super(from, to);
        this.room = room;
        this.userId = id;
    }

    @Override
    void exec(GameMechanics gameMechanics) {
        boolean isConnectionToRoomAllowed = gameMechanics.userAdd(room, userId);
        gameMechanics.getMessageSystem().sendMessage(new MsgRoomDesicion(getTo(), getFrom(), room, userId, isConnectionToRoomAllowed));
        // TODO: отправить пересчет количества игроков в комнатах
    }
}
