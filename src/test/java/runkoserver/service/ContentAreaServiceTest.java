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
import runkoserver.domain.Area;
import runkoserver.domain.SimpleContent;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class ContentAreaServiceTest {

    @Autowired
    ContentAreaService contentAreaService;

    private SimpleContent testSC;
    private Area testArea;
    private SimpleContent retrievedSC;
    private Area retrievedArea;

    public ContentAreaServiceTest() {
    }

    @Before
    public void setUp() {
        contentAreaService.deleteAll();
    }

    //Tests for content
    @Test
    public void testSaveContent() {
        testSC = contentAreaService.createSimpleContent("Test", "Test", null);
        assertTrue(contentAreaService.saveContent(testSC));
    }

    @Test
    public void testFindAllContent() {
        testSC = contentAreaService.createSimpleContent("Test", "Test", null);
        contentAreaService.saveContent(testSC);
        testSC = contentAreaService.createSimpleContent("Test2", "Test", null);
        contentAreaService.saveContent(testSC);
        assertEquals(2, contentAreaService.findAllContent().size());
    }

    @Test
    public void testFindContentById() {
        testSC = contentAreaService.createSimpleContent("Testausta", "Test", null);
        contentAreaService.saveContent(testSC);
        assertEquals(testSC.getName(), contentAreaService.findContentById(testSC.getId()).getName());
    }

    @Test
    public void testDeleteContent() {
        testSC = contentAreaService.createSimpleContent("Test", "Test", null);
        contentAreaService.saveContent(testSC);
        assertTrue(contentAreaService.deleteContent(testSC.getId()));
        assertFalse(contentAreaService.deleteContent(testSC.getId()));
    }

    //Test for areas
    @Test
    public void testCreateSimpleContentWithArea() {
        
    }
}
