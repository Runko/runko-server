package runkoserver.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import runkoserver.Application;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.MESSAGE_LOGIN_DEFAULT;
import runkoserver.service.PersonService;

/**
 * Tests for basic application-interactions.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(value = "server.port=9000")
@SeleniumTest(baseUrl = "http://localhost:9000")
public class LoginTest {

    @Autowired
    WebDriver driver;

    @Autowired
    PersonService personService;

    @Test
    public void UserCanAccessHomePage() {
        driver.get(LINK_LOCALHOST);

        assertTrue(driver.getPageSource().contains(TITLE_STANDARD));
    }

    @Test
    public void UserCannotLogInWithWrongPassword() {
        driver.get(LINK_LOCALHOST + LINK_LOGIN);

        WebElement username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        WebElement password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));

        username.sendKeys(LOGIN_TEST);
        password.sendKeys("huehue");
        password.submit();

        assertTrue(driver.getPageSource().contains(MESSAGE_LOGIN_DEFAULT));
    }

    @Test
    public void UserCanLogInWithCorrectCredentials() {
        driver.get(LINK_LOCALHOST + LINK_LOGIN);

        WebElement username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        WebElement password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));

        username.sendKeys(LOGIN_TEST);
        password.sendKeys(PASSWORD_TEST);
        password.submit();

        assertTrue(driver.getPageSource().contains(TITLE_FRONTPAGE));
    }

    @Test
    public void UserCanLogOutIfLoggedIn() {
        driver.get(LINK_LOCALHOST + LINK_LOGIN);

        WebElement username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        WebElement password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));

        username.sendKeys(LOGIN_TEST);
        password.sendKeys(PASSWORD_TEST);
        password.submit();

        WebElement logoutButton = driver.findElement(By.name("logIn"));
        logoutButton.click();

        assertTrue(driver.getPageSource().contains("Kirjaudu sisään"));
    }
}
