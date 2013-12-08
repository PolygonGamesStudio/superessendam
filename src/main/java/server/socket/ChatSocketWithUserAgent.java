package server.socket;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ClientEndpoint
//@ServerEndpoint(value = "/chat/{room-name}")
@ServerEndpoint(value = "/chat", configurator = GetUserAgentConfigurator.class)
public class ChatSocketWithUserAgent {
    private static final Map<String, ChatSocketWithUserAgent> sockets = new ConcurrentHashMap<>();
    private Session session;
    private String userId;
    private String userAgent;

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
        for (ChatSocketWithUserAgent chatSocket : sockets.values()) {
            if (chatSocket == this)
                continue;
            chatSocket.sendToClient(message);
        }
    }

    @OnOpen
    public void onWebSocketConnect(Session session, @PathParam("room-name") String roomName, EndpointConfig config) {
        System.out.println("Socket connected: " + session);
        this.session = session;
        this.userId = this.getId();

        ChatSocketWithUserAgent.sockets.put(userId, this);
        this.sendToClient("Hello, user " + this.userId);
        this.broadcast("New user appeared: user " + this.userId);


        this.userAgent = (String) config.getUserProperties().get("User-Agent");


        if (this.userAgent.equals("mobile")) {
            this.sendToClient("you are mobile");
        }
        else {
            this.sendToClient("you are desktop");
        }
    }

    @OnMessage
    public void onWebSocketMessage(String message) {
        System.out.println("Received message: " + message);
        this.broadcast("User " + this.userId + ": " + message);
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        System.out.println("Socket closed because: " + reason);
        if (ChatSocketWithUserAgent.sockets.containsKey(this.userId)) {
            ChatSocketWithUserAgent.sockets.remove(this.userId);
            this.broadcast("User " + this.userId + " has left...");
        }
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }
}
