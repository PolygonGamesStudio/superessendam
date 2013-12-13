package behavioral;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class AuthorizationTest {
    private static final String BASE_URL = "http://localhost:8080";
    private static final String AUTH_URL = "http://localhost:8080/auth";
    private static final String USER_ID_URL = "http://localhost:8080/user_id";

    WebDriver driver;

    @Before
    public void setUp() {
        System.out.println("before");
        driver = new FirefoxDriver();
        driver.get("http://www.google.com");
    }

    @After
    public void cleanUp() throws Exception {
        System.out.println("after");
        driver.quit();
    }

    @Test
    public void test_authorization() throws Exception {
//        assert  driver.getCurrentUrl().equals(BASE_URL);
//
//        WebElement authLink = driver.findElement(By.id("auth"));
//        authLink.click();
//
//        WebElement loginInput = driver.findElement(By.id("login"));
//        WebElement passwordInput = driver.findElement(By.id("password"));
//        WebElement submitButton = driver.findElement(By.id("submitLogin"));
//        WebElement serverMessage = driver.findElement(By.id("message"));
//
//        assert driver.getCurrentUrl().equals("http://localhost:8080/auth");
//
//        assert serverMessage.getText().equals("User session: New session. Hello!");
//
//        loginInput.sendKeys("user0");
//        passwordInput.sendKeys("pass0");
//        submitButton.click();
//        driver.findElement(By.name("q")).sendKeys("Selenium 2.0 WebDriver");
//        driver.findElement(By.name("q")).submit();
//        System.out.println("Page title is: " + driver.getTitle());
        System.out.println("test");
    }
}
