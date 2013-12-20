package server.message;

import server.Address;
import server.base.Frontend;

public class MsgCloseSocket extends MsgToFrontend {

    private Long idForSocket;

    public MsgCloseSocket(Address from, Address to, Long idForSocket) {
        super(from, to);
        this.idForSocket = idForSocket;
    }

    @Override
    void exec(Frontend frontend) {
        frontend.closeSocket(idForSocket);
    }
}
