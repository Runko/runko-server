/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runkoserver.integration;

import com.google.common.io.Files;
import java.io.File;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Initializer for selenium tests.
 * http://blog.codeleak.pl/2015/03/spring-boot-integration-testing-with.html
 */
public class SeleniumTestExecutionListener extends AbstractTestExecutionListener {

    private WebDriver webDriver;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        if (webDriver != null) {
            return;
        }
        ApplicationContext context = testContext.getApplicationContext();
        if (context instanceof ConfigurableApplicationContext) {

            SeleniumTest annotation = findAnnotation(
                    testContext.getTestClass(), SeleniumTest.class);
            webDriver = BeanUtils.instantiate(annotation.driver());

            ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
            ConfigurableListableBeanFactory bf = configurableApplicationContext.getBeanFactory();
            bf.registerResolvableDependency(WebDriver.class, webDriver);
        }
    }
    
    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        if (webDriver != null) {
            SeleniumTest annotation = findAnnotation(
                    testContext.getTestClass(), SeleniumTest.class);
            webDriver.get(annotation.baseUrl());
        }
    }
    
    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
    
    
}
