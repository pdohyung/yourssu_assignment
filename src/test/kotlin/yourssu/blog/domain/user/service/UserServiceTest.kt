package yourssu.blog.domain.user.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import yourssu.blog.domain.user.dto.request.UserDeleteRequestDto
import yourssu.blog.domain.user.dto.request.UserJoinRequestDto
import yourssu.blog.domain.user.entity.User
import yourssu.blog.domain.user.repository.UserRepository
import yourssu.blog.error.ErrorCode
import yourssu.blog.error.exception.custom.BusinessException

class UserServiceTest : BehaviorSpec(){

    private val userRepository = mockk<UserRepository>()

    private val passwordEncoder = mockk<BCryptPasswordEncoder>()

    init {

        afterContainer {
            clearAllMocks()
        }

        Given("사용자가 회원가입"){
            val request = UserJoinRequestDto(email = "test@test.com", "test-pw", "test")
            val user = User(
                    email = request.email!!,
                    password = request.password!!,
                    username = request.username!!
            )

            When("중복된 이메일로 회원가입 요청"){
                every { userRepository.findByEmail(any()) } returns mockk()
                val result = shouldThrow<BusinessException> {
                    val findUser = userRepository.findByEmail( email = request.email!!)

                    if(findUser != null) throw BusinessException(ErrorCode.DUPLICATE_EMAIL)
                }
                Then("이메일 중복 예외처리") {
                    result.errorCode.message shouldBe "중복된 이메일입니다."
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

            When("새로운 이메일로 회원가입 요청"){
                every { userRepository.findByEmail(any()) } returns null
                every { userRepository.save(any()) } returns user

                userRepository.findByEmail( email = request.email!!) ?: userRepository.save(user)

                Then("회원가입 성공"){
                    verify(exactly = 1) { userRepository.save(user) }
                }
            }
        }

        Given("사용자가 회원 탈퇴"){
            val request = UserDeleteRequestDto(email = "test@test.com", "test-pw")
            val user = User(
                    email = request.email!!,
                    password = request.password!!,
                    username = "test"
            )

            When("중복된 이메일로 회원가입 요청"){
                every { userRepository.findByEmail(any()) } returns mockk()
                val result = shouldThrow<BusinessException> {
                    val findUser = userRepository.findByEmail( email = request.email!!)

                    if(findUser != null) throw BusinessException(ErrorCode.DUPLICATE_EMAIL)
                }
                Then("이메일 중복 예외처리") {
                    result.errorCode.message shouldBe "중복된 이메일입니다."
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

            When("올바른 요청"){
                every { userRepository.deleteById(any()) } just runs

                userRepository.deleteById(1L)

                Then("회원 탈퇴 성공"){
                    verify(exactly = 1) { userRepository.deleteById(any()) }
                }
            }
        }
    }
}