package com.example.desiregallery.helpers

import com.example.desiregallery.models.Post
import com.example.desiregallery.models.User

import java.util.ArrayList

object ModelGenerator {
    private val posts = initPosts()
    private val users = initUsers()

    private fun initPosts(): List<Post> {
        val posts = mutableListOf<Post>()

        var p = Post()
        p.setId("1")
        p.setImageUrl("https://via.placeholder.com/600/92c952")
        p.setRating(4.5f)
        p.setNumOfRates(2)
        p.setComments(ArrayList())
        posts.add(p)

        p = Post()
        p.setId("2")
        p.setImageUrl("https://via.placeholder.com/600/771796")
        p.setRating(4.6f)
        p.setNumOfRates(5)
        p.setComments(arrayListOf("good", "nice"))
        posts.add(p)

        p = Post()
        p.setId("3")
        p.setImageUrl("https://via.placeholder.com/600/24f355")
        p.setRating(3.1f)
        p.setNumOfRates(10)
        p.setComments(
            arrayListOf(
                "bad",
                "very bad",
                "not bad",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.",
                "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness."
            )
        )
        posts.add(p)

        p = Post()
        p.setId("4")
        p.setImageUrl("https://via.placeholder.com/600/d32776")
        p.setRating(0.0f)
        p.setNumOfRates(0)
        p.setComments(arrayListOf("very well"))
        posts.add(p)

        p = Post()
        p.setId("5")
        p.setImageUrl("https://loremflickr.com/320/240/cat")
        p.setRating(5.0f)
        p.setNumOfRates(1)
        p.setComments(arrayListOf("so cute", "love cats"))
        posts.add(p)

        p = Post()
        p.setId("6")
        p.setImageUrl("https://loremflickr.com/240/320/cat")
        p.setRating(4.9f)
        p.setNumOfRates(10)
        p.setComments(arrayListOf("love it", "another cat"))
        posts.add(p)

        p = Post()
        p.setId("7")
        p.setImageUrl("https://loremflickr.com/100/100/cat")
        p.setRating(3.9f)
        p.setNumOfRates(10)
        p.setComments(ArrayList())
        posts.add(p)

        p = Post()
        p.setId("8")
        p.setImageUrl("https://loremflickr.com/150/100/cat")
        p.setRating(3.8f)
        p.setNumOfRates(5)
        p.setComments(ArrayList())
        posts.add(p)

        p = Post()
        p.setId("9")
        p.setImageUrl("https://loremflickr.com/100/150/cat")
        p.setRating(3.3f)
        p.setNumOfRates(10)
        p.setComments(ArrayList())
        posts.add(p)

        return posts
    }

    private fun initUsers(): List<User> {
        val users = ArrayList<User>()

        var u = User("login1", "pass1")
        u.setEmail("login1@example.com")
        u.setGender("male")
        u.setBirthday("01.01.1970")
        users.add(u)

        u = User("login2", "pass2")
        u.setEmail("login2@example.com")
        u.setGender("female")
        u.setBirthday("21.12.2012")
        users.add(u)

        return users
    }

    fun getUser(login: String): User? = users.findLast{ it.getLogin() == login }

    fun getPost(id: String): Post? = posts.findLast{ it.getId() == id }
}
