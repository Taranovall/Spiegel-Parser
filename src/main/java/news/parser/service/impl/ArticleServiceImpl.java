package news.parser.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import news.parser.entity.Article;
import news.parser.repository.ArticleRepository;
import news.parser.service.ArticleService;
import news.parser.util.Constant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    @Transactional
    public void deleteAllArticlesOlderThatOneDay() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusSeconds(Constant.ONE_DAY_IN_SECONDS);
        int count = articleRepository.deleteArticlesOlderThan(oneDayAgo);
        if (count > 0)
            log.info("Were deleted {} articles older that 1 day", count);
    }

    @Override
    @Transactional
    public void saveArticles(List<Article> articles) {
        articleRepository.saveAll(articles);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Article> getAll() {
        return articleRepository.findAll();
    }
}
