package com.example.lab9.api

import com.example.lab9.model.Post
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("/posts")
    suspend fun getPosts(): Response<List<Post>>

    @GET("/posts/{id}")
    suspend fun getPost(@Path("id") id: Int): Response<Post>

    @POST("/posts")
    suspend fun createPost(@Body post: Post): Response<Post>

    @PUT("/posts/{id}")
    suspend fun updatePost(@Path("id") id: Int, @Body post: Post): Response<Post>

    @DELETE("/posts/{id}")
    suspend fun deletePost(@Path("id") id: Int): Response<Void>
}
