package server.service;


import server.Address;
import server.base.AddressService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AddressServiceImpl implements AddressService {
    private Map<String, Address> addressByName; // TODO: Address in List

    public AddressServiceImpl() {
        this.addressByName = new ConcurrentHashMap<>(); // FIXME: ConcurrentHashMap or HashMap
    }

    public Address getAddressFE() {
        return addressByName.get("frontend");
    }

    public void setAddressFE(Address address) {
        addressByName.put("frontend", address);
    }

    public Address getAddressGM() {
        return addressByName.get("gameMechanics");
    }

    public void setAddressGM(Address address) {
        addressByName.put("gameMechanics", address);
    }

    public void setAddressAS(Address address) {
        addressByName.put("accountService", address);
    }

    public Address getAddressAS() {
        return addressByName.get("accountService");
    }
}
