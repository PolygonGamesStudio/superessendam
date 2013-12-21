package server.service;

import org.json.JSONException;
import org.json.JSONObject;
import server.Address;
import server.Subscriber;
import server.TimeHelper;
import server.base.GameMechanics;
import server.message.MessageSystem;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GameMechanicsImpl implements Subscriber, Runnable, GameMechanics {

    private final Address address;
    private final MessageSystem messageSystem;
    private Map<String, Set<Long>> roomNameToIds = new ConcurrentHashMap<>();
    private Integer playerAmount;

    public GameMechanicsImpl(MessageSystem messageSystem) {
        this.address = new Address();
        this.messageSystem = messageSystem;
        playerAmount = 1; // TODO: move to RS
        messageSystem.addService(this);
        messageSystem.getAddressService().setAddressGM(address);

    }

    public String getUsersIdJson(String roomName) {
//        roomNameToIds.get(roomName)
        JSONObject jsonObject = new JSONObject();
        for (Long id : roomNameToIds.get(roomName)) {
            try {
                jsonObject.append("id", id);
            } catch (JSONException e) {
                System.out.println("error with append to json");
            }
        }
        System.out.println(jsonObject.toString());
        return jsonObject.toString();

    }

    public boolean userAdd(String room, Long userId) {
        if (roomNameToIds.get(room) == null) {
            Set idSet = new HashSet();
            idSet.add(userId);
            roomNameToIds.put(room, idSet);
            return true;
        }
        else {
            return roomNameToIds.get(room).size() < playerAmount;
        }

    }

    public void handleEvent(String stuff) {
        System.out.println("GM");
        System.out.println("Hello, " + stuff);
        System.out.println("GM");
    }

    public void run() {
        while (true) {
            messageSystem.execForSubscriber(this);
            TimeHelper.sleep(10);
        }

    }

    public Address getAddress() {
        return address;
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }
}
