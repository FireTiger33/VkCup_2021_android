package com.stacktivity.vknews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stacktivity.vknews.model.NewsItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.stacktivity.vknews.repo.NewsRepository

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _newsFlow = MutableStateFlow<List<NewsItem>>(listOf())
    val newsFlow: StateFlow<List<NewsItem>> get() = _newsFlow.asStateFlow()

    private val _dataLoading = MutableStateFlow(false)
    val dataLoading: StateFlow<Boolean> get() = _dataLoading.asStateFlow()

    var testMode = false

    fun fetchItems() {
        viewModelScope.launch {
            if (dataLoading.value) return@launch

            _dataLoading.value = true

            if (testMode) {
                _newsFlow.tryEmit(repository.getTestNews())
            } else {
                _newsFlow.tryEmit(repository.fetchNews())
            }
            _dataLoading.value = false
        }
    }

    fun onNewsLiked(item: NewsItem) {
        // TODO
        _newsFlow.tryEmit(repository.removeItem(item))
    }

    fun onNewsDisliked(item: NewsItem) {
        // TODO
        _newsFlow.tryEmit(repository.removeItem(item))
    }
}