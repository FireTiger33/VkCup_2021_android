package com.stacktivity.vknews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stacktivity.vknews.repo.NewsRepository

class NewsViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel(NewsRepository()) as T
        }

        throw IllegalArgumentException("Unable to construct viewModel")
    }
}