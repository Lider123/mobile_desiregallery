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
        from("posts")
        orderBy("timestamp", OrderDirection.DESCENDING)
        if (limit > 0) limit(limit)
        if (offset > 0) offset(offset)
        author?.let {
            where(FieldFilter("author", ComparisonOperator.EQUAL, Value(author)))
        }
    }
}
