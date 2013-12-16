package server.message;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import server.Address;
import server.TimeHelper;
import server.service.AccountServiceImpl;
import server.service.FrontendImpl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class MessageSystemTest {

    private AccountServiceImpl accountServiceImpl;
    private AccountServiceImpl spyAccountServiceImpl;
    private FrontendImpl frontendImpl;
    private FrontendImpl spyFrontendImpl;
    private MessageSystem messageSystem;

    private Long id;
    private String sessionId;

    @Before
    public void setUp() {
        messageSystem = new MessageSystem();

//        frontend = new Frontend(messageSystem);
//        spyFrontend = spy(frontend);

        accountServiceImpl = new AccountServiceImpl(messageSystem);
        spyAccountServiceImpl = spy(accountServiceImpl);
        id = (long) 2;

        sessionId = "df45e";

//        (new Thread(spyFrontend)).start();
        (new Thread(spyAccountServiceImpl)).start();
    }

    @Test
    public void testingMessageSystem() {
        Address fAddress = new Address();
        Address aAddress = new Address();
//        when(spyFrontend.getAddress()).thenReturn(fAddress);
        when(spyAccountServiceImpl.getAddress()).thenReturn(aAddress);

//        Address frontendAddress = spyFrontend.getAddress();
        Address accountServiceAddress = spyAccountServiceImpl.getAddress();

        String login = "python";
        String password = "java";
        // why not)


        when(spyAccountServiceImpl.getUserId("python", "java")).thenReturn(id);

//        messageSystem.sendMessage(new MsgGetUserId(frontendAddress, accountServiceAddress, login, password, sessionId));
        TimeHelper.sleep(6000);

        ArgumentCaptor<String> captorForSessionId = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> captorForId = ArgumentCaptor.forClass(Long.class);

//        verify(spyFrontend).setId(captorForSessionId.capture(), captorForId.capture());
        assertEquals(captorForSessionId.getValue(), sessionId);
        assertEquals(captorForId.getValue(), id);

    }
}
