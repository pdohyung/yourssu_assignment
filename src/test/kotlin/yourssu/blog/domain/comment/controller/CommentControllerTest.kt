package yourssu.blog.domain.comment.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import yourssu.blog.domain.comment.dto.request.CommentCreateRequestDto
import yourssu.blog.domain.comment.dto.response.CommentCreateResponseDto
import yourssu.blog.domain.comment.service.CommentService

class CommentControllerTest : DescribeSpec() {

    @MockK
    private lateinit var commentService: CommentService

    private lateinit var mockMvc: MockMvc

    private val objectMapper = ObjectMapper()

    init {
        MockKAnnotations.init(this)
        mockMvc = MockMvcBuilders.standaloneSetup(CommentController(commentService)).build()

        describe("댓글 작성 요청"){
            it("CommentCreateResponseDto와 201 status를 반환한다."){

                val request = CommentCreateRequestDto(email = "test@test.com", password = "test-pw", content = "test")
                val response = CommentCreateResponseDto(commentId = 1L, email = "test@test.com", content = "test")

                every { commentService.create(any(), any()) } returns response

                val result = mockMvc.perform(MockMvcRequestBuilders.post("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )

                result.andExpect(MockMvcResultMatchers.status().isCreated)
                        .andExpect(MockMvcResultMatchers.jsonPath("$.commentId").value(1L))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@test.com"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("test"))
                        .andDo(MockMvcResultHandlers.print())
            }
        }
    }
}