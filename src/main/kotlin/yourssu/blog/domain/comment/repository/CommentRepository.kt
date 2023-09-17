package yourssu.blog.domain.comment.repository

import org.springframework.data.jpa.repository.JpaRepository
import yourssu.blog.domain.comment.entity.Comment

interface CommentRepository: JpaRepository<Comment, Long> {
}