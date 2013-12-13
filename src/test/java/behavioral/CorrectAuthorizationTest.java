package behavioral;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import server.TimeHelper;


public class CorrectAuthorizationTest {
    private static final String BASE_URL = "http://localhost:8080/";
    private static final String AUTH_URL = "http://localhost:8080/auth";
    private static final String USER_ID_URL = "http://localhost:8080/userid";

    private WebDriver driver;

    @Before
    public void setUp() {
//        System.setProperty("webdriver.chrome.driver", "/home/flexo/bin/chromedriver");
//        driver = new ChromeDriver();

        driver = new FirefoxDriver();
    }

    @After
    public void cleanUp() throws Exception {
        driver.quit();
    }

    @Test
    public void test_authorization() throws Exception {
        driver.get(BASE_URL);
        assert driver.getCurrentUrl().equals(BASE_URL) : driver.getCurrentUrl();

        WebElement authLink = driver.findElement(By.id("auth"));
        authLink.click();
        assert driver.getCurrentUrl().equals(AUTH_URL) : driver.getCurrentUrl();

        WebElement loginInput = driver.findElement(By.id("login"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement submitButton = driver.findElement(By.id("submitLogin"));
        WebElement serverMessage = driver.findElement(By.id("message"));

        assert serverMessage.getText().equals("User session: New session. Hello!") : "Wrong message from server";
        TimeHelper.sleep(1000);

        loginInput.sendKeys("python");
        passwordInput.sendKeys("java");

        submitButton.click();

        serverMessage = driver.findElement(By.id("message"));
        assert serverMessage.getText().equals("User session: Waiting for authorization") : "Wrong message from server";
        TimeHelper.sleep(6000);

        driver.get(USER_ID_URL);
        assert driver.getCurrentUrl().equals(USER_ID_URL) : driver.getCurrentUrl();

        WebElement userState = driver.findElement(By.id("state"));
        assert userState.getText().equals("User state: name = python, id = 2");
        TimeHelper.sleep(3000);
        userState = driver.findElement(By.id("state"));
        assert userState.getText().equals("User state: name = python, id = 2");
    }
}
