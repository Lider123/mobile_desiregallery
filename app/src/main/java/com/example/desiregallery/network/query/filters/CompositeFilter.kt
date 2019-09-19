package com.example.desiregallery.network.query.filters

import com.example.desiregallery.network.query.operators.LogicalOperator
import com.google.gson.JsonArray
import com.google.gson.JsonObject

/**
 * @author babaetskv on 19.09.19
 */
class CompositeFilter(filters: List<FieldFilter>, op: LogicalOperator) :
    IFilter {
    override val json = JsonObject().apply {
        addProperty("op", op.name)
        add("filters", JsonArray().apply {
            for (filter in filters) {
                add(JsonObject().apply {
                    add("fieldFilter", filter.json)
                })
            }
        })
    }
}
