package com.stacktivity.core.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object FragmentManagers {
    /**
     * The [fragment] is added to the container view with id [frameId]. The operation is
     * performed by the [fragmentManager].
     */
    fun addFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment, frameId: Int
    ) {
        fragmentManager.beginTransaction()
            .add(frameId, fragment)
            .commit()
    }

    /**
     * The [fragment] is replaced to the container view with id [frameId]. The operation is
     * performed by the [fragmentManager].
     */
    fun replaceFragment(
        fragmentManager: FragmentManager,
        fragment: Fragment, frameId: Int
    ) {
        fragmentManager.beginTransaction()
            .replace(frameId, fragment)
            .addToBackStack(null)
            .commit()
    }
}