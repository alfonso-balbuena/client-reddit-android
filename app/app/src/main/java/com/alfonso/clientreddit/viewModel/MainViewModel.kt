package com.alfonso.clientreddit.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfonso.clientreddit.models.DataPost
import com.alfonso.clientreddit.repositoryReddit.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    val posts  = repository.posts
    val isLoading = repository.isLoading

    init {
        viewModelScope.launch {
            repository.init()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            repository.refresh()
        }
    }

    fun dismiss(post: DataPost) {
        viewModelScope.launch {
            Timber.d("Dismissing... $post")
            repository.dismiss(post)
        }
    }

    fun dismissAll() {
        viewModelScope.launch {
            Timber.d("Dismissing all items...")
            posts.value?.let { repository.dismiss(it) }
        }
    }

}