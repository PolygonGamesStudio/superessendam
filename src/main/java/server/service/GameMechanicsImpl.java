package server.service;

import server.Address;
import server.Subscriber;
import server.TimeHelper;
import server.base.GameMechanics;
import server.message.MessageSystem;
import server.message.MsgCloseSocket;

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
        playerAmount = 2; // TODO: move to RS
        messageSystem.addService(this);
        messageSystem.getAddressService().setAddressGM(address);

    }

    public void userAdd(String room, Long userId) {
        if (roomNameToIds.get(room) == null) {
            Set idSet = new HashSet();
            idSet.add(userId);
            roomNameToIds.put(room, idSet);
        }
        else {
            if (roomNameToIds.get(room).size() <= playerAmount) {
                roomNameToIds.get(room).add(userId);
            }
            else {
                    messageSystem.sendMessage(new MsgCloseSocket(messageSystem.getAddressService().getAddressGM(), messageSystem.getAddressService().getAddressFE(), userId));
                }
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
