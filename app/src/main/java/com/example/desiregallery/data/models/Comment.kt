package com.example.desiregallery.data.models

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
import kotlin.random.Random

/**
 * @author babaetskv
 * @since 08.08.19
 */
data class Comment(
    @SerializedName("text")
    var text: String = "",
    @SerializedName("postId")
    var postId: String = "",
    @SerializedName("author")
    var author: User = User("", ""),
    @SerializedName("timestamp")
    var timestamp: Long = Date().time
) : Serializable {
    @SerializedName("id")
    var id: String = Random.nextLong(1e10.toLong()).toString()

    companion object {
        val CALLBACK: DiffUtil.ItemCallback<Comment> = object : DiffUtil.ItemCallback<Comment>() {

            override fun areItemsTheSame(oldItem: Comment, newItem: Comment) =
                (oldItem.id == newItem.id)

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment) = true
        }
    }
}
