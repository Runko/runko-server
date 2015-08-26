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
import runkoserver.domain.Area;
import runkoserver.domain.Content;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;
import runkoserver.service.AreaService;
import runkoserver.service.ElementService;

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
    private AreaService areaService;
    
    @Autowired
    private ElementService elementService;
    
    @Before
    public void userIsLoggedIn() {
        driver.get(LINK_LOCALHOST + LINK_LOGIN);

        WebElement username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        WebElement password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));

        username.sendKeys(LOGIN_TEST);
        password.sendKeys(PASSWORD_TEST);
        password.submit();
    }

    private Content createNewContent(String contentName, String tArea) {
        driver.get(LINK_LOCALHOST + LINK_CONTENT + LINK_CONTENT_FORM);

        WebElement name = driver.findElement(By.name(ATTRIBUTE_NAME));
        WebElement textArea = driver.findElement(By.name(ATTRIBUTE_TEXTAREA));

        String theName = contentName;
        name.sendKeys(theName);
        String text = tArea;
        textArea.sendKeys(text);
        textArea.submit();

        return (Content)elementService.findElementByName(theName);
    }

    private Area createNewArea(String areaName){
        driver.get(LINK_LOCALHOST + LINK_CONTENT + LINK_AREA_FORM);
        
        WebElement name = driver.findElement(By.name(ATTRIBUTE_NAME));

        name.sendKeys(areaName);
        name.submit();
        
        return areaService.findAreaByName(areaName);
    }

    private String getViewContent(Content content) {
        return LINK_LOCALHOST + LINK_CONTENT + "/" + content.getId();
    }

    @Test
    public void contentCannotBeCreatedWithInvalidInformation() {
        String theName = "ba";
        createNewContent(theName, "");

        assertFalse(driver.getPageSource().contains(MESSAGE_CONTENT_SAVE_SUCCESS));
    }

    @Test
    public void contentCanBeCreatedWithValidInformation() {
        String theName = "banjana";
        String text = "is jellow";
        createNewContent(theName, text);

        assertTrue(driver.getPageSource().contains(MESSAGE_CONTENT_SAVE_SUCCESS));
    }

    @Test
    public void createdContentCanBeFound() {
        String theName = "mandoliini soi";
        String text = "ei itkeä saa";

        Content content = createNewContent(theName, text);

        assertTrue(content != null);
    }

    @Test
    public void createdContentContainsAllGivenInformation() {
        String name = "timo viheltää";
        String text = "en tunnista sävelmää";

        Content content = createNewContent(name, text);

        driver.get(getViewContent(content));

        assertTrue(driver.getPageSource().contains(name) && driver.getPageSource().contains(text));
    }

    @Test
    public void contentOwnerCanDeleteContent() {
        String name = "omistaja poistaa";
        String text = "tulee olematon";

        Content content = createNewContent(name, text);

        driver.get(getViewContent(content));

        WebElement deleteButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_CONTENT_DELETE));
        deleteButton.click();

        driver.get(getViewContent(content));

        assertFalse(driver.getPageSource().contains(name));
    }

    @Test
    public void contentCannotBeDeletedByOtherUser() {
        String name = "älä edes yritä!";
        String text = "Yritit kuitenkin";

        Content content = createNewContent(name, text);

        driver.get(LINK_LOCALHOST + LINK_LOGIN);

        WebElement username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        WebElement password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));

        username.sendKeys(LOGIN_TEST2);
        password.sendKeys(PASSWORD_TEST2);
        password.submit();

        driver.get(getViewContent(content));

        assertFalse(driver.getPageSource().contains(ATTRIBUTE_BUTTON_CONTENT_DELETE));
    }

    @Test
    public void contentCanBeEdited() {
        String name = "kaka";
        String text = "on ruskeaa";

        Content content = createNewContent(name, text);

        driver.get(getViewContent(content));

        WebElement editButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_CONTENT_EDIT));
        editButton.click();

        WebElement nameField = driver.findElement(By.name(ATTRIBUTE_NAME));
        WebElement textField = driver.findElement(By.name(ATTRIBUTE_TEXTAREA));

        name = "Kakka";
        nameField.clear();
        nameField.sendKeys(name);
        text = "On Ruskeaa.";
        textField.clear();
        textField.sendKeys(text);

        editButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_CONTENT_EDIT));
        editButton.click();
        driver.get(getViewContent(content));

        assertTrue(driver.getPageSource().contains(name) && driver.getPageSource().contains(text));
    }

    @Test
    public void contentCannotBeEditedByOtherUser() {
        String name = "ääälä edes yritä!";
        String text = "Yyyyritit kuitenkin";

        Content content = createNewContent(name, text);

        driver.get(LINK_LOCALHOST + LINK_LOGIN);

        WebElement username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        WebElement password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));

        username.sendKeys(LOGIN_TEST2);
        password.sendKeys(PASSWORD_TEST2);
        password.submit();

        driver.get(getViewContent(content));

        assertFalse(driver.getPageSource().contains(ATTRIBUTE_BUTTON_CONTENT_EDIT));
    }

@Test
    public void contentHasBookmarkButtonWhenNotBookmarked() {
        String name = "Elder Scrolls";
        String text = "playing time";
        Content content = createNewContent(name, text);
        
        driver.get(LINK_LOCALHOST + LINK_CONTENT + "/" + content.getId());
        
        assertEquals(ATTRIBUTE_BUTTON_BOOKMARK, driver.findElement(By.name(ATTRIBUTE_BUTTON_BOOKMARK)).getAttribute("name"));
    }
    
    @Test
    public void contentHasUnbookmarkButtonWhenBookmarked() {
        String name = "Praise the Sun!";
        String text = "YES, MY LORD";
        Content content = createNewContent(name, text);
        
        driver.get(LINK_LOCALHOST + LINK_CONTENT + "/" + content.getId());
        
        WebElement subscribe = driver.findElement(By.name(ATTRIBUTE_BUTTON_BOOKMARK));
        subscribe.click();
        
        assertEquals(ATTRIBUTE_BUTTON_UNBOOKMARK, driver.findElement(By.name(ATTRIBUTE_BUTTON_UNBOOKMARK)).getAttribute("name"));
    }
}
