package com.akarinti.sapoe.view.main.camera

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class CameraAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    private val tabs = listOf(
        CameraFragment()
    )
    override fun getItem(position: Int): Fragment {
        return tabs[position]
    }

    override fun getCount(): Int {
        return tabs.size
    }
}