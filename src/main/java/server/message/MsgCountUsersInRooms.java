package server.message;


import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import server.Address;
import server.base.Frontend;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MsgCountUsersInRooms extends MsgToFrontend {
    private final JSONObject countUsersInRooms;

    public MsgCountUsersInRooms(Address from, Address to, JSONObject countUsersInRooms) {
        super(from, to);
        this.countUsersInRooms = countUsersInRooms;
    }

    @Override
    void exec(Frontend frontend) {
        Map map = new HashMap<String,String>();

        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(countUsersInRooms.toString(), HashMap.class);
            frontend.putDataForGameHall(map);
        } catch (IOException e) {
            System.out.println("problems with mapping from json");
        }
    }
}
