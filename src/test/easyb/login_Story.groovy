import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import runkoserver.Application;
import static runkoserver.libraries.Attributes.*;
import static runkoserver.libraries.Links.*;
import static runkoserver.libraries.Messages.*;

before "start application", {
    given "application is running", {
        SpringApplication.run(Application.class);
        driver = new HtmlUnitDriver();        
    }
}

scenario "user can see login-page", {
    given "user navigates to frontpage", {
        driver.get(LINK_LOCALHOST);
    }
    
    when "user is not logged on", {
        
    }
    
    then "user sees the frontpage", {
        driver.getPageSource().shouldHave(TITLE_FRONTPAGE);
    }
}

scenario "user can't log in with incorrect password", {
    given "user is at login form", {
        driver.get(LINK_LOCALHOST + LINK_LOGIN);
    }
    
    when "correct username and incorrect password are entered", {
        username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));
        
        username.sendKeys("testi");
        password.sendKeys("hihi");
        password.submit();
    }
    
    then "login-page should have error-message", {
        driver.getPageSource().shouldHave(MESSAGE_LOGIN_DEFAULT);
    }
}

scenario "user can log in with correct credentials", {
    given "user is at login form", {
        driver.get(LINK_LOCALHOST + LINK_LOGIN);
    }
    
    when "valid username and password are entered", {
        username = driver.findElement(By.name(ATTRIBUTE_USERNAME));
        password = driver.findElement(By.name(ATTRIBUTE_PASSWORD));
        
        username.sendKeys("testi");
        password.sendKeys("testi");
        password.submit();
    }
    
    then "user should be redirected to frontpage", {
        driver.getPageSource().shouldHave(TITLE_FRONTPAGE);
    }
}



