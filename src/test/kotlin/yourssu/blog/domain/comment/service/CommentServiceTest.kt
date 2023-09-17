package yourssu.blog.domain.comment.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import yourssu.blog.domain.article.entity.Article
import yourssu.blog.domain.article.repository.ArticleRepository
import yourssu.blog.domain.comment.dto.request.CommentCreateRequestDto
import yourssu.blog.domain.comment.entity.Comment
import yourssu.blog.domain.comment.repository.CommentRepository
import yourssu.blog.domain.user.entity.User
import yourssu.blog.domain.user.repository.UserRepository
import yourssu.blog.error.ErrorCode
import yourssu.blog.error.exception.custom.BusinessException

class CommentServiceTest : BehaviorSpec() {

    private val articleRepository = mockk<ArticleRepository>()

    private val userRepository = mockk<UserRepository>()

    private val commentRepository = mockk<CommentRepository>()

    private val passwordEncoder = mockk<BCryptPasswordEncoder>()
    init {

        afterContainer {
            clearAllMocks()
        }

        Given("댓글 작성") {
            val user = User(email = "test@test.com", password = "test-pw", username = "test")
            val article = Article(user = user, title = "test", content = "test")
            val request = CommentCreateRequestDto(email = "test@test.com", password = "test-pw", content = "test")
            val comment = Comment(user = user, article = article, content = request.content!!)

            When("존재하지 않는 이메일로 요청") {
                every { userRepository.findByEmail(any()) } returns null

                val result = shouldThrow<BusinessException> {
                    userRepository.findByEmail(email = request.email!!) ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)
                }

                Then("찾을 수 없는 사용자 예외처리") {
                    result.errorCode.message shouldBe "해당 사용자를 찾을 수 없습니다."
                }

            }

            When("일치하지 않는 패스워드로 요청") {

                every { userRepository.findByEmail(any()) } returns user
                every { passwordEncoder.matches(any(), any()) } returns false

                val findUser = userRepository.findByEmail(request.email!!)!!

                val result = shouldThrow<BusinessException> {
                    if (!passwordEncoder.matches(request.password, findUser.password)) throw BusinessException(ErrorCode.WRONG_PASSWORD)
                }

                Then("찾을 수 없는 사용자 예외처리") {
                    result.errorCode.message shouldBe "잘못된 비밀번호입니다."
                }

            }

            When("존재하지 않는 게시글로 요청") {
                every { articleRepository.findByIdOrNull(any()) } returns null

                val result = shouldThrow<BusinessException> {
                    articleRepository.findByIdOrNull(1L) ?: throw BusinessException(ErrorCode.ARTICLE_NOT_FOUND)
                }

                Then("찾을 수 없는 게시글 예외처리") {
                    result.errorCode.message shouldBe "해당 게시글을 찾을 수 없습니다."
                }
            }

            When("올바른 데이터로 요청") {
                every { userRepository.findByEmail(any()) } returns user
                every { passwordEncoder.matches(any(), any()) } returns true
                every { articleRepository.findByIdOrNull(any()) } returns article
                every { commentRepository.save(any()) } returns mockk()

                val findUser = userRepository.findByEmail(request.email!!)!!

                if (passwordEncoder.matches(request.password, findUser.password)) {
                    articleRepository.findByIdOrNull(1L)
                            ?.takeIf { true }
                            ?.let { commentRepository.save(comment) }
                }

                Then("성공") {
                    verify(exactly = 1) { commentRepository.save(any()) }
                }
            }
        }
    }
}