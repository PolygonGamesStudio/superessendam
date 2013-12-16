package server.base;


import server.message.MessageSystem;

public interface GameMechanics {

    public void handleEvent(String stuff);

    public MessageSystem getMessageSystem();
}
