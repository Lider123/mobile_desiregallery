package com.example.desiregallery.helpers

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
        posts.add(p)

        p = Post()
        p.id = "2"
        p.setImageUrl("https://via.placeholder.com/600/771796")
        p.rating = 4.6f
        p.numOfRates = 5
        posts.add(p)

        p = Post()
        p.id = "3"
        p.setImageUrl("https://via.placeholder.com/600/24f355")
        p.rating = 3.1f
        p.numOfRates = 10
        posts.add(p)

        p = Post()
        p.id = "4"
        p.setImageUrl("https://via.placeholder.com/600/d32776")
        p.rating = 0.0f
        p.numOfRates = 0
        posts.add(p)

        p = Post()
        p.id = "5"
        p.setImageUrl("https://loremflickr.com/320/240/cat")
        p.rating = 5.0f
        p.numOfRates = 1
        posts.add(p)

        p = Post()
        p.id = "6"
        p.setImageUrl("https://loremflickr.com/240/320/cat")
        p.rating = 4.9f
        p.numOfRates = 10
        posts.add(p)

        p = Post()
        p.id = "7"
        p.setImageUrl("https://loremflickr.com/100/100/cat")
        p.rating = 3.9f
        p.numOfRates = 10
        posts.add(p)

        p = Post()
        p.id = "8"
        p.setImageUrl("https://loremflickr.com/150/100/cat")
        p.rating = 3.8f
        p.numOfRates = 5
        posts.add(p)

        p = Post()
        p.id = "9"
        p.setImageUrl("https://loremflickr.com/100/150/cat")
        p.rating = 3.3f
        p.numOfRates = 10
        posts.add(p)

        return posts
    }

    private fun initUsers(): List<User> {
        val users = ArrayList<User>()

        var u = User("login1@example.com", "pass1")
        u.login = "login1"
        u.gender = "male"
        u.birthday = "01.01.1970"
        users.add(u)

        u = User("login2@example.com", "pass2")
        u.login = "login2"
        u.gender = "female"
        u.birthday = "21.12.2012"
        users.add(u)

        return users
    }

    fun getUser(login: String): User? = users.findLast{ it.login == login }

    fun getPost(id: String): Post? = posts.findLast{ it.id == id }
}
