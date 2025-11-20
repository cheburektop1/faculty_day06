package ru.tbank.education.school.lesson2

import java.util.Date

//класс для контента
abstract class Content(
    val id: Int,
    val author: User,
    val createdAt: Date = Date()
) {
    abstract fun display()

    // геттер
    val ageInMinutes: Long
        get() = (Date().time - createdAt.time) / (1000 * 60)
}

//инфа о лайках
data class Like(
    val user: User,
    val timestamp: Date = Date()
)

//тип контента
sealed class ContentType {
    object Post : ContentType()
    object Photo : ContentType()
    object Video : ContentType()
}

// пост
class Post(
    id: Int,
    author: User,
    val text: String,
    val contentType: ContentType = ContentType.Post
) : Content(id, author) {
    private val _likes = mutableListOf<Like>()
    val likes: List<Like>
        get() = _likes.toList()

    // сеттер
    var isDeleted: Boolean = false
        private set

    fun addLike(user: User) {
        if (!isDeleted && user.isActive) {
            _likes.add(Like(user))
        }
    }

    override fun display() {
        println("""
            post #$id from ${author.username}
            Text: $text
            like: ${likes.size}
            created: $createdAt
        """.trimIndent())
    }

    fun delete() {
        isDeleted = true
    }
}

// user
open class User(
    val id: Int,
    val username: String,
    private val email: String,
    private var password: String
) {
    protected val _friends = mutableListOf<User>()
    protected val _posts = mutableListOf<Post>()

    val friends: List<User>
        get() = _friends.toList()

    val posts: List<Post>
        get() = _posts.filter { !it.isDeleted }

    // геттер
    val isActive: Boolean
        get() = posts.isNotEmpty() || friends.isNotEmpty()

    constructor(id: Int, username: String) : this(id, username, "", "")

    fun addFriend(user: User) {
        if (user != this && user !in _friends) {
            _friends.add(user)
            user._friends.add(this)
        }
    }

    fun createPost(text: String): Post {
        val post = Post(_posts.size + 1, this, text)
        _posts.add(post)
        return post
    }

    open fun displayProfile() {
        println("""
            user profile: $username
            friends: ${friends.size}
            posts: ${posts.size}
            activiti: ${if (isActive) "Yes" else "No"}
        """.trimIndent())
    }
}

// модер
class Moderator(
    id: Int,
    username: String,
    email: String,
    password: String
) : User(id, username, email, password) {

    fun deletePost(post: Post) {
        post.delete()
        println("Moder $username delete post #${post.id}")
    }

    override fun displayProfile() {
        super.displayProfile()
        println("role: moder")
    }
}

data class NetworkStats(
    val totalUsers: Int,
    val totalPosts: Int,
    val activeUsers: Int
)

fun main() {
    // создание пользователей
    val alice = User(1, "Alice", "alice@mail.com", "pass123")
    val bob = User(2, "Bob")
    val moderator = Moderator(3, "Eve", "eve@mail.com", "modpass")

    // добавление друзей
    alice.addFriend(bob)
    alice.addFriend(moderator)

    // посты
    val post1 = alice.createPost("my first post!")
    val post2 = bob.createPost("hi here.")

    // лайки
    post1.addLike(bob)
    post1.addLike(moderator)
    post2.addLike(alice)

    // Модератор удаляет пост
    moderator.deletePost(post2)

    // Вывод
    alice.displayProfile()
}