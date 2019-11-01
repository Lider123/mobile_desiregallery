package com.example.desiregallery.data.network.query

import com.example.desiregallery.data.network.*
import com.example.desiregallery.data.network.query.filters.CompositeFilter
import com.example.desiregallery.data.network.query.filters.FieldFilter
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.io.Serializable

/**
 * @author babaetskv on 19.09.19
 * Related to https://cloud.google.com/firestore/docs/reference/rest/v1/StructuredQuery
 */
class QueryRequest : Serializable {
    private val structuredQuery = JsonObject()

    fun from(collectionId: String): QueryRequest {
        if (structuredQuery.has(FROM))
            structuredQuery.remove(FROM)

        structuredQuery.add(FROM, JsonObject().apply {
            addProperty(COLLECTION_ID, collectionId)
        })
        return this
    }

    fun from(collectionIds: List<String>): QueryRequest {
        if (structuredQuery.has(FROM))
            structuredQuery.remove(FROM)

        structuredQuery.add(FROM, JsonArray().apply {
            for (col in collectionIds)
                add(JsonObject().apply { addProperty(COLLECTION_ID, col) })
        })
        return this
    }

    fun where(filter: FieldFilter): QueryRequest {
        if (structuredQuery.has(WHERE))
            structuredQuery.remove(WHERE)

        structuredQuery.add(WHERE, JsonObject().apply {
            add(FIELD_FILTER, filter.json)
        })
        return this
    }

    fun where(filter: CompositeFilter): QueryRequest {
        if (structuredQuery.has(WHERE))
            structuredQuery.remove(WHERE)

        structuredQuery.add(WHERE, JsonObject().apply {
            add(COMPOSITE_FILTER, filter.json)
        })
        return this
    }

    fun orderBy(field: String, direction: OrderDirection): QueryRequest {
        if (structuredQuery.has(ORDER_BY))
            structuredQuery.remove(ORDER_BY)

        structuredQuery.add(ORDER_BY, JsonObject().apply {
            addProperty(DIRECTION, direction.name)
            add(FIELD, JsonObject().apply { addProperty(FIELD_PATH, field) })
        })
        return this
    }

    fun limit(limit: Int): QueryRequest {
        if (structuredQuery.has(LIMIT))
            structuredQuery.remove(LIMIT)
        structuredQuery.addProperty(LIMIT, limit)
        return this
    }

    fun offset(count: Long): QueryRequest {
        if (structuredQuery.has(OFFSET))
            structuredQuery.remove(OFFSET)
        structuredQuery.addProperty(OFFSET, count)
        return this
    }
}
