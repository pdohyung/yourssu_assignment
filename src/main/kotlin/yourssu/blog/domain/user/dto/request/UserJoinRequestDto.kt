package yourssu.blog.domain.user.dto.request

import javax.validation.constraints.NotBlank

data class UserJoinRequestDto(

        @field:NotBlank(message = "이메일을 입력해주세요.")
        val email: String? = null,

        @field:NotBlank(message = "패스워드를 입력해주세요.")
        val password: String? = null,

        @field:NotBlank(message = "유저이름을 입력해주세요.")
        val username: String? = null
)