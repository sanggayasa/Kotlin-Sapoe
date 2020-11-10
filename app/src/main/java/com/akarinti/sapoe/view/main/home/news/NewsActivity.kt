package com.akarinti.sapoe.view.main.home.news

import android.app.Activity
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseActivity
import com.akarinti.sapoe.extension.dateFormat
import com.akarinti.sapoe.extension.toHtml
import com.akarinti.sapoe.model.news.News
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.utils.GlideApp
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import org.jetbrains.anko.intentFor
import java.util.*

class NewsActivity : BaseActivity() {

    companion object {
        fun newInstance(activity: Activity, news: News) {
            activity.startActivity(activity.intentFor<NewsActivity>(
                Params.BUNDLE_NEWS to news
            ))
        }
    }

    private var data: News? = null

    override fun injectView() {
        AndroidInjection.inject(this)
    }

    override fun setup() {
        initBundle()
        initView()
        initAction()
    }

    override fun getLayout(): Int = R.layout.activity_news

    private fun initBundle() {
        intent?.extras?.let {
            data = it.getParcelable(Params.BUNDLE_NEWS)
        }

        data?.let {
            var date = "-"
            it.publishAt?.let { input ->
                date = (input*1000).dateFormat(pattern = "dd/MM/yyyy, HH:mm", locale = Locale("id", "ID"))
            }
            tvDate.text = date
            tvNewsTitle.text = it.title
            tvNewsContent.text = it.content.toHtml()
            GlideApp.with(this)
                .load(it.image)
                .fitCenter()
                .into(ivImage)
        }
    }

    private fun initView() {
        tvTitle.text = getString(R.string.berita_acara)
    }

    private fun initAction() {
        ivBack.setOnClickListener { onBackPressed() }
    }

//    private fun getFormattedDate(inputDate: String): String {
//        return if (DateUtils.isToday(inputDate.toDate().time)) {
//            "Hari ini ${inputDate.dateFormat(output = "HH:mm")}"
//        } else if (DateUtils.isToday(inputDate.toDate().time + DateUtils.DAY_IN_MILLIS)) {
//            "Kemarin ${inputDate.dateFormat(output = "HH:mm")}"
//        } else {
//            inputDate.dateFormat(output = "EEEE, HH:mm", outputLocale = Locale("id", "ID"))
//        }
//    }
}