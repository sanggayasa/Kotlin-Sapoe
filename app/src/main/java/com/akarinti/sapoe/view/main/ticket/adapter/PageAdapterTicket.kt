package com.akarinti.sapoe.view.main.ticket.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.akarinti.sapoe.view.main.ticket.myticket.MyTicketFragment
import com.akarinti.sapoe.view.main.ticket.ticketall.TicketListFragment


class PageAdapterTicket(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val tabs = listOf(
        MyTicketFragment(),
        TicketListFragment()
    )
    override fun getItem(position: Int): Fragment {
        return tabs[position]
    }

    override fun getCount(): Int {
        return tabs.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Tiket Saya"
            else -> "Semua Tiket"
        }
    }
}