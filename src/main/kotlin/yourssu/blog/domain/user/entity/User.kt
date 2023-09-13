package yourssu.blog.domain.user.entity

import yourssu.blog.common.entity.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "users")
class User (

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id", nullable = false)
        val id : Long? = null,

        @Column(name = "email", nullable = false)
        val email: String,

        @Column(name = "password", nullable = false)
        val password: String,

        @Column(name = "username", nullable = false)
        val username: String

) : BaseTimeEntity()