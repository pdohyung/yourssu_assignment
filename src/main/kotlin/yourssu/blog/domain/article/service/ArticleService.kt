package yourssu.blog.domain.article.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yourssu.blog.domain.article.dto.request.ArticleCreateRequestDto
import yourssu.blog.domain.article.dto.request.ArticleDeleteRequestDto
import yourssu.blog.domain.article.dto.request.ArticleUpdateRequestDto
import yourssu.blog.domain.article.dto.response.ArticleCreateResponseDto
import yourssu.blog.domain.article.dto.response.ArticleUpdateResponseDto
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

    @Transactional
    fun update(articleId: Long, request: ArticleUpdateRequestDto): ArticleUpdateResponseDto {

        val existUser = userRepository.findByEmail(request.email!!) ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

        existUser.takeIf { passwordEncoder.matches(request.password, it.password) } ?: throw BusinessException(ErrorCode.WRONG_PASSWORD)

        val findArticle = articleRepository.findByIdOrNull(articleId) ?: throw BusinessException(ErrorCode.ARTICLE_NOT_FOUND)

        if(findArticle.user.email != request.email){
            throw BusinessException(ErrorCode.USER_NOT_MATCH)
        } else{
            findArticle.update(request.title!!, request.content!!)
        }

        return ArticleUpdateResponseDto(findArticle.id, existUser.email, findArticle.title, findArticle.content)
    }

    @Transactional
    fun delete(articleId: Long, request: ArticleDeleteRequestDto) {

        val existUser = userRepository.findByEmail(request.email!!) ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

        existUser.takeIf { passwordEncoder.matches(request.password, it.password) } ?: throw BusinessException(ErrorCode.WRONG_PASSWORD)

        val findArticle = articleRepository.findByIdOrNull(articleId) ?: throw BusinessException(ErrorCode.ARTICLE_NOT_FOUND)

        if(findArticle.user.email != request.email){
            throw BusinessException(ErrorCode.USER_NOT_MATCH)
        } else{
            articleRepository.deleteById(findArticle.id!!)
        }
    }
}