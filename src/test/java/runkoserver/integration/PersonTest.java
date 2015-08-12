package runkoserver.integration;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
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
import runkoserver.domain.SimpleContent;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;
import runkoserver.repository.PersonRepository;
import runkoserver.service.ContentAreaService;
import runkoserver.service.PersonService;

/**
 * Integration tests for person-usage.
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
    SimpleContent simpleContent;

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
        
        simpleContent = contentAreaService.createSimpleContent("content", "a lot of text", areas, user);
        contentAreaService.saveContent(simpleContent);
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
    public void subscribedContentIsShownAtContentManager() {
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
    public void unsubscribedContentIsNotShownAtContentManager() {
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
}
