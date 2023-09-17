package yourssu.blog.domain.article.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import yourssu.blog.domain.article.dto.request.ArticleCreateRequestDto
import yourssu.blog.domain.article.dto.request.ArticleDeleteRequestDto
import yourssu.blog.domain.article.dto.request.ArticleUpdateRequestDto
import yourssu.blog.domain.article.entity.Article
import yourssu.blog.domain.article.repository.ArticleRepository
import yourssu.blog.domain.user.entity.User
import yourssu.blog.domain.user.repository.UserRepository
import yourssu.blog.error.ErrorCode
import yourssu.blog.error.exception.custom.BusinessException

class ArticleServiceTest : BehaviorSpec() {

    private val articleRepository = mockk<ArticleRepository>()

    private val userRepository = mockk<UserRepository>()

    private val passwordEncoder = mockk<BCryptPasswordEncoder>()

    init {

        afterContainer {
            clearAllMocks()
        }

        Given("글 작성") {
            val user = User(email = "test@test.com", password = "test-pw", username = "test")
            val request = ArticleCreateRequestDto(email = "test@test.com", password = "test-pw", title = "test", content = "test")
            val article = Article(user = user, title = request.title!!, content = request.content!!)

            When("존재하지 않는 이메일로 요청") {
                every { userRepository.findByEmail(any()) } returns null

                val result = shouldThrow<BusinessException> {
                    userRepository.findByEmail(email = request.email!!)
                            ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)
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
                    if (!passwordEncoder.matches(request.password, findUser.password))
                        throw BusinessException(ErrorCode.WRONG_PASSWORD)
                }

                Then("찾을 수 없는 사용자 예외처리") {
                    result.errorCode.message shouldBe "잘못된 비밀번호입니다."
                }

            }

            When("올바른 데이터로 요청") {
                every { userRepository.findByEmail(any()) } returns user
                every { passwordEncoder.matches(any(), any()) } returns true
                every { articleRepository.save(any()) } returns mockk()

                val findUser = userRepository.findByEmail(request.email!!)!!

                if (passwordEncoder.matches(request.password, findUser.password)) {
                    articleRepository.save(article)
                }

                Then("성공") {
                    verify(exactly = 1) { articleRepository.save(any()) }
                }
            }
        }

        Given("글 수정") {
            val user = User(email = "test@test.com", password = "test-pw", username = "test")
            val request = ArticleUpdateRequestDto(email = "test@test.com", password = "test-pw", title = "updatedTitle", content = "updatedContent")
            val article = Article(user = user, title = "title", content = "content")

            When("존재하지 않는 이메일로 요청") {
                every { userRepository.findByEmail(any()) } returns null

                val result = shouldThrow<BusinessException> {
                    userRepository.findByEmail(email = request.email!!)
                            ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)
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
                    if (!passwordEncoder.matches(request.password, findUser.password))
                        throw BusinessException(ErrorCode.WRONG_PASSWORD)
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

            When("일치하지 않는 사용자로 요청") {
                val anotherUser = User(email = "test1@test.com", password = "test-pw", username = "test")

                val result = shouldThrow<BusinessException> {
                    check(request.email == anotherUser.email) { throw BusinessException(ErrorCode.USER_NOT_MATCH) }
                }

                Then("일치하지 않은 사용자 예외처리"){
                    result.errorCode.message shouldBe "사용자가 일치하지 않습니다."
                }
            }

            When("올바른 데이터로 요청") {

                article.update(request.title!!, request.content!!)

                Then("성공") {
                    article.title shouldBe "updatedTitle"
                    article.content shouldBe "updatedContent"
                }
            }
        }

        Given("글 삭제") {
            val user = User(email = "test@test.com", password = "test-pw", username = "test")
            val request = ArticleDeleteRequestDto(email = "test@test.com", password = "test-pw")
            val article = Article(user = user, title = "title", content = "content")

            When("존재하지 않는 이메일로 요청") {
                every { userRepository.findByEmail(any()) } returns null

                val result = shouldThrow<BusinessException> {
                    userRepository.findByEmail(email = request.email!!)
                            ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)
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
                    if (!passwordEncoder.matches(request.password, findUser.password))
                        throw BusinessException(ErrorCode.WRONG_PASSWORD)
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

            When("일치하지 않는 사용자로 요청") {
                val anotherUser = User(email = "test1@test.com", password = "test-pw", username = "test")

                val result = shouldThrow<BusinessException> {
                    check(request.email == anotherUser.email) { throw BusinessException(ErrorCode.USER_NOT_MATCH) }
                }

                Then("일치하지 않은 사용자 예외처리"){
                    result.errorCode.message shouldBe "사용자가 일치하지 않습니다."
                }
            }

            When("올바른 데이터로 요청") {
                every { articleRepository.deleteById(any()) } just runs

                articleRepository.deleteById(1L)

                Then("성공") {
                    verify(exactly = 1) { articleRepository.deleteById(any()) }
                }
            }
        }
    }
}