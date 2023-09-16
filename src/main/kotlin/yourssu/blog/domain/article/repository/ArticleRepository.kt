package yourssu.blog.domain.article.repository

import org.springframework.data.jpa.repository.JpaRepository
import yourssu.blog.domain.article.entity.Article

interface ArticleRepository : JpaRepository<Article, Long> {
}