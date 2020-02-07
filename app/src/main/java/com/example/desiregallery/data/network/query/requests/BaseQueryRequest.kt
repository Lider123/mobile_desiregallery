package com.example.desiregallery.data.network.query.requests

import com.example.desiregallery.data.network.*
import com.example.desiregallery.data.network.query.OrderDirection
import com.example.desiregallery.data.network.query.filters.CompositeFilter
import com.example.desiregallery.data.network.query.filters.FieldFilter
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.io.Serializable

/**
 * @author babaetskv on 19.09.19
 * Related to https://cloud.google.com/firestore/docs/reference/rest/v1/StructuredQuery
 */
abstract class BaseQueryRequest : Serializable {
    private val structuredQuery = JsonObject()

    protected fun from(collectionId: String): BaseQueryRequest {
        with(structuredQuery) {
            if (has(FROM)) remove(FROM)
            add(FROM, JsonObject().apply {
                addProperty(COLLECTION_ID, collectionId)
            })
        }
        return this
    }

    protected fun from(collectionIds: List<String>): BaseQueryRequest {
        with(structuredQuery) {
            if (has(FROM)) remove(FROM)
            add(FROM, JsonArray().apply {
                for (col in collectionIds) add(JsonObject().apply {
                    addProperty(COLLECTION_ID, col)
                })
            })
        }
        return this
    }

    protected fun where(filter: FieldFilter): BaseQueryRequest {
        with(structuredQuery) {
            if (has(WHERE)) remove(WHERE)
            add(WHERE, JsonObject().apply {
                add(FIELD_FILTER, filter.json)
            })
        }
        return this
    }

    protected fun where(filter: CompositeFilter): BaseQueryRequest {
        with(structuredQuery) {
            if (has(WHERE)) remove(WHERE)
            add(WHERE, JsonObject().apply {
                add(COMPOSITE_FILTER, filter.json)
            })
        }
        return this
    }

    protected fun orderBy(field: String, direction: OrderDirection): BaseQueryRequest {
        with(structuredQuery) {
            if (has(ORDER_BY)) remove(ORDER_BY)
            add(ORDER_BY, JsonObject().apply {
                addProperty(DIRECTION, direction.name)
                add(FIELD, JsonObject().apply { addProperty(FIELD_PATH, field) })
            })
        }
        return this
    }

    protected fun limit(limit: Int): BaseQueryRequest {
        with(structuredQuery) {
            if (has(LIMIT)) remove(LIMIT)
            addProperty(LIMIT, limit)
        }
        return this
    }

    protected fun offset(count: Long): BaseQueryRequest {
        with(structuredQuery) {
            if (has(OFFSET)) remove(OFFSET)
            addProperty(OFFSET, count)
        }
        return this
    }
}
