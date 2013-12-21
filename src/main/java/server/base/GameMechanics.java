package server.base;


import org.json.JSONObject;
import server.message.MessageSystem;

public interface GameMechanics {

    public void handleEvent(String stuff);

    public MessageSystem getMessageSystem();

    boolean userAdd(String room, Long userId);

    public JSONObject getRooms();
    
}
