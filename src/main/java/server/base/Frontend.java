package server.base;

public interface Frontend {
    public void setId(String sessionId, Long userId);

    void makeDecisionAboutPersonInRoom(Long idForSocket, String roomName, boolean isConnectionToRoomAllowed);
}
