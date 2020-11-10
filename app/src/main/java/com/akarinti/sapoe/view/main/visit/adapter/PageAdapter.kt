package com.akarinti.sapoe.view.main.visit.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.akarinti.sapoe.view.main.visit.arsip.VisitArsipFragment
import com.akarinti.sapoe.view.main.visit.onprogress.VisitListFragment
import com.akarinti.sapoe.view.main.visit.unsent.VisitPendingFragment


class PageAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    private val tabs = listOf(
        VisitListFragment(),
        VisitPendingFragment(),
        VisitArsipFragment()
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