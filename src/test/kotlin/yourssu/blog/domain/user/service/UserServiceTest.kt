package yourssu.blog.domain.user.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import yourssu.blog.domain.user.entity.User
import yourssu.blog.domain.user.repository.UserRepository
import yourssu.blog.error.ErrorCode
import yourssu.blog.error.exception.custom.BusinessException

class UserServiceTest : BehaviorSpec(){

    init {
        val userRepository = mockk<UserRepository>()

        afterContainer {
            clearAllMocks()
        }

        Given("사용자가 회원가입"){
            val user = User(
                    email = "test@test.com",
                    password = "test-pw",
                    username = "test"
            )

            When("중복된 이메일로 회원가입 요청"){
                every { userRepository.findByEmail(any()) } returns mockk()
                val result = shouldThrow<BusinessException> {
                    val user = userRepository.findByEmail( email = "test@test.com")

                    if(user != null) throw BusinessException(ErrorCode.DUPLICATE_EMAIL)
                }
                Then("이메일 중복 예외처리") {
                    result.errorCode.message shouldBe "중복된 이메일입니다."
                }
            }

            When("새로운 이메일로 회원가입 요청"){
                every { userRepository.findByEmail(any()) } returns null
                every { userRepository.save(any()) } returns user

                val findUser = userRepository.findByEmail( email = "test@test.com")
                if(findUser == null) userRepository.save(user)

                Then("회원가입 성공"){
                    verify(exactly = 1) { userRepository.save(user) }
                }
            }

        }
    }
}