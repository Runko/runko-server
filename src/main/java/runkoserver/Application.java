
package runkoserver;

import runkoserver.profiles.DevProfile;
import runkoserver.profiles.ProdProfile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

 
@Configuration
@EnableAutoConfiguration
@ComponentScan
@Import({DevProfile.class, ProdProfile.class})
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
 
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    } 
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
