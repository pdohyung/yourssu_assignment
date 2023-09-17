package yourssu.blog.domain.comment.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yourssu.blog.common.dto.ResponseDto
import yourssu.blog.domain.comment.dto.request.CommentCreateRequestDto
import yourssu.blog.domain.comment.dto.request.CommentDeleteRequestDto
import yourssu.blog.domain.comment.dto.request.CommentUpdateRequestDto
import yourssu.blog.domain.comment.dto.response.CommentCreateResponseDto
import yourssu.blog.domain.comment.dto.response.CommentUpdateResponseDto
import yourssu.blog.domain.comment.service.CommentService
import javax.validation.Valid

@RestController
@RequestMapping("/api/comments")
class CommentController(
        private val commentService: CommentService
) {
    @PostMapping("/{articleId}")
    fun create(@PathVariable articleId: Long, @RequestBody @Valid request: CommentCreateRequestDto): ResponseEntity<CommentCreateResponseDto>{
        val response = commentService.create(articleId, request)
        return ResponseDto.created(response)
    }

    @PatchMapping("/{articleId}/{commentId}")
    fun update(@PathVariable articleId: Long,
               @PathVariable commentId: Long,
               @RequestBody @Valid request: CommentUpdateRequestDto): ResponseEntity<CommentUpdateResponseDto> {
        val response = commentService.update(articleId, commentId, request)
        return ResponseDto.ok(response)
    }

    @DeleteMapping("/{articleId}/{commentId}")
    fun delete(@PathVariable articleId: Long,
               @PathVariable commentId: Long,
               @RequestBody @Valid request: CommentDeleteRequestDto): ResponseEntity<Void>{
        commentService.delete(articleId, commentId, request)
        return ResponseDto.noContent()
    }

}