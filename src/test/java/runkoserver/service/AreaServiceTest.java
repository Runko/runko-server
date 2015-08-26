package runkoserver.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import runkoserver.Application;
import runkoserver.domain.Area;
import runkoserver.domain.Content;
import runkoserver.domain.Person;
import runkoserver.service.AreaService;
import runkoserver.service.ElementService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class AreaServiceTest {

    @Autowired
    private AreaService areaService;

    @Autowired
    private ElementService elementService;

    @Autowired
    PersonService personService;

    private Content testSC;
    private Area testArea;
    private List<Long> areaIDs;
    private Person testMan;
    private Person testMan2;

    public AreaServiceTest() {
    }

    @Before
    public void setUp() {
        areaService.deleteAllAreas();
        elementService.deleteAllElements();
        testSC = null;
        testArea = null;
        testMan = new Person("Jenny");
        personService.save(testMan);
        testMan2 = new Person("Julia");
        personService.save(testMan2);

    }

    private void doNewAreaAndSave(String name, Person person, Boolean visibility) {
        testArea = areaService.createArea(name, person, visibility);
        areaService.saveArea(testArea);
    }

    private void doNewContentAndSave(String name, String textArea, List<Long> areaIDs, Person person) {
        testSC = elementService.createContent(name, textArea, areaIDs, person);
        elementService.saveElement(testSC);
    }
    
    //Test for areas

    @Test
    public void testSaveArea() {
        assertFalse(areaService.saveArea(testArea));
        testArea = areaService.createArea("Test Save", null, Boolean.TRUE);
        assertTrue(areaService.saveArea(testArea));
    }

    @Test
    public void testFindAllAreas() {
        doNewAreaAndSave("Test Area", null, Boolean.TRUE);
        doNewAreaAndSave("Test Area 2", null, Boolean.FALSE);
        assertEquals(2, areaService.findAllAreas().size());
    }

    @Test
    public void testFindAreaById() {
        doNewAreaAndSave("Test Id find", null, Boolean.TRUE);
        assertEquals(testArea.getName(), areaService.findAreaById(testArea.getId()).getName());
    }

    @Test
    public void testFindPublicAreas() {
        doNewAreaAndSave("Test Area", null, Boolean.TRUE);
        doNewAreaAndSave("Test Area", null, Boolean.FALSE);
        assertEquals(1, areaService.findAllPublicAreas().size());
    }

    @Test
    public void testDeleteAllLifeFromEarth() {
        doNewAreaAndSave("Long live the Queen", null, Boolean.TRUE);
        doNewContentAndSave("Life", "Is guut", null, testMan2);
        assertTrue(elementService.deleteAllElements() && areaService.deleteAllAreas());
    }

    @Test
    public void testAreasCanBeSubscribed() {
        testArea = areaService.createArea("What's going on?", testMan, Boolean.TRUE);
        personService.addSubscribtion(testMan, testArea);

        assertEquals(1, testMan.getSubscriptions().size());
    }

    @Test
    public void testCreateListFromSubscribedContentsReturnsSubscribedContent() {
        testArea = areaService.createArea("Dohvakin", testMan, Boolean.TRUE);
        areaService.saveArea(testArea);
        personService.addSubscribtion(testMan, testArea);

        List<Long> areaIds = new ArrayList<>();
        areaIds.add(testArea.getId());

        Content c1 = elementService.createContent("Force", "FUS", areaIds, testMan);
        testArea.addElements(c1);
        Content c2 = elementService.createContent("Balance", "RO", areaIds, testMan);
        testArea.addElements(c2);
        Content c3 = elementService.createContent("Push", "DAH", areaIds, testMan);
        testArea.addElements(c3);

        List<Content> subscribedContent = areaService.createListFromSubscripedContents(testMan);

        assertEquals(3, subscribedContent.size());
    }

    @Test
    public void testDeleteAreaWithOutElements() {
        doNewAreaAndSave("TestiArea", testMan, Boolean.TRUE);
        assertTrue(areaService.deleteArea(testSC.getId(), testMan));
        assertFalse(areaService.deleteArea(testSC.getId(), testMan));
    }

    @Test
    public void testDontDeleteAreatWithElements() {
        areaIDs = new ArrayList<>();
        doNewAreaAndSave("Test Area", testMan, Boolean.TRUE);
        areaIDs.add(testArea.getId());
        doNewContentAndSave("Test", "Delete", areaIDs, testMan);
        assertFalse(areaService.deleteArea(testSC.getId(), testMan));
    }
}
