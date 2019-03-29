package com.example.desiregallery.models

import java.io.Serializable
import java.util.*

data class Comment(val text: String, val timestamp: Date = Date()) : Serializable