package runkoserver.integration;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
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

/**
 * Integration tests for content-usage.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(value = "server.port=9000")
@SeleniumTest(baseUrl = "http://localhost:9000")
public class ContentTest {
    @Autowired
    private WebDriver driver;
    
    @Before
    public void userIsLoggedIn() {
        driver.get(LINK_LOCALHOST + LINK_LOGIN);

        WebElement username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        WebElement password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));

        username.sendKeys(LOGIN_TEST);
        password.sendKeys(PASSWORD_TEST);
        password.submit();
    }
    
    @Test
    public void simpleContentCanBeCreatedWithValidInformation() {
        driver.get(LINK_LOCALHOST + LINK_CONTENT + LINK_CONTENT_SIMPLEFORM);
        
        WebElement name = driver.findElement(By.name(ATTRIBUTE_NAME));
        WebElement textArea = driver.findElement(By.name(ATTRIBUTE_TEXTAREA));
        
        String theName = "banjana";
        name.sendKeys(theName);
        textArea.sendKeys("is jellow");
        textArea.submit();
        
        driver.get(LINK_LOCALHOST + LINK_CONTENT + "/1");
        assertTrue(driver.getPageSource().contains(theName));
    }
}
