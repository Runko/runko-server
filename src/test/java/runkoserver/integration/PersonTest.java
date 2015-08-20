package runkoserver.integration;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
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
import runkoserver.domain.Person;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;
import runkoserver.repository.PersonRepository;
import runkoserver.service.ContentAreaService;
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
    private ContentAreaService contentAreaService;

    Person user;
    Content simpleContent;

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
        Area area = contentAreaService.createArea("area", user, Boolean.TRUE);
        contentAreaService.saveArea(area);
        areas.add(area.getId());
        
        simpleContent = contentAreaService.createContent("content", "a lot of text", areas, user);
        contentAreaService.saveElement(simpleContent);
    }
    
    private Content createNewSimpleContent(String contentName, String tArea) {
        driver.get(LINK_LOCALHOST + LINK_CONTENT + LINK_CONTENT_FORM);

        WebElement name = driver.findElement(By.name(ATTRIBUTE_NAME));
        WebElement textArea = driver.findElement(By.name(ATTRIBUTE_TEXTAREA));

        String theName = contentName;
        name.sendKeys(theName);
        String text = tArea;
        textArea.sendKeys(text);
        textArea.submit();

        return (Content) contentAreaService.findElementByName(theName);
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

        Content content = createNewSimpleContent(name, text);

        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);

        assertTrue(driver.getPageSource().contains(name));
    }
    
    @Test
    public void otherPersonsContentDoesNotShowUpInContentManager() {
        String name = "I used to be adventurer like you";
        String text = "Until I took arrow to my knee";
        
        Content content = createNewSimpleContent(name, text);
        
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
        contentAreaService.deleteAll();
        
        String name = "Dark Soul's most common screen";
        String text = "YOU DIED";
        
        Content content = createNewSimpleContent(name, text);
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        
        WebElement delete = driver.findElement(By.name(ATTRIBUTE_BUTTON_DELETE));
        delete.click();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        
        assertFalse(driver.getPageSource().contains(name));
    }
    
    private Area createArea(String name) {
        driver.get(LINK_LOCALHOST + LINK_AREA_INDEX + LINK_AREA_FORM);
        
        WebElement nameField = driver.findElement(By.name(ATTRIBUTE_NAME));
        nameField.sendKeys(name);
        nameField.submit();
        
        return contentAreaService.findAreaByName(name);
    }
    
    @Test
    public void subscribedAreaIsShownAtContentManager() {
        String name = "Futurama references";
        
        Area area = createArea(name);
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
        Area area = createArea(name);
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
        
        assertFalse(driver.getPageSource().contains(name));
    }
    
    @Test
    public void allSubscribedAreasAreShownAtContentManager() {
        String name1 = "Epic Rap Battles of History";
        Area area1 = createArea(name1);
        driver.get(LINK_LOCALHOST + LINK_AREA_INDEX + "/" + area1.getId());
        WebElement subscribeButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_SUBSCRIBE));
        subscribeButton.click();
        
        String name2 = "Chuck Norris";
        Area area2 = createArea(name2);
        driver.get(LINK_LOCALHOST + LINK_AREA_INDEX + "/" + area2.getId());
        subscribeButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_SUBSCRIBE));
        subscribeButton.click();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        
        assertTrue(driver.getPageSource().contains(name1) 
                && driver.getPageSource().contains(name2));
    }
    
    @Test
    public void allSubscribedAreasAreShownOnceAtContentManager() {
        String name1 = "Perjantai-rage";
        Area area1 = createArea(name1);
        driver.get(LINK_LOCALHOST + LINK_AREA_INDEX + "/" + area1.getId());
        WebElement subscribeButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_SUBSCRIBE));
        subscribeButton.click();
        
        String name2 = "Matti Luukkainen";
        Area area2 = createArea(name2);
        driver.get(LINK_LOCALHOST + LINK_AREA_INDEX + "/" + area2.getId());
        subscribeButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_SUBSCRIBE));
        subscribeButton.click();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        
        List<WebElement> area1Elements = driver.findElements(By.id(name1));
        List<WebElement> area2Elements = driver.findElements(By.id(name2));
        
        assertEquals(1, area1Elements.size());
        assertEquals(1, area2Elements.size());
    }
    
    
    @Test
    public void bookmarkedContentIsShownAtContentManager() {
        String name = "Hello space!";
        String text = "Jihaa";
        
        Content content = createNewSimpleContent(name, text);
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        WebElement contentLink = driver.findElement(By.name(name));
        contentLink.click();
        
        WebElement bookmarkButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_BOOKMARK));
        bookmarkButton.click();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_BOOKMARK);
        
        assertTrue(driver.getPageSource().contains(name));
    }
    /*
    @Test
    public void unbookmarkedContentIsNotShownAtContentManager() {
        String name = "Back to the future";
        String text = "NOOOOOOOOOOOOOOOOOOOOOO!";
        
        Content content = createNewSimpleContent(name, text);
        driver.get(LINK_LOCALHOST);
        WebElement contentLink = driver.findElement(By.name(name));
        contentLink.click();
        
        WebElement bookmarkButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_BOOKMARK));
        bookmarkButton.click();
        
        driver.get(LINK_LOCALHOST);
        contentLink = driver.findElement(By.name(name));
        contentLink.click();
        
        WebElement unbookmarkButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_UNBOOKMARK));
        unbookmarkButton.click();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        
        assertFalse(driver.getPageSource().contains(name));
    }
    
    @Test
    public void allBookmarkedContentsAreShownAtContentManager() {
        String name1 = "Larping is everything";
        String text1 = "You are right";
        
        Content content1 = createNewSimpleContent(name1, text1);
        driver.get(LINK_LOCALHOST + LINK_CONTENT + "/" + content1.getId());
        WebElement bookmarkButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_BOOKMARK));
        bookmarkButton.click();
        
        String name2 = "Kerola";
        String text2 = "is HEAD";
        
        Content content2 = createNewSimpleContent(name2, text2);
        driver.get(LINK_LOCALHOST + LINK_CONTENT + "/" + content2.getId());
        bookmarkButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_BOOKMARK));
        bookmarkButton.click();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        
        assertTrue(driver.getPageSource().contains(name1) 
                && driver.getPageSource().contains(name2));
    }
    
    @Test
    public void allBookmarkedContentsAreShownOnceAtContentManager() {
        String name1 = "Perjantai-rage";
        String text1 = "Im first!";
        
        Content content1 = createNewSimpleContent(name1, text1);
        driver.get(LINK_LOCALHOST + LINK_CONTENT + "/" + content1.getId());
        WebElement bookmarkButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_BOOKMARK));
        bookmarkButton.click();
        
        String name2 = "Matti Luukkainen";
        String text2 = "is behing youuuuu....";
        
        Content content2 = createNewSimpleContent(name2, text2);
        driver.get(LINK_LOCALHOST + LINK_CONTENT + "/" + content2.getId());
        bookmarkButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_BOOKMARK));
        bookmarkButton.click();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        
        List<WebElement> conten1Elements = driver.findElements(By.id(name1));
        List<WebElement> conten2Elements = driver.findElements(By.id(name2));
        
        assertEquals(1, conten1Elements.size());
        assertEquals(1, conten2Elements.size());
    }
    */
}
