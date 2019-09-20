package com.example.desiregallery.network.query

import com.example.desiregallery.network.query.filters.CompositeFilter
import com.example.desiregallery.network.query.filters.FieldFilter
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.io.Serializable

/**
 * @author babaetskv on 19.09.19
 */
class QueryRequest : Serializable {
    private val structuredQuery = JsonObject()

    fun from(collectionId: String): QueryRequest {
        if (structuredQuery.has("from"))
            structuredQuery.remove("from")

        structuredQuery.add("from", JsonObject().apply { addProperty("collectionId", collectionId) })
        return this
    }

    fun from(collectionIds: List<String>): QueryRequest {
        if (structuredQuery.has("from"))
            structuredQuery.remove("from")

        structuredQuery.add("from", JsonArray().apply {
            for (col in collectionIds)
                add(JsonObject().apply { addProperty("collectionId", col) })
        })
        return this
    }

    fun where(filter: FieldFilter): QueryRequest {
        if (structuredQuery.has("where"))
            structuredQuery.remove("where")

        structuredQuery.add("where", JsonObject().apply {
            add("fieldFilter", filter.json)
        })
        return this
    }

    fun where(filter: CompositeFilter): QueryRequest {
        if (structuredQuery.has("where"))
            structuredQuery.remove("where")

        structuredQuery.add("where", JsonObject().apply {
            add("compositeFilter", filter.json)
        })
        return this
    }

    fun orderBy(field: String, direction: OrderDirection): QueryRequest {
        if (structuredQuery.has("orderBy"))
            structuredQuery.remove("orderBy")

        structuredQuery.add("orderBy", JsonObject().apply {
            addProperty("direction", direction.name)
            add("field", JsonObject().apply { addProperty("fieldPath", field) })
        })
        return this
    }
}
