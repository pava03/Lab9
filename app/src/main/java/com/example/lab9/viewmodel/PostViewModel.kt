package com.example.lab9.viewmodel

import androidx.lifecycle.*
import com.example.lab9.model.Post
import com.example.lab9.repository.PostRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
){
    class Success<T>(data: T): Resource<T>(data)
    class Loading<T>: Resource<T>()
    class Error<T>(message: String): Resource<T>(message = message)
}

class PostViewModel : ViewModel() {

    private val repository = PostRepository()

    private val _posts = MutableLiveData<Resource<List<Post>>>()
    val posts: LiveData<Resource<List<Post>>> = _posts

    private val _post = MutableLiveData<Resource<Post>>()
    val post: LiveData<Resource<Post>> = _post

    fun fetchPosts() {
        viewModelScope.launch {
            _posts.value = Resource.Loading()
            try {
                val response = repository.getPosts()
                if(response.isSuccessful){
                    _posts.value = Resource.Success(response.body()!!)
                } else {
                    _posts.value = Resource.Error("Error Code: ${response.code()}")
                }
            } catch (e: Exception){
                _posts.value = Resource.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }

    fun fetchPost(id: Int){
        viewModelScope.launch {
            _post.value = Resource.Loading()
            try {
                val response = repository.getPost(id)
                if(response.isSuccessful){
                    _post.value = Resource.Success(response.body()!!)
                } else {
                    _post.value = Resource.Error("Error Code: ${response.code()}")
                }
            } catch (e: Exception){
                _post.value = Resource.Error("An error occurred: ${e.localizedMessage}")
            }
        }
    }

    fun createPost(post: Post){
        viewModelScope.launch {
            try {
                repository.createPost(post)
                fetchPosts()
            } catch (e: Exception){
            }
        }
    }

    fun updatePost(id: Int, post: Post){
        viewModelScope.launch {
            try {
                repository.updatePost(id, post)
                fetchPosts()
            } catch (e: Exception){
            }
        }
    }

    fun deletePost(id: Int){
        viewModelScope.launch {
            try {
                repository.deletePost(id)
                fetchPosts()
            } catch (e: Exception){
            }
        }
    }
}
