package com.example.desiregallery.network.query

import com.google.gson.JsonObject

/**
 * @author babaetskv on 19.09.19
 */
class Value private constructor() {
    lateinit var jsonValue: JsonObject
        private set

    constructor(value: Int) : this() {
        jsonValue = JsonObject().apply {
            addProperty("integerValue", value)
        }
    }

    constructor(value: String) : this() {
        jsonValue = JsonObject().apply {
            addProperty("stringValue", value)
        }
    }
}
