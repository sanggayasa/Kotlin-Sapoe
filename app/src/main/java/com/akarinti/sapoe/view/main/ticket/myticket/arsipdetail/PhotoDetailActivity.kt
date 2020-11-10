package com.akarinti.sapoe.view.main.ticket.myticket.arsipdetail

import android.app.Activity
import android.view.View
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseActivity
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.utils.GlideApp
import kotlinx.android.synthetic.main.activity_picture_question.*
import kotlinx.android.synthetic.main.sticky_button.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import org.jetbrains.anko.intentFor

class PhotoDetailActivity: BaseActivity() {

    companion object {
        fun newInstance(activity: Activity, image: String, title: String, lat: Double?, long: Double?) {
            activity.startActivity(activity.intentFor<PhotoDetailActivity>(Params.BUNDLE_IMAGE_URL to image,
                Params.BUNDLE_TEXT to title, Params.BUNDLE_LAT to lat, Params.BUNDLE_LONG to long))
        }
    }

    override fun injectView() {
    }

    override fun setup() {
        initBundle()
        initView()
        initAction()
    }

    override fun getLayout(): Int = R.layout.activity_picture_question

    private fun initBundle() {
        intent?.extras?.let { it ->
            tvTitle.text = it.getString(Params.BUNDLE_TEXT)
            GlideApp.with(this)
                .load(it.getString(Params.BUNDLE_IMAGE_URL))
                .into(ivResult)
            tvLatValue.text = "${(it.getDouble(Params.BUNDLE_LAT))}"
            tvLongValue.text = "${it.getDouble(Params.BUNDLE_LONG)}"
        }
    }

    private fun initView() {
        btnAction.setText(R.string.tutup)
        btnAmbil.visibility = View.INVISIBLE
        ivIcon.visibility = View.INVISIBLE
        btnFotoUlang.visibility = View.INVISIBLE
    }

    private fun initAction() {
        btnAction.setOnClickListener { onBackPressed() }
        ivBack.setOnClickListener { onBackPressed() }
    }
}