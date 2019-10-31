package com.example.desiregallery.data.network.query

import com.example.desiregallery.data.network.INTEGER_VALUE
import com.example.desiregallery.data.network.STRING_VALUE
import com.google.gson.JsonObject

/**
 * @author babaetskv on 19.09.19
 */
class Value private constructor() {
    lateinit var jsonValue: JsonObject
        private set

    constructor(value: Int) : this() {
        jsonValue = JsonObject().apply {
            addProperty(INTEGER_VALUE, value)
        }
    }

    constructor(value: String) : this() {
        jsonValue = JsonObject().apply {
            addProperty(STRING_VALUE, value)
        }
    }
}
