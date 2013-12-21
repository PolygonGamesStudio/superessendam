package server.message;

import server.Address;
import server.Subscriber;
import server.base.AddressService;
import server.service.AddressServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageSystem {
    private Map<Address, ConcurrentLinkedQueue<Msg>> messages = new HashMap<>();
    private AddressService addressService = new AddressServiceImpl();

    public void addService(Subscriber subscriber) {
        messages.put(subscriber.getAddress(), new ConcurrentLinkedQueue<Msg>());
    }

    public void sendMessage(Msg message) {
        Queue<Msg> messageQueue = messages.get(message.getTo()); // получить очередь того, кому предназначается сообщение
        messageQueue.add(message);
    }

    public void execForSubscriber(Subscriber subscriber) {
        Queue<Msg> messageQueue = messages.get(subscriber.getAddress());
        if (messageQueue == null) {
            return;
        }
        while (!messageQueue.isEmpty()) {
            /**
             * Retrieves and removes the head of this queue,
             * or returns <tt>null</tt> if this queue is empty.
             *
             * @return the head of this queue, or <tt>null</tt> if this queue is empty
             */
            Msg message = messageQueue.poll();
            message.exec(subscriber);
        }
    }

    public AddressService getAddressService() {
        return addressService;
    }
}
