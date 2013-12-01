package server;

import java.util.concurrent.atomic.AtomicInteger;

public class Address {
    static private AtomicInteger subscriberIdCreator = new AtomicInteger();
    final private int subscriberId;

    public Address() {
        this.subscriberId = subscriberIdCreator.incrementAndGet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (subscriberId != address.subscriberId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return subscriberId;
    }
}
