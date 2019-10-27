package com.example.desiregallery.models

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
    var datetime: Long = Date().time

    override fun toString() = "Comment(text='$text', postId='$postId', author='$author', datetime=$datetime)"
}
