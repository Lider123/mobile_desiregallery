package com.example.desiregallery.data.network.query.requests

import com.example.desiregallery.data.network.query.OrderDirection
import com.example.desiregallery.data.network.query.Value
import com.example.desiregallery.data.network.query.filters.FieldFilter
import com.example.desiregallery.data.network.query.operators.ComparisonOperator

/**
 * @author babaetskv on 01.11.19
 */
class CommentsQueryRequest(postId: String, limit: Int, offset: Long) : BaseQueryRequest() {

    init {
        from("comments")
        where(FieldFilter("postId", ComparisonOperator.EQUAL, Value(postId)))
        orderBy("timestamp", OrderDirection.ASCENDING)
        if (limit > 0) limit(limit)
        if (offset > 0) offset(offset)
    }
}
