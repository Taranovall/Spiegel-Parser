package news.parser.repository;

import news.parser.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Modifying
    @Query("DELETE FROM Article a WHERE a.publicationTime < :cutoffDate")
    int deleteArticlesOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);
}
