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

    //Tests for content
    @Test
    public void testSaveContent() {
        assertFalse(elementService.saveElement(testSC));
        testSC = elementService.createContent("Test", "Test", null, testMan);
        assertTrue(elementService.saveElement(testSC));
    }

    @Test
    public void testFindAllContent() {
        doNewSimpleContentAndSave("Test ", "Find", null, testMan);
        doNewSimpleContentAndSave("All", "Content", null, testMan);
        assertEquals(2, elementService.findAllElements().size());
    }

    @Test
    public void testFindContentById() {
        doNewSimpleContentAndSave("Test Find", "by Id", null, testMan);
        assertEquals(testSC.getName(), elementService.findElementById(testSC.getId()).getName());
    }

    @Test
    public void testDeleteContentWithOutAreas() {
        doNewSimpleContentAndSave("Test", "Delete", null, testMan);
        assertFalse(elementService.deleteElement(testSC.getId(), testMan2));
        assertTrue(elementService.deleteElement(testSC.getId(), testMan));
        assertFalse(elementService.deleteElement(testSC.getId(), testMan));
    }

    @Test
    public void testDeleteContentWithAreas() {
        areaIDs = new ArrayList<>();
        doNewAreaAndSave("Test Area", null, Boolean.TRUE);
        areaIDs.add(testArea.getId());
        doNewSimpleContentAndSave("Test", "Delete", areaIDs, testMan);
        assertTrue(elementService.deleteElement(testSC.getId(), testMan));
        assertFalse(elementService.deleteElement(testSC.getId(), testMan));
        assertEquals(0, testArea.getElements().size());
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

    private void doNewAreaAndSave(String name, Person person, Boolean visibility) {
        testArea = areaService.createArea(name, person, visibility);
        areaService.saveArea(testArea);
    }

    private void doNewSimpleContentAndSave(String name, String textArea, List<Long> areaIDs, Person person) {
        testSC = elementService.createContent(name, textArea, areaIDs, person);
        elementService.saveElement(testSC);
    }

    @Test
    public void testDeleteAllLifeFromEarth() {
        doNewAreaAndSave("Long live the Queen", null, Boolean.TRUE);
        doNewSimpleContentAndSave("Life", "Is guut", null, testMan2);
        assertTrue(areaService.deleteAllAreas() && elementService.deleteAllElements());
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
    public void testCreateListFromSubscribedContentReturnsContentNewestFirst() throws InterruptedException {
        testArea = areaService.createArea("Dohvakin", testMan, Boolean.TRUE);
        areaService.saveArea(testArea);
        personService.addSubscribtion(testMan, testArea);

        List<Long> areaIds = new ArrayList<>();
        areaIds.add(testArea.getId());

        Content c1 = elementService.createContent("Force", "FUS", areaIds, testMan);
        testArea.addElements(c1);
        Thread.sleep(1000l);
        Content c2 = elementService.createContent("Balance", "RO", areaIds, testMan);
        testArea.addElements(c2);
        Thread.sleep(1000l);
        Content c3 = elementService.createContent("Push", "DAH", areaIds, testMan);
        testArea.addElements(c3);

        List<Content> subscribedContent = areaService.createListFromSubscripedContents(testMan);

        assertEquals(c3.getName(), subscribedContent.get(0).getName());
    }

    @Test
    public void testCreateListFromSubscribersReturnsContentLastModifiedFirst() throws InterruptedException {
        testArea = areaService.createArea("Dohvakin", testMan, Boolean.TRUE);
        areaService.saveArea(testArea);
        personService.addSubscribtion(testMan, testArea);
        
        List<Long> areaIds = new ArrayList<>();
        areaIds.add(testArea.getId());
        
        String c1Name = "Force";
        String c1Text = "FUS";
        
        Content c1 = elementService.createContent(c1Name, "FUS", areaIds, testMan);
        c1.setOwner(testMan);
        testArea.addElements(c1);
        Thread.sleep(1000l);
        Content c2 = elementService.createContent("Balance", "RO", areaIds, testMan);
        c2.setOwner(testMan);
        testArea.addElements(c2);
        Thread.sleep(1000l);
        Content c3 = elementService.createContent("Push", "DAH", areaIds, testMan);
        testArea.addElements(c3);
        c3.setOwner(testMan);
        
        Thread.sleep(1000l);
        c1.setModifyTime();
        
        List<Content> subscribedContent = areaService.createListFromSubscripedContents(testMan);
        
        assertEquals(c1.getName(), subscribedContent.get(0).getName());
    }
}
