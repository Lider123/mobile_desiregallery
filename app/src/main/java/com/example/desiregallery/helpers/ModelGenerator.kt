package com.example.desiregallery.helpers

import com.example.desiregallery.models.Comment
import com.example.desiregallery.models.Post
import com.example.desiregallery.models.User

import java.util.ArrayList


object ModelGenerator {
    private val posts = initPosts()
    private val users = initUsers()

    private fun initPosts(): List<Post> {
        val posts = ArrayList<Post>()

        var p = Post()
        p.id = "1"
        p.setImageUrl("https://via.placeholder.com/600/92c952")
        p.rating = 4.5f
        p.numOfRates = 2
        p.comments = emptyList()
        posts.add(p)

        p = Post()
        p.id = "2"
        p.setImageUrl("https://via.placeholder.com/600/771796")
        p.rating = 4.6f
        p.numOfRates = 5
        p.comments = listOf(
            Comment("good", 1),
            Comment("nice", 2)
        )
        posts.add(p)

        p = Post()
        p.id = "3"
        p.setImageUrl("https://via.placeholder.com/600/24f355")
        p.rating = 3.1f
        p.numOfRates = 10
        p.comments = listOf(
            Comment("bad", 1),
            Comment("very bad", 2),
            Comment("not bad", 3),
            Comment("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.", 4),
            Comment("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.", 5),
            Comment("But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness.", 6)
        )

        posts.add(p)

        p = Post()
        p.id = "4"
        p.setImageUrl("https://via.placeholder.com/600/d32776")
        p.rating = 0.0f
        p.numOfRates = 0
        p.comments = listOf(
            Comment("very well", 123)
        )
        posts.add(p)

        p = Post()
        p.id = "5"
        p.setImageUrl("https://loremflickr.com/320/240/cat")
        p.rating = 5.0f
        p.numOfRates = 1
        p.comments = listOf(
            Comment("so cute", 11),
            Comment("love cats", 13)
        )
        posts.add(p)

        p = Post()
        p.id = "6"
        p.setImageUrl("https://loremflickr.com/240/320/cat")
        p.rating = 4.9f
        p.numOfRates = 10
        p.comments = listOf(
            Comment("love it", 42),
            Comment("another cat", 44)
        )
        posts.add(p)

        p = Post()
        p.id = "7"
        p.setImageUrl("https://loremflickr.com/100/100/cat")
        p.rating = 3.9f
        p.numOfRates = 10
        p.comments = emptyList()
        posts.add(p)

        p = Post()
        p.id = "8"
        p.setImageUrl("https://loremflickr.com/150/100/cat")
        p.rating = 3.8f
        p.numOfRates = 5
        p.comments = emptyList()
        posts.add(p)

        p = Post()
        p.id = "9"
        p.setImageUrl("https://loremflickr.com/100/150/cat")
        p.rating = 3.3f
        p.numOfRates = 10
        p.comments = emptyList()
        posts.add(p)

        return posts
    }

    private fun initUsers(): List<User> {
        val users = ArrayList<User>()

        var u = User("login1", "pass1")
        u.email = "login1@example.com"
        u.gender = "male"
        u.birthday = "01.01.1970"
        users.add(u)

        u = User("login2", "pass2")
        u.email = "login2@example.com"
        u.gender = "female"
        u.birthday = "21.12.2012"
        users.add(u)

        return users
    }

    fun getUser(login: String): User? = users.findLast{ it.getLogin() == login }

    fun getPost(id: String): Post? = posts.findLast{ it.id == id }
}
