package com.akarinti.sapoe.view.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpFragment
import com.akarinti.sapoe.data.response.NewsListResponse
import com.akarinti.sapoe.event.LocationUpdateEvent
import com.akarinti.sapoe.extension.checkPermissions
import com.akarinti.sapoe.extension.format
import com.akarinti.sapoe.extension.formatDigit
import com.akarinti.sapoe.extension.isNetworkAvailable
import com.akarinti.sapoe.model.MenuCount
import com.akarinti.sapoe.model.ProfileUser
import com.akarinti.sapoe.model.news.News
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.utils.LocationUtils
import com.akarinti.sapoe.view.main.MainActivity
import com.akarinti.sapoe.view.main.home.news.NewsActivity
import dagger.android.support.AndroidSupportInjection
import io.vrinda.kotlinpermissions.DeviceInfo
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.header_default.*
import java.util.*
import javax.inject.Inject

class HomeFragment : BaseMvpFragment<HomePresenter>(), HomeContract.View {
    @Inject
    override lateinit var presenter: HomePresenter

    private var profileUser: ProfileUser? = null
    var locArr:DoubleArray = DoubleArray(2)
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsData: ArrayList<News>
    private var page = 1

    override fun injectView() {
        AndroidSupportInjection.inject(this)
    }

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun setup() {
        profileUser = presenter.headerManager.profileRepository.profile
        initView()
        getCurrentStatus()
        initAction()
        getTaskCount()
        getNewsList()
    }

    private fun initView(){
        val name = profileUser?.name?:"-"
        ivLogo.visibility=View.VISIBLE
        tvRefreshTop.visibility=View.GONE
        tvTitle.visibility=View.GONE
        tvContent.visibility = View.VISIBLE
        tvContent.text = "Hi, $name"

        newsData = arrayListOf()
        newsAdapter = NewsAdapter(newsData)
        newsAdapter.setOnItemClickListener { _, _, pos ->
            NewsActivity.newInstance(requireActivity(), newsAdapter.data[pos])
        }
        newsAdapter.setOnLoadMoreListener(this::onLoadMore, rvNews)

        rvNews.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = newsAdapter
            isNestedScrollingEnabled = false
        }
        swipeLayout.setOnRefreshListener {
            getTaskCount()
            getNewsList()
        }
    }

    private fun initAction(){
        btKunjungan.setOnClickListener {
            getListener()?.onKunjunganPressed()
        }
        btTakTerjadwal.setOnClickListener {
            getListener()?.onUnpredictPressed()
        }
        btTiket.setOnClickListener {
            getListener()?.onTicketPressed()
        }
        tvRefresh.setOnClickListener {
            getCurrentStatus()
            getTaskCount()
            getNewsList()
        }
    }

    override fun getLayout(): Int = R.layout.fragment_home

    private fun getCurrentStatus() {
        if (context?.checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION) == true) {
            (activity as? MainActivity)?.myLoc()?.let {
                locArr = it
                setCurrentStatus()
            }
        } else {
            (activity as? MainActivity)?.requestRequiredPermission()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCurrentStatus() {
        val currentTime = Calendar.getInstance().time
        tvLongContent.text = ": ${DeviceInfo.getLatLong(requireContext())[LocationUtils.LONGTITUDE_INDEX].formatDigit(5)}"
        tvLatContent.text = ": ${DeviceInfo.getLatLong(requireContext())[LocationUtils.LATITUDE_INDEX].formatDigit(5)}"
        tvTanggalContent.text = ": ${currentTime.format("dd/MM/yyyy")}"
        tvWaktuContent.text = ": ${currentTime.format("HH:mm:ss")}"
    }

    private fun getTaskCount() {
        if (requireActivity().isNetworkAvailable()) {
            showLoading()
            presenter.getMenuCount()
        } else {
            swipeLayout.isRefreshing = false
            tabKunjungan.visibility = View.GONE
            tabTakTerjadwal.visibility = View.GONE
            tabTiket.visibility = View.GONE
            tabKosong.visibility = View.VISIBLE
            tvKosong.setText(R.string.no_internet)
        }
    }

    override fun onMenuCount(data: MenuCount?) {
        data?.let {
            tvKunjunganRute.text = String.format(getString(R.string.rute_fmt), it.scheduled)
            tabKunjungan.visibility = if (it.scheduled > 0) View.VISIBLE else View.GONE
            tvTakTerjadwalRute.text = String.format(getString(R.string.rute_fmt), it.unscheduledVisit)
            tabTakTerjadwal.visibility = if (it.unscheduledVisit > 0) View.VISIBLE else View.GONE
            tvTiketRute.text = String.format(getString(R.string.ticket_fmt), it.ticket)
            tabTiket.visibility = if (it.ticket > 0) View.VISIBLE else View.GONE
            tabKosong.visibility = if (it.scheduled == 0 && it.unscheduledVisit == 0 && it.ticket == 0) View.VISIBLE else View.GONE
        }
        swipeLayout.isRefreshing = false
        dismissLoading()
    }

    private fun getNewsList() {
        if (requireActivity().isNetworkAvailable()) {
            showLoading()
            newsAdapter.emptyView = LayoutInflater.from(context).inflate(R.layout.empty_news, rvNews, false)
            page = 1
            presenter.getNews()
        } else {
            swipeLayout.isRefreshing = false
            newsAdapter.emptyView = LayoutInflater.from(context).inflate(R.layout.empty_news_offline, rvNews, false)
            newsData.clear()
            newsAdapter.notifyDataSetChanged()
        }
    }

    private fun onLoadMore() {
        page++
        Handler().postDelayed({ presenter.getNews(page = page) }, 1000)
    }

    override fun onNews(dataList: NewsListResponse) {
        if (page == 1)
            newsData.clear()

        dataList.data?.list?.let {
            newsData.addAll(it)
        }
        newsAdapter.notifyDataSetChanged()
        var loadMore = false
        dataList.data?.pagination?.let {
            page = it.current_page
            loadMore = page < it.totalPage
        }
        if (newsAdapter.isLoadMoreEnable) newsAdapter.loadMoreComplete()
        newsAdapter.setEnableLoadMore(loadMore)
        dismissLoading()
        swipeLayout.isRefreshing = false
    }

    private fun getListener(): Listener? {
        var listener = parentFragment as? Listener
        if(null == listener) listener = activity as? Listener
        return listener
    }

    interface Listener {
        fun onKunjunganPressed()
        fun onUnpredictPressed()
        fun onTicketPressed()
    }

    override fun initSubscription() {
        super.initSubscription()
        addUiSubscription(RxBus.listen(LocationUpdateEvent::class.java).subscribe {
            locArr = it.location
            setCurrentStatus()
        })
    }
}