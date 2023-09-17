package yourssu.blog.domain.comment.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import yourssu.blog.domain.comment.dto.request.CommentCreateRequestDto
import yourssu.blog.domain.comment.dto.request.CommentDeleteRequestDto
import yourssu.blog.domain.comment.dto.request.CommentUpdateRequestDto
import yourssu.blog.domain.comment.dto.response.CommentCreateResponseDto
import yourssu.blog.domain.comment.dto.response.CommentUpdateResponseDto
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

                result.andExpect(status().isCreated)
                        .andExpect(jsonPath("$.commentId").value(1L))
                        .andExpect(jsonPath("$.email").value("test@test.com"))
                        .andExpect(jsonPath("$.content").value("test"))
                        .andDo(print())
            }
        }

        describe("댓글 수정 요청"){
            it("CommentUpdateResponseDto와 200 status를 반환한다."){

                val request = CommentUpdateRequestDto(email = "test@test.com", password = "test-pw", content = "test")
                val response = CommentUpdateResponseDto(commentId = 1L, email = "test@test.com", content = "test")

                every { commentService.update(any(), any(), any()) } returns response

                val result = mockMvc.perform(MockMvcRequestBuilders.patch("/api/comments/1/1",)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )

                result.andExpect(status().isOk)
                        .andExpect(jsonPath("$.commentId").value(1L))
                        .andExpect(jsonPath("$.email").value("test@test.com"))
                        .andExpect(jsonPath("$.content").value("test"))
                        .andDo(print())
            }
        }

        describe("댓글 삭제 요청"){
            it("200 status를 반환한다."){

                val request = CommentDeleteRequestDto(email = "test@test.com", password = "test-pw")

                every { commentService.delete(any(), any(), any()) } just runs

                val result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )

                result.andExpect(status().isOk)
                        .andDo(print())
            }
        }
    }
}