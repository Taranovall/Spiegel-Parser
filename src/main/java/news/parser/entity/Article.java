package news.parser.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@RequiredArgsConstructor
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String headline;
    @Column(length = 512)
    private String description;
    private LocalDateTime publicationTime;

    public Article(String headline, String description, LocalDateTime publicationTime) {
        this.headline = headline;
        this.description = description;
        this.publicationTime = publicationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (!Objects.equals(headline, article.headline)) return false;
        if (!Objects.equals(description, article.description)) return false;
        return Objects.equals(publicationTime, article.publicationTime);
    }

    @Override
    public int hashCode() {
        int result = headline != null ? headline.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (publicationTime != null ? publicationTime.hashCode() : 0);
        return result;
    }
}
