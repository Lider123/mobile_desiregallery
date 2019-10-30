package com.example.desiregallery.network.query

import com.example.desiregallery.network.query.filters.CompositeFilter
import com.example.desiregallery.network.query.filters.FieldFilter
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.io.Serializable

/**
 * @author babaetskv on 19.09.19
 * Related to https://cloud.google.com/firestore/docs/reference/rest/v1/StructuredQuery
 */
class QueryRequest : Serializable {
    val structuredQuery = JsonObject()

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

    fun limit(limit: Int): QueryRequest {
        if (structuredQuery.has("limit"))
            structuredQuery.remove("limit")
        structuredQuery.addProperty("limit", limit)
        return this
    }

    fun offset(count: Long): QueryRequest {
        if (structuredQuery.has("offset"))
            structuredQuery.remove("offset")
        structuredQuery.addProperty("offset", count)
        return this
    }
}
