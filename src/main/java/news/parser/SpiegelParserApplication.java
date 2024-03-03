package news.parser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import news.parser.controller.MainPageController;
import news.parser.service.ArticleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import static news.parser.util.Constant.JAVA_FX_APPLICATION_TITLE;
import static news.parser.util.Constant.PATH_TO_MAIN_PAGE_FXML;

@SpringBootApplication
public class SpiegelParserApplication extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		ApplicationContext context = SpringApplication.run(SpiegelParserApplication.class);

		ArticleService articleService = context.getBean(ArticleService.class);

		FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_TO_MAIN_PAGE_FXML));
		loader.setControllerFactory(clazz -> new MainPageController(articleService));

		Parent root = loader.load();

		stage.setScene(new Scene(root, 300, 200));
		stage.setTitle(JAVA_FX_APPLICATION_TITLE);
		stage.show();
	}
}
