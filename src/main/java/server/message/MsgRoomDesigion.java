package server.message;

import server.Address;
import server.base.Frontend;

public class MsgRoomDesigion extends MsgToFrontend {
    private Long userIdOfSocket;
    private String roomName;
    private boolean isConnectionToRoomAllowed;

    public MsgRoomDesigion(Address from, Address to, String room, Long userIdOfSocket, boolean isConnectionToRoomAllowed) {
        super(from, to);
        this.userIdOfSocket = userIdOfSocket;
        this.roomName = room;
        this.isConnectionToRoomAllowed = isConnectionToRoomAllowed;
    }

    @Override
    void exec(Frontend frontend) {
        frontend.makeDesigionAboutPersonInRoom(userIdOfSocket, roomName, isConnectionToRoomAllowed);
    }
}
