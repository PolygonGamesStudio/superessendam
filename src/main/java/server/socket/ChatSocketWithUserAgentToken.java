package server.socket;

import org.eclipse.jetty.util.ConcurrentHashSet;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ClientEndpoint
//@ServerEndpoint(value = "/chat/{room-name}")
@ServerEndpoint(value = "/chat", configurator = GetUserAgentConfigurator.class)
public class ChatSocketWithUserAgentToken {

//    private static final Map<String, ChatSocketWithUserAgentToken> sockets = new ConcurrentHashMap<>();
    private Session session;
    private String userId;
    private String userAgent;

    private static Map<String, Set<ChatSocketWithUserAgentToken>> chats = new ConcurrentHashMap<>();
    private String token;
    private Boolean isAuthorized = false;

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

    private void broadcast(String token, String message) {
        for (ChatSocketWithUserAgentToken chatSocket : chats.get(token)) {
            if (chatSocket == this)
                continue;
            chatSocket.sendToClient(message);
        }
    }

    private String getRandomHash() {
        return UUID.randomUUID().toString().substring(0,5); // вернуть 5 первых символов
    }

    @OnOpen
    public void onWebSocketConnect(Session session, @PathParam("room-name") String roomName, EndpointConfig config) {
        System.out.println("Socket connected: " + session);
        this.session = session;
        this.userId = this.getId();

//        ChatSocketWithUserAgentToken.sockets.put(userId, this);
        this.sendToClient("Hello, user " + this.userId);
//        this.broadcast(this.token, "New user appeared: user " + this.userId);


        this.userAgent = (String) config.getUserProperties().get("User-Agent");


        if (this.userAgent.equals("mobile")) {
            this.sendToClient("you are mobile");
            this.sendToClient("Enter token:");
        }
        else {
            this.token = this.getRandomHash();
            this.isAuthorized = true;

            this.sendToClient("you are desktop");
            this.sendToClient("Auth token: " + this.token);

            Set<ChatSocketWithUserAgentToken> relatedSockets = new ConcurrentHashSet<>();
            relatedSockets.add(this);
            ChatSocketWithUserAgentToken.chats.put(this.token, relatedSockets);
        }
    }

    @OnMessage
    public void onWebSocketMessage(String message) {
        System.out.println("Received message: " + message);
        if (this.isAuthorized) {
            this.broadcast(this.token, "User " + this.userId + ": " + message);
        }
        else {
            if (ChatSocketWithUserAgentToken.chats.keySet().contains(message)) {
                this.token = message;
                this.isAuthorized = true;
                ChatSocketWithUserAgentToken.chats.get(message).add(this);
                this.broadcast(this.token, "User " + this.userId + " has connected"); // FIXME: smart broadcast
            }
            else {
                this.sendToClient("Invalid token! Try again...");
            }
        }
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
        System.out.println("Socket closed because: " + reason);
        if (ChatSocketWithUserAgentToken.chats.get(this.token).contains(this)) {
            ChatSocketWithUserAgentToken.chats.get(this.token).remove(this);
            if (!ChatSocketWithUserAgentToken.chats.get(this.token).isEmpty()) {
                this.broadcast(this.token, "User " + this.userId + " has left...");
            }
            else {
                ChatSocketWithUserAgentToken.chats.remove(this.token);
            }
        }
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }
}
