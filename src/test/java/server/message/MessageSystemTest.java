//package server.message;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.ArgumentCaptor;
//import server.Address;
//import server.service.AccountServiceImpl;
//import server.service.FrontendImpl;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.*;
//
//public class MessageSystemTest {
//
//    private AccountServiceImpl accountService;
//    private AccountServiceImpl spyAccountService;
//    private FrontendImpl frontend;
//    private FrontendImpl spyFrontend;
//    private MessageSystem messageSystem;
//
//    @Before
//    public void setUp() {
//        messageSystem = new MessageSystem();
//        frontend = new FrontendImpl(messageSystem);
//        spyFrontend = spy(frontend);
//
//        accountService = new AccountServiceImpl(messageSystem);
//        spyAccountService = spy(accountService);
//    }
//
//    @Test
//    public void testingMessageSystem() {
//        Address frontendAddress = spyFrontend.getAddress();
//        Address accountServiceAddress = spyAccountService.getAddress();
//
//        Long id = (long) 2;
//        String sessionId = "df45e";
//        String login = "superUser";
//        String password = "elParole";
//
//        doReturn(id).when(spyAccountService).getUserId(login, password);
//        ArgumentCaptor<String> captorForSessionId = ArgumentCaptor.forClass(String.class);
//        ArgumentCaptor<Long> captorForId = ArgumentCaptor.forClass(Long.class);
//
//        messageSystem.sendMessage(new MsgGetUserId(frontendAddress, accountServiceAddress, login, password, sessionId));
//        messageSystem.execForSubscriber(spyAccountService);
//        messageSystem.execForSubscriber(spyFrontend);
//        verify(spyFrontend).setId(captorForSessionId.capture(), captorForId.capture());
//
//        assertEquals(captorForSessionId.getValue(), sessionId);
//        assertEquals(captorForId.getValue(), id);
//    }
//}
