package server.message;

import org.json.JSONObject;
import server.Address;
import server.base.GameMechanics;

public class MsgGetRooms extends MsgToGM {

    public MsgGetRooms(Address from, Address to) {
        super(from, to);
    }

    @Override
    void exec(GameMechanics gameMechanics) {
        JSONObject countUsersRooms = gameMechanics.getRooms();
        gameMechanics.getMessageSystem().sendMessage(new MsgCountUsersInRooms(getTo(), getFrom(), countUsersRooms));
    }


}
