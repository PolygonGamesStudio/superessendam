package server.message;

import server.Address;
import server.base.Frontend;

public class MsgBroadcastResponse extends MsgToFrontend {
    private String response;
    private Long userId;

    public MsgBroadcastResponse(Address from, Address to, String response, Long userId) {
        super(from, to);
        this.response = response;
        this.userId = userId;
    }

    @Override
    void exec(Frontend frontend) {
        frontend.broadcastToSockets(userId, response);
    }
}
