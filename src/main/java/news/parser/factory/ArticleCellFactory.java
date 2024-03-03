package news.parser.factory;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import news.parser.entity.Article;
import news.parser.util.Constant;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static news.parser.util.Constant.HEADLINE_FONT_STYLE;

public class ArticleCellFactory implements Callback<ListView<Article>, ListCell<Article>> {

    @Override
    public ListCell<Article> call(ListView<Article> param) {
        return new ListCell<>() {
            @Override
            protected void updateItem(Article article, boolean empty) {
                super.updateItem(article, empty);
                if (empty || article == null) {
                    setText(null);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.PUBLICATION_TIME_PATTERN, Locale.ENGLISH);
                    Label headlineLabel = new Label(article.getHeadline());
                    Label descriptionLabel = new Label(article.getDescription());
                    Label timeLabel = new Label(article.getPublicationTime().format(formatter));

                    headlineLabel.setStyle(HEADLINE_FONT_STYLE);
                    descriptionLabel.setWrapText(true);

                    VBox vbox = new VBox(5);
                    vbox.getChildren().addAll(headlineLabel, descriptionLabel, timeLabel);

                    setGraphic(vbox);
                }
            }
        };
    }
}
