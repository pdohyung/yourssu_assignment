package yourssu.blog.domain.article.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yourssu.blog.common.dto.ResponseDto
import yourssu.blog.domain.article.dto.request.ArticleCreateRequestDto
import yourssu.blog.domain.article.dto.request.ArticleDeleteRequestDto
import yourssu.blog.domain.article.dto.request.ArticleUpdateRequestDto
import yourssu.blog.domain.article.dto.response.ArticleCreateResponseDto
import yourssu.blog.domain.article.dto.response.ArticleUpdateResponseDto
import yourssu.blog.domain.article.service.ArticleService
import javax.validation.Valid

@RestController
@RequestMapping("/api/articles")
class ArticleController(
        private val articleService: ArticleService
) {
    @PostMapping
    fun create(@RequestBody @Valid request: ArticleCreateRequestDto): ResponseEntity<ArticleCreateResponseDto>{
        val response = articleService.create(request)
        return ResponseDto.created(response)
    }

    @PatchMapping("/{articleId}")
    fun update(@PathVariable articleId: Long, @RequestBody @Valid request: ArticleUpdateRequestDto): ResponseEntity<ArticleUpdateResponseDto>{
        val response = articleService.update(articleId, request)
        return ResponseDto.ok(response)
    }

    @DeleteMapping("/{articleId}")
    fun delete(@PathVariable articleId: Long, @RequestBody @Valid request: ArticleDeleteRequestDto): ResponseEntity<Void>{
        articleService.delete(articleId, request)
        return ResponseDto.noContent()
    }
}