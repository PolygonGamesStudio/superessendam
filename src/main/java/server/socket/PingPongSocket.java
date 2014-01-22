package server.socket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/ping")
public class PingPongSocket {

    private String posX;
    private String posY;
    private String betta;
    private String gamma;

    private Session session;
    private String userId;

    private static Map<String, PingPongSocket> sockets = new ConcurrentHashMap<>();

    private String getId() {
        return Integer.toHexString(this.hashCode());
    }

    private void sendToClient(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String message) {
        for (PingPongSocket chatSocket : PingPongSocket.sockets.values()) {
            if (chatSocket == this)
                continue;
            chatSocket.sendToClient(message);
        }
    }

    @OnOpen
    public void onWebSocketConnect(Session session) {
//        session.setMaxTextMessageBufferSize(10000);
//        session.setMaxBinaryMessageBufferSize(10000);

//        session.setMaxIdleTimeout(50000);
        System.out.println("Socket connected: " + session);
        this.session = session;
        this.userId = this.getId();

        PingPongSocket.sockets.put(userId, this);

//        String mes = "Hello, user " + this.userId;
//        this.sendToClient("Hello, user " + this.userId);    // TODO: make initial sens to one who opened the socket
//        this.broadcast("New user appeared: user " + this.userId);
    }

    @OnMessage
    public void onWebSocketMessage(String message) {
        System.out.println("Received rotation: " + message);

//        String response = null; // TODO: add response
//        this.broadcast(response);
//        this.broadcast(message);  // FIXME: uncomment here
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        System.out.println("Socket closed because: " + reason);
        if (PingPongSocket.sockets.containsKey(this.userId)) {
            PingPongSocket.sockets.remove(this.userId);
            this.broadcast("User " + this.userId + " has left...");  // TODO: fix or delete message
        }
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }
}
