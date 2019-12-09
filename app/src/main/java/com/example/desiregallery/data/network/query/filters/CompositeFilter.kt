package com.example.desiregallery.data.network.query.filters

import com.example.desiregallery.data.network.FIELD_FILTER
import com.example.desiregallery.data.network.FILTERS
import com.example.desiregallery.data.network.OP
import com.example.desiregallery.data.network.query.operators.LogicalOperator
import com.google.gson.JsonArray
import com.google.gson.JsonObject

/**
 * @author babaetskv on 19.09.19
 */
class CompositeFilter(filters: List<FieldFilter>, op: LogicalOperator) : IFilter {
    override val json = JsonObject().apply {
        addProperty(OP, op.name)
        add(FILTERS, JsonArray().apply {
            for (filter in filters)
                add(JsonObject().apply {
                    add(FIELD_FILTER, filter.json)
                })
        })
    }
}
