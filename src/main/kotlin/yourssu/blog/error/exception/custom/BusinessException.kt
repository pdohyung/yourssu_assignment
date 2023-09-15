package yourssu.blog.error.exception.custom

import yourssu.blog.error.ErrorCode

class BusinessException(errorCode: ErrorCode): RuntimeException() {
    public val errorCode: ErrorCode = errorCode;
}