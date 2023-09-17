package yourssu.blog.domain.article.dto.response

data class ArticleUpdateResponseDto (
        val articleId: Long? = null,
        val email: String,
        val title: String,
        val content: String
)