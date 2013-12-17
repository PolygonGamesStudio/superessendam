package server.base;


import server.Address;

public interface AddressService {

    public Address getAddressFE();

    public void setAddressFE(Address address);

    public Address getAddressGM();

    public void setAddressGM(Address address);

    public void setAddressAS(Address address);

    public Address getAddressAS();
}
