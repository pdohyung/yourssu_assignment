package yourssu.blog.domain.comment.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yourssu.blog.domain.article.repository.ArticleRepository
import yourssu.blog.domain.comment.dto.request.CommentCreateRequestDto
import yourssu.blog.domain.comment.dto.request.CommentDeleteRequestDto
import yourssu.blog.domain.comment.dto.request.CommentUpdateRequestDto
import yourssu.blog.domain.comment.dto.response.CommentCreateResponseDto
import yourssu.blog.domain.comment.dto.response.CommentUpdateResponseDto
import yourssu.blog.domain.comment.entity.Comment
import yourssu.blog.domain.comment.repository.CommentRepository
import yourssu.blog.domain.user.repository.UserRepository
import yourssu.blog.error.ErrorCode
import yourssu.blog.error.exception.custom.BusinessException

@Service
@Transactional(readOnly = true)
class CommentService(
        private val commentRepository: CommentRepository,
        private val userRepository: UserRepository,
        private val articleRepository: ArticleRepository,
        private val passwordEncoder: BCryptPasswordEncoder
) {

    @Transactional
    fun create(articleId: Long, request: CommentCreateRequestDto): CommentCreateResponseDto {

        val existUser = userRepository.findByEmail(request.email!!) ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

        existUser.takeIf { passwordEncoder.matches(request.password, it.password) } ?: throw BusinessException(ErrorCode.WRONG_PASSWORD)

        val existArticle = articleRepository.findByIdOrNull(articleId) ?: throw BusinessException(ErrorCode.ARTICLE_NOT_FOUND)

        val savedComment = Comment(
                content = request.content!!,
                user = existUser,
                article = existArticle
        ).let { commentRepository.save(it) }

        return CommentCreateResponseDto(savedComment.id, existUser.email, savedComment.content)
    }

    @Transactional
    fun update(articleId: Long, commentId: Long, request: CommentUpdateRequestDto): CommentUpdateResponseDto {

        val existUser = userRepository.findByEmail(request.email!!) ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

        existUser.takeIf { passwordEncoder.matches(request.password, it.password) } ?: throw BusinessException(ErrorCode.WRONG_PASSWORD)

        val existArticle = articleRepository.findByIdOrNull(articleId) ?: throw BusinessException(ErrorCode.ARTICLE_NOT_FOUND)

        val findComment = commentRepository.findByIdOrNull(commentId) ?: throw BusinessException(ErrorCode.COMMENT_NOT_FOUND)

        when {
            findComment.user.email != request.email -> throw BusinessException(ErrorCode.USER_NOT_MATCH)
            findComment.article.id != existArticle.id -> throw BusinessException(ErrorCode.ARTICLE_NOT_MATCH)
            else -> findComment.update(request.content!!)
        }

        return CommentUpdateResponseDto(findComment.id, existUser.email, findComment.content)
    }

    @Transactional
    fun delete(articleId: Long, commentId: Long, request: CommentDeleteRequestDto) {

        val existUser = userRepository.findByEmail(request.email!!) ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

        existUser.takeIf { passwordEncoder.matches(request.password, it.password) } ?: throw BusinessException(ErrorCode.WRONG_PASSWORD)

        val existArticle = articleRepository.findByIdOrNull(articleId) ?: throw BusinessException(ErrorCode.ARTICLE_NOT_FOUND)

        val findComment = commentRepository.findByIdOrNull(commentId) ?: throw BusinessException(ErrorCode.COMMENT_NOT_FOUND)

        when {
            findComment.user.email != request.email -> throw BusinessException(ErrorCode.USER_NOT_MATCH)
            findComment.article.id != existArticle.id -> throw BusinessException(ErrorCode.ARTICLE_NOT_MATCH)
            else -> commentRepository.deleteById(findComment.id!!)
        }
    }
}