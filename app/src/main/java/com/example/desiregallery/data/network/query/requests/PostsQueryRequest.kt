package com.example.desiregallery.data.network.query.requests

import com.example.desiregallery.data.network.query.OrderDirection
import com.example.desiregallery.data.network.query.Value
import com.example.desiregallery.data.network.query.filters.FieldFilter
import com.example.desiregallery.data.network.query.operators.ComparisonOperator

/**
 * @author babaetskv on 01.11.19
 */
class PostsQueryRequest(limit: Int, offset: Long, author: String? = null) : BaseQueryRequest() {

    init {
        this.from("posts")
        this.orderBy("timestamp",
            OrderDirection.DESCENDING
        )
        if (limit > 0)
            this.limit(limit)
        if (offset > 0)
            this.offset(offset)
        author?.let {
            this.where(FieldFilter("author", ComparisonOperator.EQUAL, Value(author)))
        }
    }
}