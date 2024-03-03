package news.parser.controller;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import news.parser.entity.Article;
import news.parser.factory.ArticleCellFactory;
import news.parser.service.ArticleService;
import news.parser.util.Util;
import org.springframework.stereotype.Controller;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static news.parser.util.Constant.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainPageController {

    private final Util util;

    private final ArticleService articleService;
    @FXML
    private Button refreshButton;
    @FXML
    private TabPane tabPane;
    private Map<String, List<Article>> articlesByTime = new HashMap<>();

    public void initialize() {
        tabPane.getTabs().get(0).setText(MORNING_TAB_TEXT);
        tabPane.getTabs().get(1).setText(DAY_TAB_TEXT);
        tabPane.getTabs().get(2).setText(EVENING_TAB_TEXT);
        fetchNews();
    }

    @FXML
    private void previousArticle() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        ListView<Article> listView = (ListView<Article>) selectedTab.getContent();
        int currentIndex = listView.getSelectionModel().getSelectedIndex();
        if (currentIndex > 0) {
            listView.getSelectionModel().select(currentIndex - 1);
        }
    }

    @FXML
    private void nextArticle() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        ListView<Article> listView = (ListView<Article>) selectedTab.getContent();
        int currentIndex = listView.getSelectionModel().getSelectedIndex();
        List<Article> articles = articlesByTime.get(selectedTab.getText());
        if (currentIndex < articles.size() - 1) {
            listView.getSelectionModel().select(currentIndex + 1);
        }
    }

    @FXML
    private void refreshArticles() {
        refreshButton.setDisable(true);

        fetchNews();

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> refreshButton.setDisable(false));
        pause.play();
    }

    public void fetchNews() {
        List<Article> allArticles = articleService.getAll();

        articlesByTime.put(MORNING_TAB_TEXT, util.filterArticlesByTime(allArticles, LocalTime.of(0, 0), LocalTime.of(12, 0)));
        articlesByTime.put(DAY_TAB_TEXT, util.filterArticlesByTime(allArticles, LocalTime.of(12, 0), LocalTime.of(18, 0)));
        articlesByTime.put(EVENING_TAB_TEXT, util.filterArticlesByTime(allArticles, LocalTime.of(18, 0), LocalTime.of(0, 0)));

        tabPane.getTabs().forEach(tab -> {
            ListView<Article> listView = (ListView<Article>) ((Tab) tab).getContent();
            String tabText = tab.getText();
            listView.setItems(FXCollections.observableArrayList(articlesByTime.get(tabText)));
            listView.setCellFactory(new ArticleCellFactory());
            if (tab.isSelected()) {
                listView.getSelectionModel().selectFirst();
            }
        });
    }

}
