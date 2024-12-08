package com.example.lab9.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab9.databinding.ItemPostBinding
import com.example.lab9.model.Post

class PostAdapter(
    private var posts: List<Post>,
    private val onItemClick: (Post) -> Unit
): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post){
            binding.tvTitle.text = post.title
            binding.tvBody.text = post.body
            binding.root.setOnClickListener {
                onItemClick(post)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    fun setPosts(newPosts: List<Post>){
        this.posts = newPosts
        notifyDataSetChanged()
    }
}
