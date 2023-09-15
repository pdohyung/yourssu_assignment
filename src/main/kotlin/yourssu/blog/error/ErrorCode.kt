package yourssu.blog.error

import org.springframework.http.HttpStatus

enum class ErrorCode(status: HttpStatus, message: String) {

    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다.");

    public val status: HttpStatus = status
    public val message: String = message
}