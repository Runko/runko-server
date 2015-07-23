package runkoserver;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import runkoserver.service.PersonService;
import runkoserver.service.RepoService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ApplicationTest {
    
    @Autowired
    PersonService personService;
    
    List<RepoService> repositories;
    
    public ApplicationTest() {
        repositories.add(personService);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
        for (RepoService repository : repositories) {
            repository.deleteAll();
        }
    }

    /**
     * Test of main method, of class Application.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        Application.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
