package server.socket;

import org.eclipse.jetty.util.ConcurrentHashSet;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat", configurator = GetUserAgentConfigurator.class)
public class ChatSocketWithUserAgentToken {

//    private static final Map<String, ChatSocketWithUserAgentToken> sockets = new ConcurrentHashMap<>();
    private Session session;
    private String userId;
    private String userAgent;

    private static Map<String, Set<ChatSocketWithUserAgentToken>> chats = new ConcurrentHashMap<>();
    private String token;
    private Boolean isAuthorized = false;   // флаг означающий, состоит ли человек в беседе
    private boolean hasName = false;
    private String name;

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
    public void onWebSocketConnect(Session session, EndpointConfig config) {
        System.out.println("Socket connected: " + session);
        this.session = session;
        this.userId = this.getId();

//        ChatSocketWithUserAgentToken.sockets.put(userId, this);
        this.sendToClient("Hello, user " + this.userId);
//        this.broadcast(this.token, "New user appeared: user " + this.userId);


        this.userAgent = (String) config.getUserProperties().get("User-Agent");

        if (this.userAgent.contains("Android")) {
            this.sendToClient("you are mobile");
        } else {
            this.sendToClient("you are desktop");
        }

//        this.sendToClient("Enter your name:");

    }

    @OnMessage
    public void onWebSocketMessage(String message) {
        System.out.println("Received message: " + message);

        if (!this.isAuthorized && message.equals("service:create")) {
            this.token = this.getRandomHash();
            this.isAuthorized = true;

            this.sendToClient("Auth token: " + this.token);

            Set<ChatSocketWithUserAgentToken> relatedSockets = new ConcurrentHashSet<>();
            relatedSockets.add(this);
            ChatSocketWithUserAgentToken.chats.put(this.token, relatedSockets);

            this.sendToClient("Enter your name:");
            return;
        }

        if (!this.isAuthorized && message.equals("service:connect")) {
            this.sendToClient("Enter token:");

            return;
        }

        if (this.isAuthorized) {
            if (this.hasName)
                this.broadcast(this.token, this.name + ": " + message);
            else {
                this.name = message;
                this.hasName = true;
                this.sendToClient("Long live the ... " + this.name);
            }
        } else {
            if (ChatSocketWithUserAgentToken.chats.keySet().contains(message)) {
                this.token = message;
                this.isAuthorized = true;
                ChatSocketWithUserAgentToken.chats.get(message).add(this);
                this.broadcast(this.token, "new user has connected"); // FIXME: smart broadcast
                this.sendToClient("Enter name:");
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
