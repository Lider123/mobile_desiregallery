package com.example.desiregallery.models

import java.io.Serializable
import java.util.*

/**
 * @author babaetskv
 * @since 08.08.19
 */

data class Comment(var text: String, var datetime: Long = Date().time) : Serializable
