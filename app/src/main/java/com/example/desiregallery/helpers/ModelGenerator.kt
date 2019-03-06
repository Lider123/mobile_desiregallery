package com.example.desiregallery.helpers

import com.example.desiregallery.models.Post

import java.util.ArrayList
import java.util.Arrays

class ModelGenerator {
    companion object {
        fun getPosts() : List<Post> {
            val posts : ArrayList<Post> = ArrayList()

            var p = Post()
            p.setId("1")
            p.setImageUrl("https://via.placeholder.com/600/92c952")
            p.setRating(4.5f)
            p.setComments(ArrayList())
            posts.add(p)

            p = Post()
            p.setId("2")
            p.setImageUrl("https://via.placeholder.com/600/771796")
            p.setRating(4.6f)
            p.setComments(Arrays.asList("good", "nice"))
            posts.add(p)

            p = Post()
            p.setId("3")
            p.setImageUrl("https://via.placeholder.com/600/24f355")
            p.setRating(3.1f)
            p.setComments(Arrays.asList("bad", "very bad", "not bad"))
            posts.add(p)

            p = Post()
            p.setId("4")
            p.setImageUrl("https://via.placeholder.com/600/d32776")
            p.setRating(0.0f)
            p.setComments(Arrays.asList("very well"))
            posts.add(p)

            return posts
        }
    }
}
