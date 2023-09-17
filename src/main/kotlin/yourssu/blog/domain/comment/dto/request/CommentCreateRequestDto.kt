package yourssu.blog.domain.comment.dto.request

import javax.validation.constraints.NotBlank

data class CommentCreateRequestDto (

        @field:NotBlank(message = "이메일을 입력해주세요.")
        val email: String?,

        @field:NotBlank(message = "패스워드를 입력해주세요.")
        val password: String?,

        @field:NotBlank(message = "내용을 입력해주세요.")
        val content: String?
)