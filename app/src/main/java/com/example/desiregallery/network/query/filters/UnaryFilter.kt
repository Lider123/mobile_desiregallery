package com.example.desiregallery.network.query.filters

import com.example.desiregallery.network.query.operators.UnaryOperator
import com.google.gson.JsonObject

/**
 * @author babaetskv on 19.09.19
 */
class UnaryFilter(field: String, op: UnaryOperator) : IFilter {
    override val json = JsonObject().apply {
        addProperty("op", op.name)
        add("field", JsonObject().apply { addProperty("fieldPath", field) })
    }
}