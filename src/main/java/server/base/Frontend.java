package server.base;

public interface Frontend {
    public void setId(String sessionId, Long userId);

    void makeDesigionAboutPersonInRoom(Long idForSocket, String roomName, boolean isConnectionToRoomAllowed);
}
