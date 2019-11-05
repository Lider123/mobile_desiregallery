package com.example.desiregallery.data.network

import com.example.desiregallery.data.models.Comment
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.data.network.query.requests.CommentsQueryRequest
import com.example.desiregallery.data.network.query.requests.PostsQueryRequest
import io.reactivex.Single
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author babaetskv on 19.09.19
 */
interface QueryNetworkService {

    @POST(" ")
    fun getComments(@Body query: CommentsQueryRequest): Single<Response<List<Comment>>>

    @POST(" ")
    fun getPosts(@Body query: PostsQueryRequest): Single<Response<List<Post>>>

    companion object {
        private const val QUERY_URL = "https://firestore.googleapis.com/v1/projects/desiregallery-8072a/databases/(default)/documents:runQuery/"

        fun getService(): QueryNetworkService {
            val retrofit = Retrofit.Builder()
                .baseUrl(QUERY_URL)
                .addConverterFactory(createQueryGson())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(QueryNetworkService::class.java)
        }
    }
}
