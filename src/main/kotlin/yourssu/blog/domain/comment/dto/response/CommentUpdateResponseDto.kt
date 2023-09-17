package yourssu.blog.domain.comment.dto.response

data class CommentUpdateResponseDto(
        val commentId: Long? = null,
        val email: String,
        val content: String
)