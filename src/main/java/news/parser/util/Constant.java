package news.parser.util;

/**
 * Class with all constants
 */
public class Constant {

    public static final String HEADLINE_FONT_STYLE = "-fx-font-weight: bold;";

    public static final int ONE_DAY_IN_SECONDS = 86400;

    public static final String PUBLICATION_TIME_PATTERN = "d MMM yyyy, HH:mm";

    public static final String MORNING_TAB_TEXT = "Morning";
    public static final String DAY_TAB_TEXT = "Day";
    public static final String EVENING_TAB_TEXT = "Evening";

    public static final String PATH_TO_MAIN_PAGE_FXML = "/ui/main-page.fxml";

    public static final String JAVA_FX_APPLICATION_TITLE = "Spiegel news parser";

    public static final String AGREE_BUTTON = "//button[@title='Consent and continue']";

    public static final String IFRAME_ID = "sp_message_iframe_975098";

    public static final String WEBSITE_URL = "https://www.spiegel.de/thema/grupo_acs/";


    public static final String DATE_PATTERN_ON_WEBSITE = "d. MMMM yyyy, HH.mm 'Uhr'";
    public static final String MAIN_DATE_PATTERN = "dd.MM.yyyy HH:mm";

    public static final String DIV_WITH_ALL_ARTICLES_SELECTOR = "div[data-area='article-teaser-list']";
    public static final String ALL_ARTICLES_SELECTOR = "*[data-block-el='articleTeaser']";


    public static final String TITLE_ELEMENT_SELECTOR = "h2[class*='title'] > a, h2 > a";
    public static final String DESCRIPTION_ELEMENT_SELECTOR = "section span[data-target-teaser-el='text']";
    public static final String DATE_ELEMENT_SELECTOR = "footer span[data-auxiliary='']";
}
