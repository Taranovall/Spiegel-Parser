package news.parser.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import news.parser.entity.Article;
import news.parser.service.ArticleService;
import news.parser.util.Constant;
import news.parser.util.Util;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static news.parser.util.Constant.*;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class NewsParserSchedulerConfig {

    private final ArticleService articleService;
    private final Util util;

    @Scheduled(fixedRateString = "${fixed-rate-ms}")
    public void parseNews() {
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");

            WebDriver driver = new ChromeDriver(options);

            driver.get(Constant.WEBSITE_URL);
            log.info("Website {} opened", Constant.WEBSITE_URL);
            closePopUpWindow(driver);
            log.info("Pop up windows was closed");

            List<Article> newArticles = retrieveAllNewArticles(driver);

            if (!newArticles.isEmpty())
                articleService.saveArticles(newArticles);

            articleService.deleteAllArticlesOlderThatOneDay();
            driver.quit();
            log.info("Browser was closed");
        } catch (Exception e) {
            log.error("An error occurred while parsing news: {}", e.getMessage());
        }
    }

    /**
     * Retrieves all articles which were published less than a day ago and don't exist in database
     *
     * @param driver
     * @return list with parsed articles
     */
    private List<Article> retrieveAllNewArticles(WebDriver driver) {
        WebElement elementWithAllArticles = driver.findElement(By.cssSelector(DIV_WITH_ALL_ARTICLES_SELECTOR));

        List<WebElement> elements = elementWithAllArticles.findElements(By.cssSelector(ALL_ARTICLES_SELECTOR));

        List<Article> articlesInBD = articleService.getAll();
        List<Article> articles = elements.stream()
                .map(util::convertWebElementToArticle)
                .filter(a -> util.isLessThanOneDayAgo(a.getPublicationTime()))
                .filter(a -> !articlesInBD.contains(a))
                .collect(Collectors.toList());

        log.info("New {} articles were retrieved from website", articles.size());
        return articles;
    }


    /**
     * Closes pop up window on the page
     *
     * @param driver
     */
    private void closePopUpWindow(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(Constant.IFRAME_ID)));
        driver.switchTo().frame(Constant.IFRAME_ID);

        WebElement elementInsidePopup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(AGREE_BUTTON)));
        elementInsidePopup.click();
        driver.switchTo().defaultContent();
    }
}
