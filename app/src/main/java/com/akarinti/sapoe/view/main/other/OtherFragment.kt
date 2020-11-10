package com.akarinti.sapoe.view.main.other

import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.BuildConfig
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpFragment
import com.akarinti.sapoe.model.Version
import com.akarinti.sapoe.view.component.dialog.ConfirmDialogFragment
import com.akarinti.sapoe.view.component.dialog.FinisDialogFragment
import com.akarinti.sapoe.view.main.other.adapter.MenuAdapter
import com.akarinti.sapoe.view.main.other.gallery.GalleryPhotoActivity
import com.akarinti.sapoe.view.main.other.send_history.SendHistoryActivity
import com.akarinti.sapoe.view.main.other.send_location.SendLocationActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_other.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject

class OtherFragment : BaseMvpFragment<OtherPresenter>(), OtherContract.View, ConfirmDialogFragment.Listener {
    override fun onLogout() {
        forceLogout()
    }

    @Inject
    override lateinit var presenter: OtherPresenter

    override fun injectView() {
        AndroidSupportInjection.inject(this)
    }

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun setup() {
        initAction()
        initView()
        initMenu()
    }

    override fun getLayout(): Int = R.layout.fragment_other

    private fun initAction() {
        btLogout.setOnClickListener {
            fragmentManager?.let {
                ConfirmDialogFragment.apply {
                    newInstance("", getString(R.string.logout_confirm)
                        , getString(R.string.ya), getString(R.string.batal)
                    ).show(it, ConfirmDialogFragment::class.java.canonicalName)
                    listener = this@OtherFragment
                }
            }
        }
    }

    private fun initView() {
        tvVersion.text = ("App Version v${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})")
    }

    private fun initMenu() {
        val menuAdapter = MenuAdapter(
            listOf(
                getString(R.string.kirim_history),
                getString(R.string.kirim_lokasi),
                getString(R.string.galeri_foto),
                getString(R.string.update_versi)
            )
        )

        menuAdapter.setOnItemClickListener { _, _, position ->
            when (position) {
                0 -> startActivity(activity?.intentFor<SendHistoryActivity>())
                1 -> startActivity(activity?.intentFor<SendLocationActivity>())
                2 -> startActivity(activity?.intentFor<GalleryPhotoActivity>())
                3 -> presenter.checkVersion()
            }
        }
        rvMenu.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = menuAdapter
            isNestedScrollingEnabled = false
        }
    }

    override fun onCancelDialogBtnPressed(tag: String?) {
    }

    override fun onConfirmDialogBtnPressed(tag: String?) {
            presenter.logoutUser()

    }

    override fun onConfirmDialogDismissed() {
    }

    override fun onVersionCheck(data: Version?) {
        super.onVersionCheck(data)
        if (data?.updateApp == false) {
            fragmentManager?.let {
                FinisDialogFragment.newInstance(getString(R.string.no_update)
                    , getString(R.string.ok)
                ).show(it, FinisDialogFragment::class.java.canonicalName)
            }
        }
    }
}