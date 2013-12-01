package server.message;

import server.Address;
import server.service.AccountService;

public class MsgGetUserId extends MsgToAS {
    private String name;
    private String password;
    private String sessionId;

    public MsgGetUserId(Address from, Address to, String name, String password, String sessionId) {
        super(from, to);
        this.name = name;
        this.password = password;
        this.sessionId = sessionId;
    }


    void exec(AccountService accountService) {
        Long id = accountService.getUserId(name, password);
        accountService.getMessageSystem().sendMessage(new MsgUpdateUserId(getTo(), getFrom(), sessionId, id));
    }
}
