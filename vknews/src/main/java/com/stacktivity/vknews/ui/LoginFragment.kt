package com.stacktivity.vknews.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import com.stacktivity.vknews.R.id.container
import com.stacktivity.vknews.R.layout.login_screen
import com.stacktivity.vknews.databinding.LoginScreenBinding
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope

class LoginFragment: Fragment(login_screen) {
    private val binding: LoginScreenBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            VK.login(requireActivity(), arrayListOf(VKScope.WALL, VKScope.FRIENDS))
        }

        binding.buttonTestMode.setOnClickListener {
            showNewsScreen(testMode = true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("LoginFragment", "onActivityResult")
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                showNewsScreen(testMode = false)
            }

            override fun onLoginFailed(errorCode: Int) {}
        }

        VK.onActivityResult(requestCode, resultCode, data, callback)
    }

    private fun showNewsScreen(testMode: Boolean) {
        val fragment = NewsFragment()
        fragment.arguments = Bundle(1).apply {
            putBoolean(NewsFragment.KEY_TEST_MODE, testMode)
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(container, fragment)
            .addToBackStack(null)
            .commit()
    }
}