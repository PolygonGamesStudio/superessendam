package server.base;

public interface Frontend {
    public void setId(String sessionId, Long userId);
    void closeSocket(Long idForSocket);
}
