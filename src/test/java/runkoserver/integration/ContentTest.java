package runkoserver.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import runkoserver.domain.Content;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;
import runkoserver.service.ContentAreaService;

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
    
    @Autowired
    private ContentAreaService contentAreaService;
    
    @Before
    public void userIsLoggedIn() {
        driver.get(LINK_LOCALHOST + LINK_LOGIN);

        WebElement username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        WebElement password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));

        username.sendKeys(LOGIN_TEST);
        password.sendKeys(PASSWORD_TEST);
        password.submit();
    }
    
    private Content createNewSimpleContent(String contentName, String tArea) {
        driver.get(LINK_LOCALHOST + LINK_CONTENT + LINK_CONTENT_SIMPLEFORM);
        
        WebElement name = driver.findElement(By.name(ATTRIBUTE_NAME));
        WebElement textArea = driver.findElement(By.name(ATTRIBUTE_TEXTAREA));
        
        String theName = contentName;
        name.sendKeys(theName);
        String text = tArea;
        textArea.sendKeys(text);
        textArea.submit();
        
        return contentAreaService.findContentByName(theName);
    }
    
    private String getViewContent(Content content) {
        return LINK_LOCALHOST + LINK_CONTENT + "/" + content.getId();
    }
    
    @Test
    public void contentCannotBeCreatedWithInvalidInformation() {
        String theName = "ba";
        createNewSimpleContent(theName, "");
        
        assertFalse(driver.getPageSource().contains(MESSAGE_CONTENT_SAVE_SUCCESS));
    }
    
    @Test
    public void simpleContentCanBeCreatedWithValidInformation() {
        String theName = "banjana";
        String text = "is jellow";
        createNewSimpleContent(theName, text);
        
        assertTrue(driver.getPageSource().contains(MESSAGE_CONTENT_SAVE_SUCCESS));
    }
    
    @Test
    public void createdContentCanBeFound() {
        String theName = "mandoliini soi";        
        String text = "ei itkeä saa";        
        
        Content content = createNewSimpleContent(theName, text);
        
        assertTrue(content != null);
    }
    
    @Test
    public void createdContentContainsAllGivenInformation() {
        String name = "timo viheltää";
        String text = "en tunnista sävelmää";
        
        Content content = createNewSimpleContent(name, text);
        
        driver.get(getViewContent(content));
        
        assertTrue(driver.getPageSource().contains(name) && driver.getPageSource().contains(text));
    }
    
    @Test
    public void contentOwnerCanDeleteContent() {
        String name = "omistaja poistaa";
        String text = "tulee olematon";
        
        Content content = createNewSimpleContent(name, text);
        
        driver.get(getViewContent(content));
        
        WebElement delete = driver.findElement(By.name("remove"));
        delete.click();
        
        driver.get(getViewContent(content));
        
        assertFalse(driver.getPageSource().contains(name));
    }
    
    @Test
    public void contentCannotBeDeletedByOtherUser() {
        String name = "älä edes yritä!";
        String text = "Yritit kuitenkin";
        
        Content content = createNewSimpleContent(name, text);
        
        driver.get(LINK_LOCALHOST + LINK_LOGIN);
        
        WebElement username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        WebElement password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));
        
        username.sendKeys(LOGIN_TEST2);
        password.sendKeys(PASSWORD_TEST2);
        password.submit();
        
        driver.get(getViewContent(content));
        
        assertFalse(driver.getPageSource().contains("remove"));
    }
}
