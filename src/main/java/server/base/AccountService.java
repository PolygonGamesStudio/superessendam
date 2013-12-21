package server.base;


import server.message.MessageSystem;

public interface AccountService {
    public Long getUserId(String login, String password);
    public void setUserId(String login, String password);

    public MessageSystem getMessageSystem();
}
