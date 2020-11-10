package com.akarinti.sapoe.view.main.ticket.add.spareparts.foto

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class CameraAfterAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    private val tabs = listOf(
        CameraAfterFragment()
    )
    override fun getItem(position: Int): Fragment {
        return tabs[position]
    }

    override fun getCount(): Int {
        return tabs.size
    }
}