package runkoserver.service;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import runkoserver.Application;
import runkoserver.domain.Area;
import runkoserver.domain.Content;
import runkoserver.domain.SimpleContent;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class AreaServiceTest {

    @Autowired
    AreaService areaService;

    @Autowired
    ContentService contentService;
    
    private Area test;

    public AreaServiceTest() {
    }

    @Test
    public void testSaveArea() {
        doNewArea("Test");
        Area retrieved = areaService.findById(test.getId());
        assertNotNull(retrieved);
        assertEquals("Test", retrieved.getName());
    }

    @Test
    public void testListAllAreas() {
        doNewArea("Test 2");
        assertEquals(2, areaService.findAll().size());
    }

    @Test
    public void testDeleteArea() {
        List<Area> list = areaService.findAll();
        test = list.get(1);
        areaService.delete(test.getId());
        assertEquals(1, areaService.findAll().size());
        assertNull(areaService.findById(test.getId()));
    }
    
    @Test
    public void testNameIsEmptyOrNull() {
        test = new Area();
        test.setName("    ");
        assertFalse(areaService.save(test));
        test = new Area();
        test.setName(null);
        assertFalse(areaService.save(test));
    }
    
    public void testFindByIds(){
        doNewArea("test3");
        long id = test.getId();
        Area test3 = areaService.findById(id);
        assertEquals(test3, test);
    }
    
    public void testAddContentToArea(){
        SimpleContent content = new SimpleContent();
        contentService.save(content);
        List<Area> areas = new ArrayList();
        areas.add(test);
        areaService.addContentToAreas(areas, content);
        assertEquals(areas, content.getAreas());
    }

    private void doNewArea(String name) {
        test = new Area();
        test.setName(name);
        areaService.save(test);
    }
    
 
}
