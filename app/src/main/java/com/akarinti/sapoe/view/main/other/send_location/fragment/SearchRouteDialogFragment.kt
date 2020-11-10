package com.akarinti.sapoe.view.main.other.send_location.fragment

import android.app.Dialog
import android.os.Handler
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseDialogFragment
import com.akarinti.sapoe.data.response.LocationListResponse
import com.akarinti.sapoe.event.RouteFethUpdateEvent
import com.akarinti.sapoe.model.Route
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.utils.KeyboardUtil
import com.akarinti.sapoe.view.main.other.send_location.adapter.RouteAdapter
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.dialog_route.*
import kotlinx.android.synthetic.main.dialog_route_internal.*

class SearchRouteDialogFragment : BaseDialogFragment() {

    companion object {
        fun newInstance(): SearchRouteDialogFragment = SearchRouteDialogFragment()
    }

    var listener: Listener? = null
    private lateinit var routeAdapter: RouteAdapter
    private lateinit var mData: ArrayList<Route>
    private var page = 1
    private var query: String = ""

    override fun setupDialogStyle(dialog: Dialog) {
        dialog.window?.attributes?.windowAnimations = R.style.SlideAnimation
    }

    override fun loadArguments() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DefaultDialog_Fullscreen)
    }

    override fun setup() {
        initView()
        initAction()
        initBinding()
        setData()
    }

    override fun getLayout(): Int = R.layout.dialog_route

    private fun initView() {
        activity?.let { a ->
            view?.let { v ->
                KeyboardUtil(a, v)
            }
        }
        if (parentFragment is Listener) {
            listener = parentFragment as? Listener
        } else if (activity is Listener) {
            listener = activity as? Listener
        }

        listener?.let {
            mData = arrayListOf()
            routeAdapter = RouteAdapter(mData)
            routeAdapter.setOnItemClickListener { _, _, position ->
                it.selectedRoute(routeAdapter.data[position])
                dismiss()
            }
            routeAdapter.setOnLoadMoreListener(this::onLoadMore, rvRoute)
            rvRoute.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = routeAdapter
                isNestedScrollingEnabled = false
            }
            it.getRoutePager(query = "", page =  1)
        }
    }

    private fun onLoadMore() {
        page++
        Handler().postDelayed({ listener?.getRoutePager(query = query, page = page) }, 500)
    }

    private fun initAction() {
        mainLayout.setOnClickListener { dismiss() }
        ivClose.setOnClickListener { dismiss() }
        ivClear.setOnClickListener { etSearch.text.clear() }
        slidingUp.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {}
            override fun onPanelStateChanged(panel: View?,
                                             previousState: SlidingUpPanelLayout.PanelState?,
                                             newState: SlidingUpPanelLayout.PanelState?) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    this@SearchRouteDialogFragment.dismiss()
                }
            }
        })
    }

    private fun initBinding() {
//        addUiSubscription(etSearch.textChanges().debounce(2000, TimeUnit.MILLISECONDS)
//            .skip(1)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe { query ->
//                listener?.searchLocation(query.toString())
//            })
        etSearch.setOnEditorActionListener { view, id, _ ->
            return@setOnEditorActionListener when (id) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    query = view.text.toString()
                    listener?.getRoutePager(query = view.text.toString(), page = 1)
                    true
                }
                else -> false
            }
        }
    }

    override fun initSubscription() {
        super.initSubscription()
        addUiSubscription(RxBus.listen(RouteFethUpdateEvent::class.java).subscribe {
            setData()
        })
    }

    private fun setData() {
        listener?.let {
            if (page == 1)
                mData.clear()

            it.getResponse()?.list?.let { listRoute ->
                mData.addAll(listRoute)
            }
            routeAdapter.notifyDataSetChanged()
            var loadMore = false
            it.getResponse()?.pagination?.let { pager ->
                page = pager.current_page
                loadMore = page < pager.totalPage
            }
            if (routeAdapter.isLoadMoreEnable) routeAdapter.loadMoreComplete()
            routeAdapter.setEnableLoadMore(loadMore)
        }
    }

    interface Listener {
        fun getRoutePager(query: String = "", page: Int = 1)
        fun getResponse(): LocationListResponse.LocationListData?
        fun selectedRoute(selected: Route)
    }
}