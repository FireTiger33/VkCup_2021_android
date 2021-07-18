package com.stacktivity.vkcup2021.ui.main

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import com.stacktivity.vkcup2021.R.layout.main_screen
import com.stacktivity.vkcup2021.databinding.MainScreenBinding
import com.stacktivity.core.utils.FragmentManagers.replaceFragment
import com.stacktivity.vknews.R.id.container
import com.stacktivity.vknews.ui.NewsFragment

class MainFragment : Fragment(main_screen) {
    private val binding: MainScreenBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
    }

    private fun setupButtons() {
        binding.btnVkNews.setOnClickListener {
            replaceFragment(requireActivity().supportFragmentManager, NewsFragment(), container)
        }
    }
}