package com.example.desiregallery.data.network.query.filters

import com.example.desiregallery.data.network.FIELD
import com.example.desiregallery.data.network.FIELD_PATH
import com.example.desiregallery.data.network.OP
import com.example.desiregallery.data.network.query.operators.UnaryOperator
import com.google.gson.JsonObject

/**
 * @author babaetskv on 19.09.19
 */
class UnaryFilter(field: String, op: UnaryOperator) : IFilter {
    override val json = JsonObject().apply {
        addProperty(OP, op.name)
        add(FIELD, JsonObject().apply { addProperty(FIELD_PATH, field) })
    }
}