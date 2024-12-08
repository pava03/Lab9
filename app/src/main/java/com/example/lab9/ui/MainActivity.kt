// app/src/main/java/com/example/lab9/ui/MainActivity.kt

package com.example.lab9.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab9.R
import com.example.lab9.adapter.PostAdapter
import com.example.lab9.databinding.ActivityMainBinding
import com.example.lab9.databinding.DialogAddPostBinding
import com.example.lab9.model.Post
import com.example.lab9.utils.NetworkUtils
import com.example.lab9.viewmodel.PostViewModel
import com.example.lab9.viewmodel.Resource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: PostViewModel
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PostAdapter(emptyList()) { post ->

            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("post_id", post.id)
            startActivity(intent)
        }

        binding.rvPosts.layoutManager = LinearLayoutManager(this)
        binding.rvPosts.adapter = adapter

        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        if (NetworkUtils.isNetworkAvailable(this)) {
            viewModel.fetchPosts()
        } else {
            binding.tvError.visibility = View.VISIBLE
            binding.tvError.text = "No internet connection."
        }

        observeViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                showAddPostDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAddPostDialog() {
        val dialogBinding = DialogAddPostBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add New Post")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { dialogInterface, _ ->
                val title = dialogBinding.etTitle.text.toString().trim()
                val body = dialogBinding.etBody.text.toString().trim()

                if (title.isNotEmpty() && body.isNotEmpty()) {
                    val newPost = Post(userId = 1, id = 0, title = title, body = body)
                    if (NetworkUtils.isNetworkAvailable(this)) {
                        viewModel.createPost(newPost)
                        Toast.makeText(this, "Post added", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please enter title and body.", Toast.LENGTH_SHORT).show()
                }
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun observeViewModel() {
        viewModel.posts.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                    adapter.setPosts(resource.data!!)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.text = resource.message
                }
            }
        }

    }
}
