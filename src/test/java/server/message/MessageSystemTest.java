package server.message;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.service.AccountService;
import server.service.Frontend;

public class MessageSystemTest {
    @Mock
    private AccountService accountService;
    @Mock
    private Frontend frontend;

    private MessageSystem messageSystem;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        messageSystem.addService(accountService);
        messageSystem.addService(frontend);
    }
}
