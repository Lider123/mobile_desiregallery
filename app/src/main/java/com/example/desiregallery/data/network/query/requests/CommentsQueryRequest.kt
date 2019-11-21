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
        this.from("comments")
        this.where(FieldFilter("postId", ComparisonOperator.EQUAL, Value(postId)))
        this.orderBy("timestamp", OrderDirection.ASCENDING)
        if (limit > 0) this.limit(limit)
        if (offset > 0) this.offset(offset)
    }
}
