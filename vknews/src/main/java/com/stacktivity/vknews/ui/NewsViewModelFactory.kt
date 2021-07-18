package com.stacktivity.vknews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stacktivity.vknews.repo.MockNewsRepo
import com.stacktivity.vknews.repo.NewsRepository

class NewsViewModelFactory(private val testMode: Boolean): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            val repository = if (testMode) MockNewsRepo else NewsRepository()
            @Suppress("UNCHECKED_CAST")
            return NewsViewModel(repository) as T
        }

        throw IllegalArgumentException("Unable to construct viewModel")
    }
}