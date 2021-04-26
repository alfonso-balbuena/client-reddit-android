package com.alfonso.clientreddit.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    val hasNext = repository.hasNext
    val hasPrevious = repository.hasPrevious
    private val _postSelected : MutableLiveData<DataPost> = MutableLiveData()
    val postSelected : LiveData<DataPost>
    get() = _postSelected

    init {
        viewModelScope.launch {
            repository.init()
        }
    }

    fun next() {
        repository.next()
    }

    fun previous() {
        repository.previous()
    }

    fun refresh() {
        viewModelScope.launch {
            repository.refresh()
        }
    }

    fun readPost(post: DataPost) {
        viewModelScope.launch {
            repository.read(post)
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
            repository.dismissAll()
        }
    }

    fun selectPost(post : DataPost) {
        _postSelected.value = post
    }

}