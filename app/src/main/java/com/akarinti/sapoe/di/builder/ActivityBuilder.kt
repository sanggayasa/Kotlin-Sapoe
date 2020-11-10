package com.akarinti.sapoe.di.builder

import com.akarinti.sapoe.view.login.LoginActivity
import com.akarinti.sapoe.view.main.MainActivity
import com.akarinti.sapoe.view.main.MainProvider
import com.akarinti.sapoe.view.main.camera.CameraFragment
import com.akarinti.sapoe.view.main.home.news.NewsActivity
import com.akarinti.sapoe.view.main.other.gallery.GalleryPhotoActivity
import com.akarinti.sapoe.view.main.other.send_history.SendHistoryActivity
import com.akarinti.sapoe.view.main.other.send_location.SendLocationActivity
import com.akarinti.sapoe.view.main.ticket.add.TiketActivity
import com.akarinti.sapoe.view.main.ticket.add.foto.FotoAfterActivity
import com.akarinti.sapoe.view.main.ticket.add.foto.FotoBeforeActivity
import com.akarinti.sapoe.view.main.ticket.add.info.InfoTiketActivity
import com.akarinti.sapoe.view.main.ticket.add.spareparts.SparepartsActivity
import com.akarinti.sapoe.view.main.ticket.add.spareparts.foto.CameraAfterFragment
import com.akarinti.sapoe.view.main.ticket.add.spareparts.foto.CameraBeforeFragment
import com.akarinti.sapoe.view.main.ticket.myticket.arsip.ArsipTicketActivity
import com.akarinti.sapoe.view.main.unscheduled.answer_parent.UnscheduleAnswerParentActivity
import com.akarinti.sapoe.view.main.unscheduled.answer_parent.child.UnscheduleAnswerChildActivity
import com.akarinti.sapoe.view.main.unscheduled.answer_parent.question.UAnswerActivity
import com.akarinti.sapoe.view.main.unscheduled.rute.RuteActivity
import com.akarinti.sapoe.view.main.unscheduled.unschedule_parent.UnscheduleParentActivity
import com.akarinti.sapoe.view.main.unscheduled.unschedule_parent.child.UnscheduleChildActivity
import com.akarinti.sapoe.view.main.unscheduled.unschedule_parent.question.UQuestionActivity
import com.akarinti.sapoe.view.main.visit.answer_parent.AnswerParentActivity
import com.akarinti.sapoe.view.main.visit.answer_parent.child.AnswerChildActivity
import com.akarinti.sapoe.view.main.visit.answer_parent.question.SAnswerActivity
import com.akarinti.sapoe.view.main.visit.suhu.SuhuActivity
import com.akarinti.sapoe.view.main.visit.visit_parent.VisitParentActivity
import com.akarinti.sapoe.view.main.visit.visit_parent.child.VisitChildActivity
import com.akarinti.sapoe.view.main.visit.visit_parent.question.SQuestionActivity
import com.akarinti.sapoe.view.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder{

    @ContributesAndroidInjector
    abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [MainProvider::class])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindVisitParentActivity(): VisitParentActivity

    @ContributesAndroidInjector
    abstract fun bindFotoBeforeActivity(): FotoBeforeActivity

    @ContributesAndroidInjector
    abstract fun bindFotoAfterActivity(): FotoAfterActivity

    @ContributesAndroidInjector
    abstract fun bindNewsActivity(): NewsActivity

    @ContributesAndroidInjector
    abstract fun bindRuteActivity(): RuteActivity

    @ContributesAndroidInjector
    abstract fun bindTiketActivity(): TiketActivity

    @ContributesAndroidInjector
    abstract fun bindArsipTicketActivity(): ArsipTicketActivity

    @ContributesAndroidInjector
    abstract fun provideCameraFragment(): CameraFragment

    @ContributesAndroidInjector
    abstract fun bindInfoTiketActivity(): InfoTiketActivity

    @ContributesAndroidInjector
    abstract fun bindSparepartsActivity(): SparepartsActivity

    @ContributesAndroidInjector
    abstract fun bindSendLocationActivity(): SendLocationActivity

    @ContributesAndroidInjector
    abstract fun bindSuhuActivity(): SuhuActivity

    @ContributesAndroidInjector
    abstract fun bindUnscheduleParentActivity(): UnscheduleParentActivity

    @ContributesAndroidInjector
    abstract fun bindUnscheduleChildInfoActivity(): UnscheduleChildActivity

    @ContributesAndroidInjector
    abstract fun bindVisitChildActivity(): VisitChildActivity

    @ContributesAndroidInjector
    abstract fun bindAnswerParentActivity(): AnswerParentActivity

    @ContributesAndroidInjector
    abstract fun bindAnswerChildActivity(): AnswerChildActivity

    @ContributesAndroidInjector
    abstract fun bindUnscheduleAnswerParentActivity(): UnscheduleAnswerParentActivity

    @ContributesAndroidInjector
    abstract fun bindUnscheduleAnswerChildActivity(): UnscheduleAnswerChildActivity

    @ContributesAndroidInjector
    abstract fun provideCameraBeforeFragment(): CameraBeforeFragment

    @ContributesAndroidInjector
    abstract fun provideCameraAfterFragment(): CameraAfterFragment

    @ContributesAndroidInjector
    abstract fun provideGalleryPhotoActivity(): GalleryPhotoActivity

    @ContributesAndroidInjector
    abstract fun provideUQuestionActivity(): UQuestionActivity

    @ContributesAndroidInjector
    abstract fun provideUAnswerActivity(): UAnswerActivity

    @ContributesAndroidInjector
    abstract fun provideSQuestionActivity(): SQuestionActivity

    @ContributesAndroidInjector
    abstract fun provideSAnswerActivity(): SAnswerActivity

    @ContributesAndroidInjector
    abstract fun provideSendHistoryActivity(): SendHistoryActivity
}