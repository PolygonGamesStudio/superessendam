package server.socket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ClientEndpoint
@ServerEndpoint(value = "/chat")
public class ChatSocket {

    @OnOpen
    public void onWebSocketConnect(Session session) {
        System.out.println("Socket connected: " + session);
    }

    @OnMessage
    public void onWebSocketMessage(String message) {
        System.out.println("Received message: " + message);
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        System.out.println("Socket closed because: " + reason);
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }
}
