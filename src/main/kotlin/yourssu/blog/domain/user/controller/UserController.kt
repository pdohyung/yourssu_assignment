package yourssu.blog.domain.user.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yourssu.blog.common.dto.ResponseDto
import yourssu.blog.domain.user.dto.request.UserDeleteRequestDto
import yourssu.blog.domain.user.dto.request.UserJoinRequestDto
import yourssu.blog.domain.user.dto.response.UserJoinResponseDto
import yourssu.blog.domain.user.service.UserService
import javax.validation.Valid

@RestController
@RequestMapping("/api/user")
class UserController(
        private val userService: UserService
) {
    @PostMapping("/join")
    fun join(@RequestBody @Valid request: UserJoinRequestDto): ResponseEntity<UserJoinResponseDto>{
        val response = userService.join(request)
        return ResponseDto.created(response)
    }

    @DeleteMapping("/delete")
    fun delete(@RequestBody @Valid request: UserDeleteRequestDto): ResponseEntity<Void>{
        userService.delete(request)
        return ResponseDto.noContent()
    }
}