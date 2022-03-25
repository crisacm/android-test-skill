package com.github.crisacm.testskills.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.crisacm.testskills.data.database.models.User
import com.github.crisacm.testskills.data.database.repositories.UserRepo
import com.github.crisacm.testskills.data.database.viewModels.MainViewModel
import com.github.crisacm.testskills.databinding.ActivityMainBinding
import com.github.crisacm.testskills.ui.dialogs.LoadingDialog
import com.github.crisacm.testskills.ui.posts.PostsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mMainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var mUserRepo: UserRepo

    private val finalListUsers: MutableLiveData<List<User>> = MutableLiveData(listOf())
    private var listUsers: MutableList<User> = mutableListOf()
    private val userAdapter: UsersAdapter by lazy { UsersAdapter() }

    private lateinit var loadingDialog: LoadingDialog
    private var queryJob: Job? = null

    companion object {
        const val TAG = "MainActivity"
        const val LOADING_TAG = "loading_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            loadingDialog = LoadingDialog.newInstance(false, "Cargando usuarios...")

            supportActionBar?.title = "Prueba de Ingreso"

            with(binding) {
                recyclerView.apply {
                    setHasFixedSize(true)
                    adapter = userAdapter
                    layoutManager = LinearLayoutManager(this@MainActivity)
                }

                inputSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun afterTextChanged(p0: Editable?) {
                        p0?.let {
                            filterUsers(it.toString())
                        }
                    }
                })
            }

            userAdapter.setOnItemClickListener { user, _ ->
                Intent(this, PostsActivity::class.java).apply {
                    putExtra(PostsActivity.ARG_USER, user)
                }.also { _intent -> startActivity(_intent) }
            }

            lifecycleScope.launch {
                mMainViewModel.uiEvent.collectLatest {
                    when (it) {
                        is MainViewModel.SyncEvent.Syncing -> showLoading(true)
                        is MainViewModel.SyncEvent.SyncFailed -> {
                            showLoading(false)
                            Log.e(TAG, it.msg)
                        }
                        is MainViewModel.SyncEvent.SyncSuccessful -> showLoading(false)
                    }
                }
            }

            lifecycleScope.launch {
                mMainViewModel.getAllUsers().collectLatest {
                    listUsers = it.toMutableList()
                    finalListUsers.value = listUsers
                }
            }

            lifecycleScope.launch {
                finalListUsers.observe(this@MainActivity) {
                    userAdapter.setData(it.toMutableList())
                    when (it.isEmpty()) {
                        true -> binding.textEmpty.visibility = View.VISIBLE
                        false -> binding.textEmpty.visibility = View.GONE
                    }
                }
            }

            // Syncronizamos los usuarios y validamos su existencia.
            mMainViewModel.syncAndValidateUsers()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Ops! something go wrong...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filterUsers(text: String) {
        queryJob?.cancel()
        queryJob = CoroutineScope(Dispatchers.IO).launch {
            if (text.isEmpty()) {
                withContext(Dispatchers.Main) {
                    finalListUsers.value = listUsers
                }
            } else {
                val filterListUsers = mutableListOf<User>()

                listUsers.forEach {
                    if (it.name.uppercase().contains(text.uppercase())) {
                        filterListUsers.add(it)
                    }
                }

                withContext(Dispatchers.Main) {
                    finalListUsers.value = filterListUsers
                }
            }
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
}