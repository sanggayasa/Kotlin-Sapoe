package com.akarinti.sapoe.view.main.other.gallery

import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.model.dummy.GallerySection
import com.akarinti.sapoe.view.component.GridItemDecoration
import com.akarinti.sapoe.view.main.other.gallery.adapter.GalleryAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_gallery_photo.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import javax.inject.Inject

class GalleryPhotoActivity: BaseMvpActivity<GalleryPhotoPresenter>(), GalleryPhotoContract.View {


    @Inject
    override lateinit var presenter: GalleryPhotoPresenter
    private lateinit var mAdapter: GalleryAdapter
    private var mDataList: List<GallerySection> = listOf()

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun injectView() {
        AndroidInjection.inject(this)
    }

    override fun setup() {
        initView()
        initAction()
    }

    override fun getLayout(): Int = R.layout.activity_gallery_photo

    private fun initAction() {
        ivBack.setOnClickListener { onBackPressed() }
        ivAction.setOnClickListener { csSorting.performClick() }
    }

    private fun initView() {
        tvTitle.text = getString(R.string.galeri_foto)
        ivAction.apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_sort)
        }
        mDataList = presenter.getPhotoList(false)
        mAdapter = GalleryAdapter(mDataList)

        rvPhoto.apply {
            layoutManager = GridLayoutManager(this@GalleryPhotoActivity,3)
            adapter = mAdapter
            isNestedScrollingEnabled = false
            addItemDecoration(GridItemDecoration(resources.getDimensionPixelSize(R.dimen._3sdp)))
        }

        (rvPhoto.adapter as GalleryAdapter).emptyView = LayoutInflater.from(this).inflate(R.layout.empty_photo, rvPhoto, false)

        val sortAdapter = ArrayAdapter(this, R.layout.item_spinner_divider, R.id.tvSpinnerItem,
            arrayListOf(getString(R.string.urut_terbaru), getString(R.string.urut_terdahulu)))
        csSorting.apply {
            adapter = sortAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    when (position) {
                        0 -> {
                            mDataList = presenter.getPhotoList(false)
                        }
                        1 -> {
                            mDataList = presenter.getPhotoList(true)
                        }
                    }
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun showLoadingState() {
        showLoading()
    }

    override fun dismissLoadingState() {
        dismissLoading()
    }
}