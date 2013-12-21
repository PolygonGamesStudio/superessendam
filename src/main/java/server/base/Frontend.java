package server.base;

import java.util.Map;

public interface Frontend {
    public void setId(String sessionId, Long userId);

    void makeDesigionAboutPersonInRoom(Long idForSocket, String roomName, boolean isConnectionToRoomAllowed);

    void putDataForGameHall(Map gamesMap);
}
