package com.example.desiregallery.data.models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable
import java.util.*
import kotlin.random.Random

/**
 * @author babaetskv
 * @since 08.08.19
 */
class Comment: Serializable {
    var id: String = Random.nextLong(1e10.toLong()).toString()
    var text: String = ""
    var postId: String = ""
    var author = User("", "")
    var timestamp: Long = Date().time

    override fun toString() = "Comment(" +
            "text='$text', " +
            "postId='$postId', " +
            "author='$author', " +
            "timestamp=$timestamp)"

    companion object {
        val CALLBACK: DiffUtil.ItemCallback<Comment> = object: DiffUtil.ItemCallback<Comment>() {

            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment) = true
        }
    }
}
