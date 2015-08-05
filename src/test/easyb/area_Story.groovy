import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import runkoserver.Application;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;

before "testing application", {
    given "application is running", {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        driver = new HtmlUnitDriver();        
    }
    
    and "user is logged in", {
        driver.get(LINK_LOCALHOST + LINK_LOGIN);
        
        username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));
        
        username.sendKeys("testi");
        password.sendKeys("testi");
        password.submit();
    }
}

scenario "user can create a new area with correct parameters", {
    given "user has navigated to area creation page", {
        driver.get(LINK_LOCALHOST + LINK_AREA_FORM);
    }
    
    when "user fills area's information", {
        element = driver.findElement(By.name(ATTRIBUTE_NAME));
        otsikko = "otsikko";
        element.sendKeys(otsikko);
        element.submit();
    }
    
    then "new area is seen on the frontpage", {
        driver.getPageSource().shouldHave(otsikko);
    }
}

after "stop server" , {
    then "server should be shutdown", {
        driver.quit();
        context.close();
    }
}