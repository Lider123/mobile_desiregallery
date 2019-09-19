package com.example.desiregallery.network.query.filters

import com.example.desiregallery.network.query.Value
import com.example.desiregallery.network.query.operators.ComparisonOperator
import com.google.gson.JsonObject

/**
 * @author babaetskv on 19.09.19
 */
class FieldFilter(field: String, op: ComparisonOperator, value: Value) :
    IFilter {
    override val json: JsonObject = JsonObject().apply {
        addProperty("op", op.name)
        add("value", value.jsonValue)
        add("field", JsonObject().apply { addProperty("fieldPath", field) })
    }
}
