package yourssu.blog.domain.comment.dto.response

data class CommentCreateResponseDto(
        val commentId: Long? = null,
        val email: String,
        val content: String
)