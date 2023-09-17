package yourssu.blog.domain.article.dto.request

import javax.validation.constraints.NotBlank

data class ArticleDeleteRequestDto(

        @field:NotBlank(message = "이메일을 입력해주세요.")
        val email: String?,

        @field:NotBlank(message = "패스워드를 입력해주세요.")
        val password: String?,
)