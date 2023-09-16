package yourssu.blog.domain.article.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yourssu.blog.common.dto.ResponseDto
import yourssu.blog.domain.article.dto.request.ArticleCreateRequestDto
import yourssu.blog.domain.article.dto.response.ArticleCreateResponseDto
import yourssu.blog.domain.article.service.ArticleService
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class ArticleController(
        private val articleService: ArticleService
) {
    @PostMapping("/articles")
    fun create(@RequestBody @Valid request: ArticleCreateRequestDto): ResponseEntity<ArticleCreateResponseDto>{
        val response = articleService.create(request)
        return ResponseDto.created(response)
    }
}