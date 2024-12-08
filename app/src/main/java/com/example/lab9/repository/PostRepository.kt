package com.example.lab9.repository


import com.example.lab9.api.RetrofitClient
import com.example.lab9.model.Post
import retrofit2.Response

class PostRepository {

    private val api = RetrofitClient.apiService

    suspend fun getPosts(): Response<List<Post>> {
        return api.getPosts()
    }

    suspend fun getPost(id: Int): Response<Post> {
        return api.getPost(id)
    }

    suspend fun createPost(post: Post): Response<Post> {
        return api.createPost(post)
    }

    suspend fun updatePost(id: Int, post: Post): Response<Post> {
        return api.updatePost(id, post)
    }

    suspend fun deletePost(id: Int): Response<Void> {
        return api.deletePost(id)
    }
}
