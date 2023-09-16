package yourssu.blog.domain.article.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import yourssu.blog.domain.article.dto.request.ArticleCreateRequestDto
import yourssu.blog.domain.article.dto.response.ArticleCreateResponseDto
import yourssu.blog.domain.article.service.ArticleService

class ArticleControllerTest : DescribeSpec() {

    @MockK
    lateinit var articleService: ArticleService

    private var mockMvc: MockMvc

    private val objectMapper = ObjectMapper()

    init {
        MockKAnnotations.init(this)
        mockMvc = MockMvcBuilders.standaloneSetup(ArticleController(articleService)).build()

        describe("글 작성 요청 성공 시") {
            it("responseDto와 201 status 반환한다.") {

                val request = ArticleCreateRequestDto(email = "test@test.com", password = "test-pw", title = "test", content = "test")
                val response = ArticleCreateResponseDto(articleId = 1L, email = "test@test.com", title = "test", content = "test")

                every { articleService.create(any()) } returns response

                val result = mockMvc.perform(MockMvcRequestBuilders.post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )

                result.andExpect(status().isCreated)
                        .andExpect(jsonPath("$.articleId").value(1L))
                        .andExpect(jsonPath("$.email").value("test@test.com"))
                        .andExpect(jsonPath("$.title").value("test"))
                        .andExpect(jsonPath("$.content").value("test"))
                        .andDo(print())
            }
        }
    }
}