package server.message;


import server.Address;
import server.base.AccountService;

public class MsgToPutUser extends MsgToAS {
    private String name;
    private String password;
    private String sessionId;

    public MsgToPutUser(Address from, Address to, String name, String password, String sessionId) {
        super(from, to);
        this.name = name;
        this.password = password;
        this.sessionId = sessionId;
    }


    void exec(AccountService accountService) {
        accountService.setUserId(name, password); // FIXME: за один или за два?
        Long id = accountService.getUserId(name, password);
        accountService.getMessageSystem().sendMessage(new MsgUpdateUserId(getTo(), getFrom(), sessionId, id));
//        здесь сообщение отправляется тому, от кого оно пришло
    }
}
