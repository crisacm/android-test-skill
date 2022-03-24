package com.github.crisacm.testskills.data.database.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.crisacm.testskills.data.database.models.Post
import com.github.crisacm.testskills.data.database.repositories.PostRepo
import com.github.crisacm.testskills.data.network.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val mPostRepo: PostRepo
) : ViewModel() {

    private val _syncEvent: MutableSharedFlow<SyncEvent<Nothing>> = MutableSharedFlow()
    val syncEvent = _syncEvent.asSharedFlow()

    sealed class SyncEvent<out R> {
        object Syncing : SyncEvent<Nothing>()
        object SyncSuccessful : SyncEvent<Nothing>()
        data class SyncFailed(val msg: String) : SyncEvent<Nothing>()
    }

    fun syncPosts(userId: Long) {
        viewModelScope.launch {
            when (val result = mPostRepo.syncPostsByUser(userId)) {
                is ApiResult.Success -> {
                    _syncEvent.emit(SyncEvent.Syncing)
                    val newPosts = mutableListOf<Post>()

                    result.data.forEach { _postsResponse ->
                        _postsResponse?.also {
                            it.id?.let { _id ->
                                if (mPostRepo.getBy(_id) == null) {
                                    Post().apply {
                                        it.userId?.let { it2 -> this.userId = it2 }
                                        it.id?.let { it2 -> this.idServer = it2 }
                                        this.title = it.title
                                        this.body = it.body
                                    }.also { _post -> newPosts.add(_post) }
                                }
                            }
                        }
                    }

                    if (newPosts.isNotEmpty()) {
                        mPostRepo.insert(newPosts)
                    }

                    _syncEvent.emit(SyncEvent.SyncSuccessful)
                }
                is ApiResult.Failure -> _syncEvent.emit(SyncEvent.SyncFailed("Failure: ${result.message}"))
                is ApiResult.Error -> _syncEvent.emit(SyncEvent.SyncFailed("Error: ${result.t.message}"))
            }
        }
    }

    fun getAllBy(userId: Long): Flow<List<Post>> = mPostRepo.getAllByOnFlow(userId)
}