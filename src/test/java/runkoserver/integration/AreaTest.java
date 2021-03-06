/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runkoserver.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
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
 * Integration tests for area-usage.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest(value = "server.port=9000")
@SeleniumTest(baseUrl = "http://localhost:9000")

public class AreaTest {
    
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
    
    private Area createNewArea(String areaName, String areaVisibility) {
        driver.get(LINK_LOCALHOST + LINK_AREA + LINK_AREA_FORM);
        
        WebElement name = driver.findElement(By.name(ATTRIBUTE_NAME));
        WebElement publicity = driver.findElement(By.id(areaVisibility));
                
        String theName = areaName;
        name.sendKeys(theName);
        publicity.click();
        publicity.submit();
        
        return areaService.findAreaByName(theName);
    }
    
    private Content createNewContent(String contentName, String tArea, Area area) {
        driver.get(LINK_LOCALHOST + LINK_CONTENT + LINK_CONTENT_FORM);
        
        WebElement name = driver.findElement(By.name(ATTRIBUTE_NAME));
        WebElement textArea = driver.findElement(By.name(ATTRIBUTE_TEXTAREA));
        WebElement areachoice = driver.findElement(By.id(String.valueOf(area.getId())));
        
        String theName = contentName;
        name.sendKeys(theName);
        String text = tArea;
        textArea.sendKeys(text);
        
        areachoice.click();
        driver.findElement(By.name("move")).click();
       
         driver.findElement(By.name("save")).click();
        return (Content) elementService.findElementByName(theName);
    }
    
    private String getViewArea(Area area) {
        return LINK_LOCALHOST + LINK_AREA + "/" + area.getId();
    }
    
    @Test
    public void areaCannotBeCreatedWithInvalidInformation() {
        String areaName = "ue";
        String visibility = "testing1";
        createNewArea(areaName, visibility);

        assertFalse(driver.getPageSource().contains(MESSAGE_AREA_SAVE_SUCCESS));
    }
    
    @Test
    public void areaCanBeCreatedWithValidInformation() {
        String areaName = "Orange is new black!";
        String visibility = "testing1";
        createNewArea(areaName, visibility);
        
        assertTrue(driver.getPageSource().contains(MESSAGE_AREA_SAVE_SUCCESS));
    }
    
    @Test
    public void createdAreaCanBeFound() {
        String areaName = "Elämä on!";        
        String visibility = "testing1";
        createNewArea(areaName, visibility);
        driver.get(LINK_LOCALHOST);
        assertTrue(driver.getPageSource().contains(areaName));
    }
    
    @Test
    public void createdAreaContainsAllGivenInformation() {
        String areaName = "Tämä on testi.";
        String visibility = "testing2";
        Area area = createNewArea(areaName, visibility);
        
        assertTrue(driver.getPageSource().contains(MESSAGE_AREA_SAVE_SUCCESS));
        driver.get(LINK_LOCALHOST);
        assertTrue(driver.getPageSource().contains("Julkinen"));
        driver.get(LINK_LOCALHOST + LINK_AREA + "/" + area.getId());
        assertTrue(driver.getPageSource().contains(areaName));

    }
    
    @Test
    public void contentCanBeCreatedInNewArea(){
        String areaName = "Uusi alue";
        String contentName = "Uusi sisältö";
        String contentText = "Lisätään sisältöä.";
        String visibility = "testing1";
        
        Area area = createNewArea(areaName, visibility);
        Content content = createNewContent(contentName, contentText, area);
        
        driver.get(LINK_LOCALHOST + LINK_AREA + "/" + area.getId());
        
        assertTrue(driver.getPageSource().contains(contentName));
    }
    
