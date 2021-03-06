/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runkoserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import static runkoserver.libraries.Links.*;

/**
 * Configuration for example to view-controllers.
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter{
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(LINK_LOGIN).setViewName(FILE_LOGIN);
    }
}
