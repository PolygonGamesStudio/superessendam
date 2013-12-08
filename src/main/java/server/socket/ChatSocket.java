package server.socket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ClientEndpoint
@ServerEndpoint(value = "/chat")
public class ChatSocket {
    private static final Map<String, ChatSocket> sockets = new ConcurrentHashMap<>();
    private Session session;
    private String userId;

    private String getId() {
        return Integer.toHexString(this.hashCode());
    }

    private void sendToClient(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String message) {
        for (ChatSocket chatSocket : sockets.values()) {
            if (chatSocket == this)
                continue;
            chatSocket.sendToClient(message);
        }
    }

    @OnOpen
    public void onWebSocketConnect(Session session) {
        System.out.println("Socket connected: " + session);
        this.session = session;
        this.userId = this.getId();

        ChatSocket.sockets.put(userId, this);
        this.sendToClient("Hello, user " + this.userId);
        this.broadcast("New user appeared: user " + this.userId);
    }

    @OnMessage
    public void onWebSocketMessage(String message) {
        System.out.println("Received message: " + message);
        this.broadcast("User " + this.userId + ": " + message);
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        System.out.println("Socket closed because: " + reason);
        if (ChatSocket.sockets.containsKey(this.userId)) {
            ChatSocket.sockets.remove(this.userId);
            this.broadcast("User " + this.userId + " has left...");
        }
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }
}
