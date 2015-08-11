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
import runkoserver.domain.Person;
import runkoserver.domain.SimpleContent;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class ContentAreaServiceTest {

    @Autowired
    ContentAreaService contentAreaService;
    
    @Autowired
    PersonService personService;

    private SimpleContent testSC;
    private Area testArea;
    private List<Long> areaIDs;
    private Person testMan;
    private Person testMan2;

    public ContentAreaServiceTest() {
    }

    @Before
    public void setUp() {
        contentAreaService.deleteAll();
        testSC = null;
        testArea = null;
        testMan= new Person("Jenny");
        personService.save(testMan);
        testMan2= new Person("Julia");
        personService.save(testMan2);

    }

    //Tests for content
    @Test
    public void testSaveContent() {
        assertFalse(contentAreaService.saveContent(testSC));
        testSC = contentAreaService.createSimpleContent("Test", "Test", null, testMan);
        assertTrue(contentAreaService.saveContent(testSC));
    }

    @Test
    public void testFindAllContent() {
        doNewSimpleContentAndSave("Test ", "Find", null, testMan);
        doNewSimpleContentAndSave("All", "Content", null, testMan);
        assertEquals(2, contentAreaService.findAllContent().size());
    }

    @Test
    public void testFindContentById() {
        doNewSimpleContentAndSave("Test Find", "by Id", null, testMan);
        assertEquals(testSC.getName(), contentAreaService.findContentById(testSC.getId()).getName());
    }

    @Test
    public void testDeleteContentWithOutAreas() {
        doNewSimpleContentAndSave("Test", "Delete", null, testMan);
        assertFalse(contentAreaService.deleteContent(testSC.getId(),testMan2));
        assertTrue(contentAreaService.deleteContent(testSC.getId(),testMan));
        assertFalse(contentAreaService.deleteContent(testSC.getId(),testMan));
    }

    @Test
    public void testDeleteContentWithAreas() {
        areaIDs = new ArrayList<>();
        doNewAreaAndSave("Test Area", null, Boolean.TRUE);
        areaIDs.add(testArea.getId());
        doNewSimpleContentAndSave("Test", "Delete", areaIDs, testMan);
        assertTrue(contentAreaService.deleteContent(testSC.getId(),testMan));
        assertFalse(contentAreaService.deleteContent(testSC.getId(),testMan));
        assertNull(testArea.getContents());
    }

    //Test for areas
    @Test
    public void testSaveArea() {
        assertFalse(contentAreaService.saveArea(testArea));
        testArea = contentAreaService.createArea("Test Save", null, Boolean.TRUE);
        assertTrue(contentAreaService.saveArea(testArea));
    }

    @Test
    public void testFindAllAreas() {
        doNewAreaAndSave("Test Area", null, Boolean.TRUE);
        doNewAreaAndSave("Test Area 2", null, Boolean.FALSE);
        assertEquals(2, contentAreaService.findAllAreas().size());
    }

    @Test
    public void testFindAreaById() {
        doNewAreaAndSave("Test Id find", null, Boolean.TRUE);
        assertEquals(testArea.getName(), contentAreaService.findAreaById(testArea.getId()).getName());
    }

    @Test
    public void testFindPublicAreas() {
        doNewAreaAndSave("Test Area", null, Boolean.TRUE);
        doNewAreaAndSave("Test Area", null, Boolean.FALSE);
        assertEquals(1, contentAreaService.findAllPublicAreas().size());
    }

    private void doNewAreaAndSave(String name, Person person, Boolean visibility) {
        testArea = contentAreaService.createArea(name, person, visibility);
        contentAreaService.saveArea(testArea);
    }

    private void doNewSimpleContentAndSave(String name, String textArea, List<Long> areaIDs, Person person) {
        testSC = contentAreaService.createSimpleContent(name, textArea, areaIDs, person);
        contentAreaService.saveContent(testSC);
    }

    @Test
    public void testDeleteAllLifeFromEarth() {
        doNewAreaAndSave("Long live the Queen", null, Boolean.TRUE);
        doNewSimpleContentAndSave("Life", "Is guut", null, testMan2);
        assertTrue(contentAreaService.deleteAll());
    }
}
