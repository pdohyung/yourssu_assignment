package yourssu.blog.error.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import yourssu.blog.error.dto.ErrorResponse
import yourssu.blog.error.exception.custom.BusinessException
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handle(e: MethodArgumentNotValidException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val bindingResult: BindingResult = e.bindingResult
        val firstErrorMessage: String = bindingResult.fieldErrors[0].defaultMessage ?: ""

        val errorList: List<String> = bindingResult.fieldErrors.map { err -> err.defaultMessage ?: "" }
        // 첫번째 메세지만 response
        val response = ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.reasonPhrase, firstErrorMessage, request.requestURI)

        // 모든 유효성 에러 메세지 출력
        errorList.forEach { errorMsg ->
            println("MethodArgumentNotValidExceptionException = $errorMsg")
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    @ExceptionHandler(BusinessException::class)
    protected fun handleBusinessException(e: BusinessException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(e.errorCode.status)
                .body(ErrorResponse(LocalDateTime.now(), e.errorCode.status.reasonPhrase, e.errorCode.message, request.requestURI))
    }
}