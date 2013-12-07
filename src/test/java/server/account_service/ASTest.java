package server.account_service;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import server.service.AccountService;

import java.lang.Exception;

import static org.mockito.Mockito.when;

public class ASTest extends TestCase {

    @Mock
    AccountService accountService;

    @Before
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAccountServiceGet() {
        when(accountService.getUserId("user0", "pass0")).thenReturn((long) 0);
    }
}
