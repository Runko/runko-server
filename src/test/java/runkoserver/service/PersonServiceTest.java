package runkoserver.service;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import runkoserver.Application;
import runkoserver.domain.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class PersonServiceTest {

    @Autowired
    PersonService personService;

    public PersonServiceTest() {
    }

    @Before
    public void setUp() {
        personService.deleteAll();
        personService.save(new Person("Matti"));
        personService.save(new Person("Timo"));

    }

    @Test
    public void testPersonsAreAdded() {
        personService.deleteAll();

        Person p = new Person("Jenny");

        personService.save(p);

        assertEquals(1, personService.findAll().size());
    }

    @Test
    public void testListPersons() {

        assertEquals(2, personService.findAll().size());
    }

    @Test
    public void testAddedPersonsAreFound() {

        Person p = new Person("Julia");
        personService.save(p);

        assertEquals(p, personService.findById(p.getId()));
    }

    @Test
    public void testUnAddedPersonsAreNotFound() {

        Person p = new Person("Henna");

        assertEquals(null, personService.findById(p.getId()));
    }

    @Test
    public void testPersonsCanBeFoundByName() {

        Person p = personService.findByName("Matti").get(0);

        assertEquals(p, personService.findById(p.getId()));
    }

    @Test
    public void testDeletedPersonsAreNotFound() {

        Person p = personService.findByName("Timo").get(0);
        personService.delete(p.getId());

        assertEquals(null, personService.findById(p.getId()));
    }

    @Test
    public void testPersonMustHaveName() {

        personService.save(new Person("    "));
        personService.save(new Person(null));
        assertEquals(2, personService.findAll().size());
    }

}
