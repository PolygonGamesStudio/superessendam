package server.message;

import server.Address;
import server.base.Frontend;

public class MsgRoomDesicion extends MsgToFrontend {
    private Long userIdOfSocket;
    private String roomName;
    private boolean isConnectionToRoomAllowed;

    public MsgRoomDesicion(Address from, Address to, String room, Long userIdOfSocket, boolean isConnectionToRoomAllowed) {
        super(from, to);
        this.userIdOfSocket = userIdOfSocket;
        this.roomName = room;
        this.isConnectionToRoomAllowed = isConnectionToRoomAllowed;
    }

    @Override
    void exec(Frontend frontend) {
        frontend.makeDecisionAboutPersonInRoom(userIdOfSocket, roomName, isConnectionToRoomAllowed);
    }
}
