package yourssu.blog.domain.user.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yourssu.blog.domain.user.dto.request.UserDeleteRequestDto
import yourssu.blog.domain.user.dto.request.UserJoinRequestDto
import yourssu.blog.domain.user.dto.response.UserJoinResponseDto
import yourssu.blog.domain.user.entity.User
import yourssu.blog.domain.user.repository.UserRepository
import yourssu.blog.error.ErrorCode
import yourssu.blog.error.exception.custom.BusinessException

@Service
@Transactional(readOnly = true)
class UserService(
        private val userRepository: UserRepository,
        private val passwordEncoder: BCryptPasswordEncoder,
) {
    @Transactional
    fun join(request: UserJoinRequestDto): UserJoinResponseDto{

        val existUser = userRepository.findByEmail(request.email!!)

        if(existUser != null) throw BusinessException(ErrorCode.DUPLICATE_EMAIL)

        val user = User(
                email = request.email,
                password = passwordEncoder.encode(request.password),
                username = request.username!!
        )

        val savedUser = userRepository.save(user)

        return UserJoinResponseDto(savedUser.email, savedUser.username)
    }

    @Transactional
    fun delete(request: UserDeleteRequestDto) {
        val existUser = userRepository.findByEmail(request.email!!) ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

        existUser.takeIf { passwordEncoder.matches(request.password, it.password) } ?: throw BusinessException(ErrorCode.WRONG_PASSWORD)

        userRepository.deleteById(existUser.id!!)
    }
}