package yourssu.blog.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import yourssu.blog.domain.user.entity.User

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}