    @Test
    public void areaVisibilityCanChangeFromDefaultInForm(){
        String areaName = "Näkyvyys vaihtuu";
        String areaVisibility = "testing2";
       
        driver.get(LINK_LOCALHOST + LINK_AREA + LINK_AREA_FORM);
        
        WebElement name = driver.findElement(By.name(ATTRIBUTE_NAME));
        WebElement visibility = driver.findElement(By.id(areaVisibility));
        WebElement saveButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_SAVE));
        
        name.sendKeys(areaName);
        visibility.click();
        saveButton.click();
        
        assertTrue(driver.getPageSource().contains(MESSAGE_AREA_SAVE_SUCCESS) );
        driver.get(LINK_LOCALHOST);
        assertTrue(driver.getPageSource().contains("Kirjautuneille"));
    }
    
    @Test
    public void areaHasSubscriptionButtonWhenNotSubscribed() {
        String areaName = "Elder Scrolls";
        String visibility = "testing1";
        Area area = createNewArea(areaName, visibility);
        
        driver.get(LINK_LOCALHOST + LINK_AREA + "/" + area.getId());
        
        assertTrue(driver.getPageSource().contains(ATTRIBUTE_SUBSCRIPTION) &&
                !driver.getPageSource().contains(ATTRIBUTE_UNSUBSCRIPTION));
    }
    
    @Test
    public void areaHasUnsubscribeButtonWhenSubscribed() {
        String areaName = "Praise the Sun!";
        String visibility = "testing1";
        
        Area area = createNewArea(areaName, visibility);
        
        driver.get(LINK_LOCALHOST + LINK_AREA + "/" + area.getId());
        
        WebElement subscribe = driver.findElement(By.name(ATTRIBUTE_BUTTON_SUBSCRIBE));
        subscribe.click();
        
        assertTrue(!driver.getPageSource().contains(ATTRIBUTE_SUBSCRIPTION) &&
                driver.getPageSource().contains(ATTRIBUTE_UNSUBSCRIPTION));
    }
    
    @Test
    public void areaCanBeUnsubscribed() {
        String areaName = "Mansikkaa";
        String visibility = "testing1";
        
        Area area = createNewArea(areaName, visibility);
        
        driver.get(LINK_LOCALHOST + LINK_AREA + "/" + area.getId());
        
        WebElement subscribe = driver.findElement(By.name(ATTRIBUTE_BUTTON_SUBSCRIBE));
        subscribe.click();
        
        driver.get(LINK_LOCALHOST + LINK_AREA + "/" + area.getId());
        
        WebElement unsubscribe = driver.findElement(By.name(ATTRIBUTE_BUTTON_UNSUBSCRIBE));
        unsubscribe.click();
        
        assertTrue(driver.getPageSource().contains(MESSAGE_AREA_SUBSCRIPTION_STOP));
    }
   
    @Test
    public void areaOwnerCanDeleteNullArea() {
        elementService.deleteAllElements();
        areaService.deleteAllAreas();
        
        String areaName = "Omistaja poistaa ALUEEN";
        String visibility = "testing1";
        
        createNewArea(areaName, visibility);

        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        
        WebElement deleteButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_AREA_DELETE));
        deleteButton.click();

        assertTrue(driver.getPageSource().contains(MESSAGE_AREA_DELETE_SUCCESS));
    }
    
    @Test
    public void areaOwnerCantDeleteNotNullArea() {
        elementService.deleteAllElements();
        areaService.deleteAllAreas();
        
        String areaName = "Omistaja koittaa poistaa alueen";
        String visibility = "testing1";
        
        String contentName = "Ankaraa testausta..";
        String contentText = "Elämä On!";
        
        Area area = createNewArea(areaName, visibility);
        
        createNewContent(contentName, contentText, area);

        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);
        
        WebElement deleteButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_AREA_DELETE));
        deleteButton.click();

        assertTrue(driver.getPageSource().contains(MESSAGE_AREA_DELETE_FAIL));
    }
    
    @Test
    public void areaCannotBeWoundByOtherUsersContentManager() {
        elementService.deleteAllElements();
        areaService.deleteAllAreas();
        
        String name = "Ei kannata yrittää";
        String visibility = "testing2";

        createNewArea(name, visibility);

        driver.get(LINK_LOCALHOST + LINK_LOGIN);

        WebElement username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        WebElement password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));

        username.sendKeys(LOGIN_TEST2);
        password.sendKeys(PASSWORD_TEST2);
        password.submit();
        
        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);

        assertFalse(driver.getPageSource().contains(name));
    }
    
    @Test
    public void areaCanBeEdited() {
        elementService.deleteAllElements();
        areaService.deleteAllAreas();
        
        String name = "Editoitavaksi";
        String visibility = "testing2";

        createNewArea(name, visibility);

        driver.get(LINK_LOCALHOST + LINK_PERSONS + LINK_CONTENT_MANAGER);

        WebElement editButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_AREA_EDIT));
        editButton.click();

        name = "Arean nimi vaihtuu";
        visibility = "testing1";
        
        WebElement nameField = driver.findElement(By.name(ATTRIBUTE_NAME));
        WebElement visibilityField = driver.findElement(By.id(visibility));
    
        nameField.clear();
        nameField.sendKeys(name);
        visibilityField.click();

        editButton = driver.findElement(By.name(ATTRIBUTE_BUTTON_AREA_EDIT));
        editButton.click();
        
        assertTrue(driver.getPageSource().contains(MESSAGE_AREA_MODIFY_SUCCESS));
    }
}
