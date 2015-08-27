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
public class ElementServiceTest {

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

    public ElementServiceTest() {
    }

    @Before
    public void setUp() {
        elementService.deleteAllElements();
        areaService.deleteAllAreas();
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

    //Tests for content
    @Test
    public void testSaveContent() {
        assertFalse(elementService.saveElement(testSC));
        testSC = elementService.createContent("Test", "Test", null, testMan);
        assertTrue(elementService.saveElement(testSC));
    }

    @Test
    public void testFindAllContent() {
        doNewContentAndSave("Test ", "Find", null, testMan);
        doNewContentAndSave("All", "Content", null, testMan);
        assertEquals(2, elementService.findAllElements().size());
    }

    @Test
    public void testFindContentById() {
        doNewContentAndSave("Test Find", "by Id", null, testMan);
        assertEquals(testSC.getName(), elementService.findElementById(testSC.getId()).getName());
    }

    @Test
    public void testDeleteContentWithOutAreas() {
        doNewContentAndSave("Test", "Delete", null, testMan);
        assertFalse(elementService.deleteElement(testSC.getId(), testMan2));
        assertTrue(elementService.deleteElement(testSC.getId(), testMan));
        assertFalse(elementService.deleteElement(testSC.getId(), testMan));
    }

    @Test
    public void testDeleteContentWithAreas() {
        areaIDs = new ArrayList<>();
        doNewAreaAndSave("Test Area", null, Boolean.TRUE);
        areaIDs.add(testArea.getId());
        doNewContentAndSave("Test", "Delete", areaIDs, testMan);
        assertTrue(elementService.deleteElement(testSC.getId(), testMan));
        assertFalse(elementService.deleteElement(testSC.getId(), testMan));
        assertEquals(0, testArea.getElements().size());
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
    
    @Test
    public void testContentCanBeBookmarked() {
        doNewAreaAndSave("He-Man", testMan, Boolean.FALSE);
        testArea = areaService.findAreaByName("He-Man");
        List<Long> areaIds = new ArrayList<>();
        areaIds.add(testArea.getId());
        Content content = elementService.createContent("I say HEEYYYYYY", "What's going on?", areaIds, testMan);
        personService.addBookmark(testMan, content);
                
        assertEquals(1, testMan.getBookmarks().size());
    }
    
    @Test
    public void testContentCanBeUnbookmarked() {   
        doNewAreaAndSave("He-Man2", testMan, Boolean.FALSE);
        testArea = areaService.findAreaByName("He-Man2");
        List<Long> areaIds = new ArrayList<>();
        areaIds.add(testArea.getId());
        Content content = elementService.createContent("I say HEEYYYYYY", "What's going on?", areaIds, testMan);
        personService.addBookmark(testMan, content);
        personService.addBookmark(testMan, content);
        
        assertEquals(0, testMan.getBookmarks().size());
    }
}
