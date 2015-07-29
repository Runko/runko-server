package runkoserver.service;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import runkoserver.Application;
import runkoserver.domain.Content;
import runkoserver.domain.SimpleContent;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class ContentServiceTest {
    
    @Autowired
    ContentService contentService;
    
    private SimpleContent testText;
    
    public ContentServiceTest() {
    }
    
    @Test
    public void testSaveSimpleContent() {
        doNewSimpleContent("Test", "Toivottavasti toimii!");
        SimpleContent retrieved = (SimpleContent) contentService.findById(testText.getId());
        assertNotNull(retrieved);
        assertEquals("Test", retrieved.getName());
        assertEquals("Toivottavasti toimii!", retrieved.getTextArea());
    }
    
    @Test
    public void testListAllContent() {
        doNewSimpleContent("Test 2", "Juhuu");
        assertEquals(2, contentService.findAll().size());
    }
    
    @Test
    public void testNameValidationWorks() {
        doNewSimpleContent(null, "Toivottavasti toimii!");
        assertNull(contentService.findById(testText.getId()));
        doNewSimpleContent("    ", "Toivottavasti toimii!");
        assertNull(contentService.findById(testText.getId()));
    }
    
    @Test
    public void testdeleteContent() {
        List<Content> list = contentService.findAll();
        testText = (SimpleContent) list.get(1);
        contentService.delete(testText.getId());
        assertEquals(1, contentService.findAll().size());
        assertNull(contentService.findById(testText.getId()));
    }
    
 
    
    private void doNewSimpleContent(String name, String text) {
        testText = new SimpleContent();
        testText.setName(name);
        testText.setTextArea(text);
        contentService.save(testText);
    }
}
