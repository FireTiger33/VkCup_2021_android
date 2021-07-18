package com.stacktivity.vkcup2021

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import com.stacktivity.vkcup2021.databinding.MainActivityBinding
import com.stacktivity.vkcup2021.ui.main.MainFragment
import com.stacktivity.core.utils.FragmentManagers.replaceFragment

class MainActivity : AppCompatActivity() {
    private val binding: MainActivityBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        initUI(savedInstanceState)
    }

    private fun initUI(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            replaceFragment(supportFragmentManager, MainFragment(), binding.container.id)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        supportFragmentManager.findFragmentById(binding.container.id)
            ?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            finish()
        }
    }
}