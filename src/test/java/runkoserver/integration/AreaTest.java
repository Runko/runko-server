/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runkoserver.integration;

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
    
    private Area createNewArea(String areaName, String id) {
        driver.get(LINK_LOCALHOST + LINK_AREA_INDEX + LINK_AREA_FORM);
        
        WebElement name = driver.findElement(By.name(ATTRIBUTE_NAME));
        WebElement publicity = driver.findElement(By.id(id));
                
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
        String visibility = "testing1";
        Area area = createNewArea(areaName, visibility);
        
        assertTrue(driver.getPageSource().contains(MESSAGE_AREA_SAVE_SUCCESS));
        driver.get(LINK_LOCALHOST);
        assertTrue(driver.getPageSource().contains("Julkinen"));
        driver.get(LINK_LOCALHOST + LINK_AREA_INDEX + "/" + area.getId());
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
        
        driver.get(LINK_LOCALHOST + LINK_AREA_INDEX + "/" + area.getId());
        
        assertTrue(driver.getPageSource().contains(contentName));
    }
    
    @Test
    public void areaVisibilityCanChangeFromDefault(){
        String areaName = "Näkyvyys vaihtuu";
        String visibility = "testing2";
       
        createNewArea(areaName, visibility);
        
        assertTrue(driver.getPageSource().contains(MESSAGE_AREA_SAVE_SUCCESS) );
        driver.get(LINK_LOCALHOST);
        assertTrue(driver.getPageSource().contains("Kirjautuneille"));
    }
    
    @Test
    public void areaHasSubscriptionButtonWhenNotSubscribed() {
        String areaName = "Elder Scrolls";
        String visibility = "testing1";
        Area area = createNewArea(areaName, visibility);
        
        driver.get(LINK_LOCALHOST + LINK_AREA_INDEX + "/" + area.getId());
        
        assertTrue(driver.getPageSource().contains(ATTRIBUTE_SUBSCRIPTION) &&
                !driver.getPageSource().contains(ATTRIBUTE_UNSUBSCRIPTION));
    }
    
    @Test
    public void areaHasUnsubscribeButtonWhenSubscribed() {
        String areaName = "Praise the Sun!";
        String visibility = "testing1";
        Area area = createNewArea(areaName, visibility);
        
        driver.get(LINK_LOCALHOST + LINK_AREA_INDEX + "/" + area.getId());
        
        WebElement subscribe = driver.findElement(By.name(ATTRIBUTE_BUTTON_SUBSCRIBE));
        subscribe.click();
        
        assertTrue(!driver.getPageSource().contains(ATTRIBUTE_SUBSCRIPTION) &&
                driver.getPageSource().contains(ATTRIBUTE_UNSUBSCRIPTION));
    }
}
