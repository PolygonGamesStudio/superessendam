package server.service;

import server.Address;
import server.Subscriber;
import server.TimeHelper;
import server.base.GameMechanics;
import server.message.MessageSystem;

public class GameMechanicsImpl implements Subscriber, Runnable, GameMechanics {

    private final Address address;
    private final MessageSystem messageSystem;

    public GameMechanicsImpl(MessageSystem messageSystem) {
        this.address = new Address();
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().setAddressGM(address);

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
