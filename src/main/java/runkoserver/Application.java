package runkoserver;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import runkoserver.repository.PersonRepository;

/**
 * Main class that starts up the application.
 */
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class Application {
    
    @Autowired
    PersonRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
