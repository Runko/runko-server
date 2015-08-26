package runkoserver.integration;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
import runkoserver.domain.Person;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;
import runkoserver.repository.PersonRepository;
import runkoserver.service.AreaService;
import runkoserver.service.ElementService;
import runkoserver.service.PersonService;

/**
 * Integration tests for person-usage. Contains also Content-manager tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(value = "server.port=9000")
@SeleniumTest(baseUrl = "http://localhost:9000")
public class PersonTest {

    @Autowired
    private WebDriver driver;

    @Autowired
    private PersonService personService;

    @Autowired
    private AreaService areaService;
    
    @Autowired
    private ElementService elementService;

    Person user;
    Content simpleContent;

    
    private static int n = 0;
    
    @Before
    public void userIsLoggedIn() {
        driver.get(LINK_LOCALHOST + LINK_LOGIN);

        WebElement username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        WebElement password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));

        username.sendKeys(LOGIN_TEST);
        password.sendKeys(PASSWORD_TEST);
        password.submit();

        user = personService.findByUsername(ATTRIBUTE_USERNAME);
    }
    
    @Before
    public void createAreaAndContent(){
        List<Long> areas = new ArrayList<>();
        Area area = areaService.createArea("area" + n++, user, Boolean.TRUE);
        areaService.saveArea(area);
        areas.add(area.getId());
        
        simpleContent = elementService.createContent("content", "a lot of text", areas, user);
        elementService.saveElement(simpleContent);
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

        return (Content) elementService.findElementByName(theName);
    }
    
    private Area createNewArea(String name) {
        driver.get(LINK_LOCALHOST + LINK_AREA + LINK_AREA_FORM);
        
        WebElement nameField = driver.findElement(By.name(ATTRIBUTE_NAME));
        nameField.sendKeys(name);
        nameField.submit();
        
        return areaService.findAreaByName(name);
    }

    @Test
    public void userCanSeeOwnProfile() {
        driver.get(LINK_LOCALHOST + LINK_PERSONS +  LINK_PROFILE);

        assertTrue(driver.getPageSource().contains("testi"));
    }

    @Test
    public void userCanSeeOwnContents() {
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_PROFILE);
        
        assertTrue(driver.getPageSource().contains(simpleContent.getName()));
    }

    @Test
    public void userCanOpenProfileEditor() {
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_PROFILE );
        WebElement element = driver.findElement(By.id("submit"));
        element.submit();
        
        assertTrue(driver.getPageSource().contains("Muokkaa profiilia"));
    }

    @Test
    public void userCanEditOwnProfile() {
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_PROFILE );
        WebElement element = driver.findElement(By.id("submit"));
        element.submit();
        
        WebElement description = driver.findElement(By.name("description"));
        description.sendKeys("Tämä tieto on muuttunut");
        
        element = driver.findElement(By.id("submit"));
        element.submit();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_PROFILE );
        
        assertTrue(driver.getPageSource().contains("Tämä tieto on muuttunut"));
    }

    @Test
    public void ownedContentShowsUpInContentManager() {
        String name = "Meitsin sisältö";
        String text = "Tekstiä";

        Content content = createNewContent(name, text);

        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);

        assertTrue(driver.getPageSource().contains(name));
    }
    
    @Test
    public void ownedAreaShowsUpInContentManager() {
        String name = "Meitsin sisältö";  

        Area area = createNewArea(name);

        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);

        assertEquals(name, driver.findElement(By.name(name)).getAttribute("name"));
    }
    
    @Test
    public void otherPersonsContentDoesNotShowUpInContentManager() {
        String name = "I used to be adventurer like you";
        String text = "Until I took arrow to my knee";
        
        Content content = createNewContent(name, text);
        
        driver.get(LINK_LOCALHOST + LINK_LOGIN_LOGOUT);
        
        WebElement username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        WebElement password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));

        username.sendKeys(LOGIN_TEST2);
        password.sendKeys(PASSWORD_TEST2);
        password.submit();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        
        assertFalse(driver.getPageSource().contains(name));
    }
    
    @Test
    public void deletedContentIsRemovedFromContentManager() {
        elementService.deleteAllElements();
        areaService.deleteAllAreas();
        
        String name = "Dark Soul's most common screen";
        String text = "YOU DIED";
        
        Content content = createNewContent(name, text);
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        
        WebElement delete = driver.findElement(By.name(ATTRIBUTE_BUTTON_CONTENT_DELETE));
        delete.click();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        
        assertFalse(driver.getPageSource().contains(name));
    }
    
    @Test
    public void subscribedAreaIsShownAtContentManager() {
        String name = "Futurama references";
        
        Area area = createNewArea(name);
        driver.get(LINK_LOCALHOST);
        WebElement areaLink = driver.findElement(By.name(name));
        areaLink.click();
        
        WebElement subscribeButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_SUBSCRIBE));
        subscribeButton.click();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        
        assertTrue(driver.getPageSource().contains(name));
    }
    
    @Test
    public void unsubscribedAreaIsNotShownAtContentManager() {
        String name = "IMMA FIRIN MAH'";
        Area area = createNewArea(name);
        driver.get(LINK_LOCALHOST);
        WebElement areaLink = driver.findElement(By.name(name));
        areaLink.click();
        
        WebElement subscribeButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_SUBSCRIBE));
        subscribeButton.click();
        
        driver.get(LINK_LOCALHOST);
        areaLink = driver.findElement(By.name(name));
        areaLink.click();
        
        WebElement unsubscribeButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_UNSUBSCRIBE));
        unsubscribeButton.click();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
 
        assertNotEquals(name, driver.findElement(By.name(name)).getAttribute("id"));
    }
    
    @Test
    public void allSubscribedAreasAreShownAtContentManager() {
        String name1 = "Epic Rap Battles of History";
        Area area1 = createNewArea(name1);
        driver.get(LINK_LOCALHOST + LINK_AREA + "/" + area1.getId());
        WebElement subscribeButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_SUBSCRIBE));
        subscribeButton.click();
        
        String name2 = "Chuck Norris";
        Area area2 = createNewArea(name2);
        driver.get(LINK_LOCALHOST + LINK_AREA + "/" + area2.getId());
        subscribeButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_SUBSCRIBE));
        subscribeButton.click();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        
        assertEquals(name1, driver.findElement(By.id(name1)).getAttribute("id"));
        assertEquals(name2, driver.findElement(By.id(name2)).getAttribute("id"));
    }
    
    @Test
    public void allSubscribedAreasAreShownOnceAtContentManager() {
        String name1 = "Perjantai-rage";
        Area area1 = createNewArea(name1);
        driver.get(LINK_LOCALHOST + LINK_AREA + "/" + area1.getId());
        WebElement subscribeButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_SUBSCRIBE));
        subscribeButton.click();
        
        String name2 = "Matti Luukkainen";
        Area area2 = createNewArea(name2);
        driver.get(LINK_LOCALHOST + LINK_AREA + "/" + area2.getId());
        subscribeButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_SUBSCRIBE));
        subscribeButton.click();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        
        List<WebElement> area1Elements = driver.findElements(By.id(name1));
        List<WebElement> area2Elements = driver.findElements(By.id(name2));
        
        assertEquals(1, area1Elements.size());
        assertEquals(1, area2Elements.size());
    }
    
    
    @Test
    public void bookmarkedContentIsShownAtBookmarks() {
        String name = "Hello space!";
        String text = "Jihaa";
        
        Content content = createNewContent(name, text);
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        WebElement contentLink = driver.findElement(By.name(name));
        contentLink.click();
        
        WebElement bookmarkButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_BOOKMARK));
        bookmarkButton.click();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_BOOKMARK);
        
        assertTrue(driver.getPageSource().contains(name));
    }
    
    @Test
    public void unbookmarkedContentIsNotShownAtBookmarks() {
        String name = "Back to the future";
        String text = "NOOOOOOOOOOOOOOOOOOOOOO!";
        
        Content content = createNewContent(name, text);
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        WebElement contentLink = driver.findElement(By.name(name));
        contentLink.click();
        
        WebElement bookmarkButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_BOOKMARK));
        bookmarkButton.click();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        contentLink = driver.findElement(By.name(name));
        contentLink.click();
        
        WebElement unbookmarkButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_UNBOOKMARK));
        unbookmarkButton.click();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_BOOKMARK);
        
        assertFalse(driver.getPageSource().contains(name));
    }

}
