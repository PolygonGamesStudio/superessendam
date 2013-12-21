package server.base;


import org.json.JSONObject;
import server.message.MessageSystem;

public interface GameMechanics {

    public String handleEvent(String stuff, Long userId);

    public MessageSystem getMessageSystem();

    boolean userAdd(String room, Long userId);

    public JSONObject getRooms();
    
}
