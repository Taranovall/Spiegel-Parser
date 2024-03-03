package news.parser.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import news.parser.entity.Article;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static news.parser.util.Constant.ONE_DAY_IN_SECONDS;

@Component
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleUtil {

    /**
     * Filters a list of articles based on their publication time falling within a specified time range.
     * The filtered list is sorted in descending order based on publication time.
     *
     * @param articles  The list of articles to filter.
     * @param startTime The start time of the time range (inclusive).
     * @param endTime   The end time of the time range (exclusive).
     * @return A list containing only the articles whose publication time falls within the specified time range,
     * sorted in descending order based on publication time.
     */
    public static List<Article> filterArticlesByTime(List<Article> articles, LocalTime startTime, LocalTime endTime) {
        return articles.stream()
                .filter(article -> {
                    LocalTime articleTime = article.getPublicationTime().toLocalTime();
                    if (endTime.isBefore(startTime)) {
                        return articleTime.isAfter(startTime) || articleTime.isBefore(endTime);
                    } else {
                        return articleTime.isAfter(startTime) && articleTime.isBefore(endTime);
                    }
                })
                .sorted(Comparator.comparing(Article::getPublicationTime).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Coverts WebElement to Article
     *
     * @param articleElement WebElement in which data about article is stored
     * @return object Article
     */
    public static Article convertWebElementToArticle(WebElement articleElement) {
        WebElement titleElement = articleElement.findElement(By.cssSelector(Constant.TITLE_ELEMENT_SELECTOR));
        String title = titleElement.getText().trim().split(System.lineSeparator())[1];

        WebElement descriptionElement = articleElement.findElement(By.cssSelector(Constant.DESCRIPTION_ELEMENT_SELECTOR));
        String description = descriptionElement.getText().trim();

        WebElement dateElement = articleElement.findElement(By.cssSelector(Constant.DATE_ELEMENT_SELECTOR));
        String date = dateElement.getText().trim();

        return new Article(title, description, convertStringToDate(date));
    }


    /**
     * Checks if the specified date occurred less than 24 hours ago.
     *
     * @param dateTime The date to be checked.
     * @return {@code true} if the specified date occurred less than 24 hours ago, {@code false} otherwise.
     */
    public static boolean isLessThanOneDayAgo(LocalDateTime dateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        long secondsDifference = ChronoUnit.SECONDS.between(dateTime, currentDateTime);

        return secondsDifference <= ONE_DAY_IN_SECONDS;
    }

    /**
     * Converts date from string to LocalDateTime class
     *
     * @param dateString date in string format
     * @return LocalDateTime
     */
    private static LocalDateTime convertStringToDate(String dateString) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(Constant.DATE_PATTERN_ON_WEBSITE, Locale.GERMAN);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(Constant.MAIN_DATE_PATTERN);

        LocalDateTime dateTime = null;
        try {
            LocalDateTime parsedDateTime = LocalDateTime.parse(dateString, inputFormatter);
            String formattedDateString = outputFormatter.format(parsedDateTime);
            dateTime = LocalDateTime.parse(formattedDateString, outputFormatter);
        } catch (DateTimeParseException e) {
            log.error("Error parsing date '{}': {}", dateString, e.getMessage());
        }
        return dateTime;
    }
}
