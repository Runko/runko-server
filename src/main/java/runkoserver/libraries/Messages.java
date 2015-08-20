package runkoserver.libraries;

/**
 * String-library containing messages.
 */
public abstract class Messages {

    public static final String MESSAGE_DEFAULT = "Something happend.";

    public static final String MESSAGE_AREA_SAVE_SUCCESS = "Uusi alue tallennettu!";
    public static final String MESSAGE_AREA_SAVE_FAIL = "Alueen tallentaminen epäonnistui!";

    public static final String MESSAGE_CONTENT_SAVE_SUCCESS = "Uutta sisältöä tallennettu!";
    public static final String MESSAGE_CONTENT_SAVE_FAIL = "Sisällön tallentaminen epäonnistui";
    public static final String MESSAGE_CONTENT_MODIFY_SUCCESS = "Sisällön muokkaaminen onnistui";
    public static final String MESSAGE_CONTENT_MODIFY_FAIL = "Sinulla ei ole oikeuksia muokata sisältöä! Hyi Hyi.";
    public static final String MESSAGE_CONTENT_DELETE_SUCCESS = "Sisältö poistettu onnistuneesti: ";
    public static final String MESSAGE_CONTENT_DELETE_FAIL = "Sisällön poistaminen epäonnistui";
    public static final String MESSAGE_AREA_SUBSCRIPTION_START = "Alueen tilaaminen onnistui!";
    public static final String MESSAGE_AREA_SUBSCRIPTION_STOP = "Alueen tilaaminen lopetettu!";

    //is shown by Spring security by default. Used at least for testing.
    public static final String MESSAGE_LOGIN_DEFAULT = "Käyttäjätunnus tai salasana ei kelpaa.";

    public static final String MESSAGE_NOT_PERMISSION = "Sinulla ei ole oikeuksia toimintoon.";

    public static final String MESSAGE_PAGE_NOT_AVAILABLE = "Pahoittelut, hakemasi sivu ei ole saatavilla.";
    public static final String MESSAGE_NOT_LOGGEDIN = "Et ole kirjautunut sisään";
    public static final String MESSAGE_PERSON_MODIFY_SUCCESS = "Käyttäjätietojen muokkaaminen onnistui";

    public static final String MESSAGE_PRIVATE_ELEMENT = "Kirjautuneille";
    public static final String MESSAGE_PUBLIC_ELEMENT = "Julkinen";
}
