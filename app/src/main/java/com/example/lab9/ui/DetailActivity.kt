// app/src/main/java/com/example/lab9/ui/DetailActivity.kt

package com.example.lab9.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lab9.R
import com.example.lab9.model.Post
import com.example.lab9.utils.NetworkUtils
import com.example.lab9.viewmodel.PostViewModel
import com.example.lab9.viewmodel.Resource

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: PostViewModel
    private var currentPost: Post? = null
    private var postId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail) // Встановлення макету

        // Ініціалізація ViewModel
        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        // Отримання ID посту з Intent
        postId = intent.getIntExtra("post_id", -1)
        if (postId != -1) {
            if (NetworkUtils.isNetworkAvailable(this)) {
                viewModel.fetchPost(postId)
            } else {
                val tvErrorDetail = findViewById<TextView>(R.id.tvErrorDetail)
                tvErrorDetail.visibility = View.VISIBLE
                tvErrorDetail.text = "No internet connection."
            }
        } else {
            Toast.makeText(this, "Invalid post ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Спостереження за LiveData
        observeViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                currentPost?.let { showEditPostDialog(it) }
                return true
            }
            R.id.action_delete -> {
                confirmDeletePost()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showEditPostDialog(post: Post) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_post, null)
        val etEditTitle = dialogView.findViewById<EditText>(R.id.etEditTitle)
        val etEditBody = dialogView.findViewById<EditText>(R.id.etEditBody)

        // Заповнення полів поточними даними посту
        etEditTitle.setText(post.title)
        etEditBody.setText(post.body)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Post")
            .setView(dialogView)
            .setPositiveButton("Update") { dialogInterface, _ ->
                val updatedTitle = etEditTitle.text.toString().trim()
                val updatedBody = etEditBody.text.toString().trim()

                if (updatedTitle.isNotEmpty() && updatedBody.isNotEmpty()) {
                    val updatedPost = post.copy(title = updatedTitle, body = updatedBody)
                    if (NetworkUtils.isNetworkAvailable(this)) {
                        viewModel.updatePost(postId, updatedPost)
                        Toast.makeText(this, "Post updated", Toast.LENGTH_SHORT).show()
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

    private fun confirmDeletePost() {
        AlertDialog.Builder(this)
            .setTitle("Delete Post")
            .setMessage("Are you sure you want to delete this post?")
            .setPositiveButton("Yes") { dialogInterface, _ ->
                if (NetworkUtils.isNetworkAvailable(this)) {
                    viewModel.deletePost(postId)
                    Toast.makeText(this, "Post deleted", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "No internet connection.", Toast.LENGTH_SHORT).show()
                }
                dialogInterface.dismiss()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()
            .show()
    }

    private fun observeViewModel() {
        viewModel.post.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    val progressBarDetail = findViewById<ProgressBar>(R.id.progressBarDetail)
                    val tvErrorDetail = findViewById<TextView>(R.id.tvErrorDetail)
                    val layoutDetail = findViewById<LinearLayout>(R.id.layoutDetail)

                    progressBarDetail.visibility = View.VISIBLE
                    tvErrorDetail.visibility = View.GONE
                    layoutDetail.visibility = View.GONE
                }
                is Resource.Success -> {
                    val progressBarDetail = findViewById<ProgressBar>(R.id.progressBarDetail)
                    val tvErrorDetail = findViewById<TextView>(R.id.tvErrorDetail)
                    val layoutDetail = findViewById<LinearLayout>(R.id.layoutDetail)
                    val tvDetailTitle = findViewById<TextView>(R.id.tvDetailTitle)
                    val tvDetailBody = findViewById<TextView>(R.id.tvDetailBody)

                    progressBarDetail.visibility = View.GONE
                    tvErrorDetail.visibility = View.GONE
                    layoutDetail.visibility = View.VISIBLE

                    currentPost = resource.data
                    tvDetailTitle.text = resource.data?.title
                    tvDetailBody.text = resource.data?.body
                }
                is Resource.Error -> {
                    val progressBarDetail = findViewById<ProgressBar>(R.id.progressBarDetail)
                    val tvErrorDetail = findViewById<TextView>(R.id.tvErrorDetail)

                    progressBarDetail.visibility = View.GONE
                    tvErrorDetail.visibility = View.VISIBLE
                    tvErrorDetail.text = resource.message
                }
            }
        }

        // Опціонально, спостереження за іншими LiveData для CRUD операцій
    }
}
