package yourssu.blog.error.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import yourssu.blog.error.dto.ErrorResponse
import yourssu.blog.error.exception.custom.BusinessException

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handle(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val bindingResult: BindingResult = e.bindingResult
        val firstErrorMessage: String = bindingResult.fieldErrors[0].defaultMessage ?: ""

        val errorList: List<String> = bindingResult.fieldErrors.map { err -> err.defaultMessage ?: "" }
        // 첫번째 메세지만 response
        val response = ErrorResponse(HttpStatus.BAD_REQUEST.value(), firstErrorMessage)

        // 모든 유효성 에러 메세지 출력
        errorList.forEach { errorMsg ->
            println("MethodArgumentNotValidExceptionException = $errorMsg")
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    @ExceptionHandler(BusinessException::class)
    protected fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(e.errorCode.status)
                .body(ErrorResponse(e.errorCode.status.value(), e.errorCode.message))
    }
}