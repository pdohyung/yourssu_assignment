package yourssu.blog.domain.article.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import yourssu.blog.common.entity.BaseTimeEntity
import yourssu.blog.domain.user.entity.User
import javax.persistence.*

@Entity
@Table(name = "article")
class Article (

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "article_id", nullable = false)
        val id : Long? = null,

        val content : String,

        val title : String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        @OnDelete(action = OnDeleteAction.CASCADE)
        val user : User

) : BaseTimeEntity()