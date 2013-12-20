package server.message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;
import server.Address;
import server.service.AccountServiceImpl;
import server.service.FrontendImpl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MessageSystemTest {

    private AccountServiceImpl accountService;
    private AccountServiceImpl spyAccountService;
    private FrontendImpl frontend;
    private FrontendImpl spyFrontend;
    private MessageSystem messageSystem;

    private Long id;
    private String sessionId;

    @Before
    public void setUp() {
        messageSystem = new MessageSystem();

        frontend = new FrontendImpl(messageSystem);
        spyFrontend = spy(frontend);

        accountService = new AccountServiceImpl(messageSystem);
        spyAccountService = spy(accountService);
        id = (long) 2;

        sessionId = "df45e";

//        (new Thread(spyFrontend)).start();
//        (new Thread(spyAccountService)).start();
    }

    @Test
    public void testingMessageSystem() {
//        Address fAddress = new Address();
//        Address aAddress = new Address();
//        when(spyFrontend.getAddress()).thenReturn(fAddress);
//        when(spyAccountService.getAddress()).thenReturn(aAddress);

        Address frontendAddress = spyFrontend.getAddress();
        Address accountServiceAddress = spyAccountService.getAddress();

        String login = "hui";
        String password = "lol";
        // why not)


//        when(spyAccountService.getUserId(login, password)).thenReturn(id);
        doReturn(id).when(spyAccountService).getUserId(login, password);

        messageSystem.sendMessage(new MsgGetUserId(frontendAddress, accountServiceAddress, login, password, sessionId));
        messageSystem.execForSubscriber(spyAccountService);


        ArgumentCaptor<String> captorForSessionId = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> captorForId = ArgumentCaptor.forClass(Long.class);

        messageSystem.execForSubscriber(spyFrontend);
        verify(spyFrontend).setId(captorForSessionId.capture(), captorForId.capture());


        assertEquals(captorForSessionId.getValue(), sessionId);
        assertEquals(captorForId.getValue(), id);

    }
}
