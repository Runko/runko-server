package runkoserver.integration;

import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import runkoserver.Application;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;

/**
 * Does it twerk.
 */
public class SeleniumTest {
    private static ApplicationContext context;
    private static WebDriver driver;
    
    @BeforeClass
    public static void initialize() {
        context = SpringApplication.run(Application.class);
        driver = new HtmlUnitDriver();
    }
    
    @Test
    public void frontPageCanBeSeen() {
        driver.get(LINK_LOCALHOST);
        
        assertTrue(driver.getPageSource().contains(TITLE_FRONTPAGE));
    }
    
    @Test
    public void userCanLogIn() {
        driver.get(LINK_LOCALHOST + LINK_LOGIN);
        
        WebElement username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        WebElement password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));
        
        username.sendKeys("testi");
        password.sendKeys("testi");
        
        password.submit();
        
        assertTrue(driver.getPageSource().contains(TITLE_FRONTPAGE));
    }
}
