package news.parser.service;

import news.parser.entity.Article;

import java.util.List;

public interface ArticleService {

    void deleteAllArticlesOlderThatOneDay();

    void saveArticles(List<Article> articles);

    List<Article> getAll();
}
