package yourssu.blog.domain.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import yourssu.blog.domain.user.dto.request.UserJoinRequestDto
import yourssu.blog.domain.user.dto.response.UserJoinResponseDto
import yourssu.blog.domain.user.service.UserService

class UserControllerTest : DescribeSpec() {

    @MockK
    private lateinit var userService: UserService

    private lateinit var mockMvc: MockMvc

    private val objectMapper = ObjectMapper()

    init {
        MockKAnnotations.init(this)
        mockMvc = MockMvcBuilders.standaloneSetup(UserController(userService)).build()

        describe("회원가입 요청 성공 시") {
            it("UserJoinResponseDto와 201 status 반환한다.") {

                val request = UserJoinRequestDto(email = "test@test.com", password = "test-pw", username = "test")
                val response = UserJoinResponseDto(email = "test@test.com", username = "test")

                every { userService.join(any()) } returns response

                val result = mockMvc.perform(MockMvcRequestBuilders.post("/api/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )

                result.andExpect(status().isCreated)
                        .andExpect(jsonPath("$.email").value("test@test.com"))
                        .andExpect(jsonPath("$.username").value("test"))
                        .andDo(print())
            }
        }
    }
}