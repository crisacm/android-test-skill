package com.github.crisacm.testskills.ui.posts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.crisacm.testskills.data.database.models.Post
import com.github.crisacm.testskills.data.database.models.User
import com.github.crisacm.testskills.data.database.viewModels.PostViewModel
import com.github.crisacm.testskills.databinding.ActivityPostsBinding
import com.github.crisacm.testskills.ui.dialogs.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostsBinding

    private val mPostViewModel: PostViewModel by viewModels()

    private lateinit var user: User
    private lateinit var loadingDialog: LoadingDialog

    private var listPosts: MutableList<Post> = mutableListOf()
    private val postAdapter: PostAdapter by lazy { PostAdapter() }

    companion object {
        const val ARG_USER = "arg_user"
        const val TAG = "PostsActivity"
        const val LOADING_TAG = "loading_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            intent.extras?.getParcelable<User>(ARG_USER)?.also { _user -> user = _user } ?: kotlin.run {
                Toast.makeText(this, "Hubo un problema al cargar los posts", Toast.LENGTH_SHORT).show()
                finish()
            }

            loadingDialog = LoadingDialog.newInstance(false, "Cargando posts...")

            supportActionBar?.apply {
                title = "Publicaciones"
                setDisplayShowHomeEnabled(true)
            }

            with(binding) {
                textNombre.text = user.name
                textEmail.text = user.email
                textPhone.text = user.phone

                customRecyclerView.init(
                    true,
                    postAdapter,
                    LinearLayoutManager(this@PostsActivity)
                )
            }

            lifecycleScope.launch {
                mPostViewModel.syncEvent.collectLatest {
                    when (it) {
                        is PostViewModel.SyncEvent.Syncing -> showLoading(true)
                        is PostViewModel.SyncEvent.SyncFailed -> {
                            showLoading(false)
                            Log.e(TAG, it.msg)
                        }
                        is PostViewModel.SyncEvent.SyncSuccessful -> showLoading(false)
                    }
                }
            }

            lifecycleScope.launch {
                mPostViewModel.getAllBy(user.idServer).collectLatest {
                    listPosts = it.toMutableList()
                    postAdapter.setData(listPosts)
                    binding.customRecyclerView.showRecyclerView(listPosts.size)
                }
            }

            mPostViewModel.syncPosts(user.idServer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showLoading(boolean: Boolean) {
        try {
            when (boolean) {
                true -> if (supportFragmentManager.findFragmentByTag(LOADING_TAG) == null) {
                    loadingDialog.show(supportFragmentManager, LOADING_TAG)
                }
                false -> loadingDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}