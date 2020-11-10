package com.akarinti.sapoe.view.main

import com.akarinti.sapoe.view.main.home.HomeFragment
import com.akarinti.sapoe.view.main.other.OtherFragment
import com.akarinti.sapoe.view.main.ticket.myticket.MyTicketFragment
import com.akarinti.sapoe.view.main.ticket.TicketFragment
import com.akarinti.sapoe.view.main.ticket.add.spareparts.foto.CameraAfterFragment
import com.akarinti.sapoe.view.main.ticket.add.spareparts.foto.CameraBeforeFragment
import com.akarinti.sapoe.view.main.ticket.ticketall.TicketListFragment
import com.akarinti.sapoe.view.main.unscheduled.UnscheduledFragment
import com.akarinti.sapoe.view.main.unscheduled.arsip.UnscheduledArsipFragment
import com.akarinti.sapoe.view.main.unscheduled.onprogress.UnscheduledListFragment
import com.akarinti.sapoe.view.main.unscheduled.unsent.UnscheduledPendingFragment
import com.akarinti.sapoe.view.main.visit.VisitFragment
import com.akarinti.sapoe.view.main.visit.arsip.VisitArsipFragment
import com.akarinti.sapoe.view.main.visit.onprogress.VisitListFragment
import com.akarinti.sapoe.view.main.visit.unsent.VisitPendingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
open abstract class MainProvider {
    @ContributesAndroidInjector
    abstract fun provideOtherFragment(): OtherFragment

    @ContributesAndroidInjector
    abstract fun provideHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun provideVisitFragment(): VisitFragment

    @ContributesAndroidInjector
    abstract fun provideUnscheduledFragment(): UnscheduledFragment

    @ContributesAndroidInjector
    abstract fun provideTicketFragment(): TicketFragment

    @ContributesAndroidInjector
    abstract fun provideVisitArsipFragment(): VisitArsipFragment

    @ContributesAndroidInjector
    abstract fun provideVisitPendingFragment(): VisitPendingFragment

    @ContributesAndroidInjector
    abstract fun provideVisitListFragment(): VisitListFragment

    @ContributesAndroidInjector
    abstract fun provideUnscheduledArsipFragment(): UnscheduledArsipFragment

    @ContributesAndroidInjector
    abstract fun provideUnscheduledPendingFragment(): UnscheduledPendingFragment

    @ContributesAndroidInjector
    abstract fun provideUnscheduledListFragment(): UnscheduledListFragment

    @ContributesAndroidInjector
    abstract fun provideMyTicketFragment(): MyTicketFragment

    @ContributesAndroidInjector
    abstract fun provideTicketListFragment(): TicketListFragment
}