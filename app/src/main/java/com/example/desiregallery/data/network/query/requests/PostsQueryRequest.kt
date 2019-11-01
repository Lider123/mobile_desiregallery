package com.example.desiregallery.data.network.query.requests

import com.example.desiregallery.data.network.query.OrderDirection

/**
 * @author babaetskv on 01.11.19
 */
class PostsQueryRequest(limit: Int, offset: Long) : BaseQueryRequest() {

    init {
        this.from("posts")
        this.orderBy("timestamp",
            OrderDirection.DESCENDING
        )
        if (limit > 0)
            this.limit(limit)
        if (offset > 0)
            this.offset(offset)
    }
}