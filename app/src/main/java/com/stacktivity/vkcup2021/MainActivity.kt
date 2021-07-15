package com.stacktivity.vkcup2021

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import com.stacktivity.vkcup2021.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private val binding: MainActivityBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUI(savedInstanceState)
    }

    private fun initUI(savedInstanceState: Bundle?) {
        // TODO not implemented yet
    }
}