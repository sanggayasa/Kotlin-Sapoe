package com.akarinti.sapoe.view.main.unscheduled.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.akarinti.sapoe.view.main.unscheduled.arsip.UnscheduledArsipFragment
import com.akarinti.sapoe.view.main.unscheduled.onprogress.UnscheduledListFragment
import com.akarinti.sapoe.view.main.unscheduled.unsent.UnscheduledPendingFragment


class PageAdapterUn(fm: FragmentManager): FragmentPagerAdapter(fm) {
    private val tabs = listOf(
        UnscheduledListFragment(),
        UnscheduledPendingFragment(),
        UnscheduledArsipFragment()
    )
    override fun getItem(position: Int): Fragment {
        return tabs[position]
    }

    override fun getCount(): Int {
        return tabs.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Kunjungan"
            1 -> "Belum Terkirim"
            else -> "Arsip"
        }
    }
}