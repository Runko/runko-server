package runkoserver.libraries;

/**
 * String-library which contains links
 */
public abstract class Links {
    public static final String REDIRECT = "redirect:";
    public static final String REDIRECT_HOME = "redirect:/";
    
    public static final String LINK_LOCALHOST = "http://localhost:8080";
    
    public static final String LINK_HOME = "/";
    
    public static final String LINK_PROFILE = "/profile";
    public static final String LINK_PERSONS = "/persons";
    
    public static final String LINK_VIEW_ID = "/{id}";
    
    public static final String LINK_AREA_INDEX = "/area";
    // You can access area-view for example like LINK_AREA_INDEX + LINK_VIEW_ID.
    //If index is already given, LINK_VIEW_ID is enough    
    public static final String LINK_AREA_FORM = "/areaform";
    
    public static final String LINK_CONTENT_INDEX = "/content";
    public static final String LINK_CONTENT_SIMPLEFORM = "/simpleform";
    
    public static final String LINK_LOGIN = "/login";
       
    public static final String FILE_AREA = "/area/area";
    public static final String FILE_AREA_FORM = "/area/area_form";
    
    public static final String FILE_HOME = "index";
    
    public static final String FILE_PROFILE = "profile";
    
    public static final String FILE_PERSONS = "persons";
    public static final String FILE_PERSON = "person";
    
    
    public static final String FILE_SIMPLECONTENT = "/content/simple_content";
    public static final String FILE_SIMPLECONTENT_FORM = "/content/simple_content_form";
    
    public static final String FILE_LOGIN = "login";
    
    public static final String FOLDER_CSS = "/static/**";
}
