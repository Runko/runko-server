package runkoserver.libraries;

/**
 * String-library which contains links
 */
public abstract class Links {

    public static final String REDIRECT = "redirect:";
    public static final String REDIRECT_HOME = "redirect:/";

    public static final String LINK_HOME = "/";
    public static final String LINK_EDIT = "/edit";
    public static final String LINK_PROFILE = "/profile";
    public static final String LINK_PERSONS = "/persons";
    public static final String LINK_CONTENT_MANAGER = "/CM";
    public static final String LINK_BOOKMARK = "/bookmarks";
    public static final String LINK_FRONTPAGE = "/frontpage";
    

    public static final String LINK_VIEW_ID = "/{id}";

    public static final String LINK_AREA_INDEX = "/area";

    // You can access area-view for example like LINK_AREA_INDEX + LINK_VIEW_ID.
    //If index is already given, LINK_VIEW_ID is enough    
    public static final String LINK_AREA_FORM = "/areaform";

    public static final String LINK_CONTENT = "/content";
    public static final String LINK_CONTENT_FORM = "/contentform";

    public static final String LINK_LOGIN = "/login";
    public static final String LINK_LOGOUT = "/logout";
    public static final String LINK_LOGIN_LOGOUT = "/login_logout";

    //for test-purposes
    public static final String SERVER_PORT = "server.port=9000";
    public static final String LINK_LOCALHOST = "http://localhost:9000";

    public static final String FILE_AREA = "/area/area";
    public static final String FILE_AREA_FORM = "/area/area_form";

    public static final String FILE_HOME = "index";

    public static final String FILE_PROFILE = "person/profile";
    public static final String FILE_PROFILE_EDIT = "person/profile_edit";
    public static final String FILE_CONTENT_MANAGER = "person/content_manager";
    public static final String FILE_BOOKMARK= "person/bookmarks";
    public static final String FILE_PERSONS = "persons";
    public static final String FILE_PERSON = "person";
    public static final String FILE_FRONTPAGE = "frontpage";

    public static final String FILE_CONTENT = "/content/content_view";
    public static final String FILE_CONTENT_FORM = "/content/content_form";
    public static final String FILE_CONTENT_EDIT = "/content/content_edit";

    public static final String FILE_LOGIN = "login";

    public static final String FOLDER_CSS = "/static/**";
}
