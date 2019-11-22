package com.example.desiregallery.data.network.query.filters

import com.example.desiregallery.data.network.FIELD
import com.example.desiregallery.data.network.FIELD_PATH
import com.example.desiregallery.data.network.OP
import com.example.desiregallery.data.network.VALUE
import com.example.desiregallery.data.network.query.Value
import com.example.desiregallery.data.network.query.operators.ComparisonOperator
import com.google.gson.JsonObject

/**
 * @author babaetskv on 19.09.19
 */
class FieldFilter(field: String, op: ComparisonOperator, value: Value) : IFilter {

    override val json: JsonObject = JsonObject().apply {
        addProperty(OP, op.name)
        add(VALUE, value.jsonValue)
        add(FIELD, JsonObject().apply { addProperty(FIELD_PATH, field) })
    }
}
