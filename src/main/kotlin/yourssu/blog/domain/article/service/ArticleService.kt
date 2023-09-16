package yourssu.blog.domain.article.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yourssu.blog.domain.article.dto.request.ArticleCreateRequestDto
import yourssu.blog.domain.article.dto.response.ArticleCreateResponseDto
import yourssu.blog.domain.article.entity.Article
import yourssu.blog.domain.article.repository.ArticleRepository
import yourssu.blog.domain.user.repository.UserRepository
import yourssu.blog.error.ErrorCode
import yourssu.blog.error.exception.custom.BusinessException

@Service
@Transactional(readOnly = true)
class ArticleService(
        private val articleRepository: ArticleRepository,
        private val userRepository: UserRepository,
        private val passwordEncoder: BCryptPasswordEncoder
) {
    @Transactional
    fun create(request: ArticleCreateRequestDto): ArticleCreateResponseDto {

        val existUser = userRepository.findByEmail(request.email!!) ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

        if(!passwordEncoder.matches(request.password, existUser.password)){
            throw BusinessException(ErrorCode.WRONG_PASSWORD)
        }

        val article = Article(
                user = existUser,
                title = request.title!!,
                content = request.content!!
        )

        val savedArticle = articleRepository.save(article)

        return ArticleCreateResponseDto(savedArticle.id, request.email, savedArticle.title, savedArticle.content)
    }
}