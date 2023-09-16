package yourssu.blog.error

import org.springframework.http.HttpStatus

enum class ErrorCode(status: HttpStatus, message: String) {

    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다.");

    public val status: HttpStatus = status
    public val message: String = message
}