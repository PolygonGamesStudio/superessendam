package server;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AddressService {
    private Map<String, Address> addressByName; // TODO: Address in List

    public AddressService(Map<String, Address> addressByName) {
        this.addressByName = new ConcurrentHashMap<>(); // FIXME: ConcurrentHashMap or HashMap
    }

    public Address getAddressFE() {
        return addressByName.get("frontend");
    }

    public void setAddressFE(Address address) {
        addressByName.put("frontend", address);
    }

    public Address getAddressGM() {
        return addressByName.get("gamemechanics");
    }

    public void setAddressGM(Address address) {
        addressByName.put("gamemechanics", address);
    }
}
