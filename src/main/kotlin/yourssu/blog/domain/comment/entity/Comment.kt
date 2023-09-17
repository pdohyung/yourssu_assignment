package yourssu.blog.domain.comment.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import yourssu.blog.common.entity.BaseTimeEntity
import yourssu.blog.domain.article.entity.Article
import yourssu.blog.domain.user.entity.User
import javax.persistence.*

@Entity
@Table(name = "comment")
class Comment (

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "comment_id")
        val id : Long? = null,

        @Column(nullable = false)
        val content : String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "article_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val article : Article,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        @OnDelete(action = OnDeleteAction.CASCADE)
        val user: User

) : BaseTimeEntity()