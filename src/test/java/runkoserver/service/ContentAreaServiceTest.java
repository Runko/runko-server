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

    private SimpleContent testSC;
    private Area testArea;

    public ContentAreaServiceTest() {
    }

    @Before
    public void setUp() {
        contentAreaService.deleteAll();
        testSC = null;
        testArea = null;
    }

    //Tests for content
    @Test
    public void testSaveContent() {
        assertFalse(contentAreaService.saveContent(testSC));
        testSC = contentAreaService.createSimpleContent("Test", "Test", null, null);
        assertTrue(contentAreaService.saveContent(testSC));
    }

    @Test
    public void testFindAllContent() {
        doNewSimpleContentAndSave("Test ", "Find", null, null);
        doNewSimpleContentAndSave("All", "Content", null, null);
        assertEquals(2, contentAreaService.findAllContent().size());
    }

    @Test
    public void testFindContentById() {
        doNewSimpleContentAndSave("Test Find", "by Id", null, null);
        assertEquals(testSC.getName(), contentAreaService.findContentById(testSC.getId()).getName());
    }

    @Test
    public void testDeleteContentWithOutAreas() {
        doNewSimpleContentAndSave("Test", "Delete", null, null);
        assertTrue(contentAreaService.deleteContent(testSC.getId()));
        assertFalse(contentAreaService.deleteContent(testSC.getId()));
    }

    @Test
    public void testDeleteContentWithAreas() {
        List<Long> areaIDs = new ArrayList<>();
        doNewAreaAndSave("Test Area", null, Boolean.TRUE);
        areaIDs.add(testArea.getId());
        doNewSimpleContentAndSave("Test", "Delete", areaIDs, null);
        assertTrue(contentAreaService.deleteContent(testSC.getId()));
        assertFalse(contentAreaService.deleteContent(testSC.getId()));
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
    public void testDeleteAllLifeFromEarth(){
    doNewAreaAndSave("Long live the Queen", null, Boolean.TRUE);
    doNewSimpleContentAndSave("Life","Is guut", null, null);
    assertTrue(contentAreaService.deleteAll());
    }
}
