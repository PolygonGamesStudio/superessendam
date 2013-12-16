package server.account_service;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.service.AccountServiceImpl;

import static org.mockito.Mockito.when;

public class ASTest extends TestCase {

    @Mock
    AccountServiceImpl accountServiceImpl;

    @Before
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAccountServiceGet() {
        when(accountServiceImpl.getUserId("user0", "pass0")).thenReturn((long) 0);
    }
}
