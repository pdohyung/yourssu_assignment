package yourssu.blog.common.dto

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class ResponseDto<T>(val data : T) {
    companion object{
        fun <T> ok(data : T): ResponseEntity<T> {
            return ResponseEntity.ok(data)
        }

        fun <T> created(data : T): ResponseEntity<T> {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(data)
        }
    }
}