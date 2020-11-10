package com.akarinti.sapoe.view.main.ticket.add.fragment

import android.app.Dialog
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseDialogFragment
import com.akarinti.sapoe.event.SlaFetchUpdateEvent
import com.akarinti.sapoe.model.ticket.Spareparts
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.utils.KeyboardUtil
import com.akarinti.sapoe.view.main.ticket.adapter.SparepartsListAdapter
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.dialog_route.*
import kotlinx.android.synthetic.main.dialog_route_internal.*


class SearchSparepartsListFragment : BaseDialogFragment() {

    companion object {
        fun newInstance(): SearchSparepartsListFragment = SearchSparepartsListFragment()
    }

    var listener: Listener? = null

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
    }

    override fun getLayout(): Int = R.layout.dialog_route

    private fun initView() {
        tvLokasiRute.setText(R.string.pilih_parts_hint_t)
        etSearch.setHint(R.string.ketik_jenis_sparepart)
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
            val catAdapter = SparepartsListAdapter(it.getSpareparts())
            catAdapter.setOnItemClickListener { _, _, position ->
                it.selectedSpareparts(catAdapter.data[position])
                dismiss()
            }
            rvRoute.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = catAdapter
                isNestedScrollingEnabled = false
            }
            it.searchSpareparts("")
        }
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
                    this@SearchSparepartsListFragment.dismiss()
                }
            }
        })
    }

    private fun initBinding() {
//        addUiSubscription(etSearch.textChanges().debounce(2000, TimeUnit.MILLISECONDS)
//            .skip(1)
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe { query ->
//                listener?.searchSpareparts(query.toString())
//            })
        etSearch.setOnEditorActionListener { view, id, _ ->
            return@setOnEditorActionListener when (id) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    listener?.searchSpareparts(view.text.toString())
                    true
                }
                else -> false
            }
        }
    }

    override fun initSubscription() {
        super.initSubscription()
        addUiSubscription(RxBus.listen(SlaFetchUpdateEvent::class.java).subscribe {
            listener?.let {
                (rvRoute?.adapter as? SparepartsListAdapter)?.setNewData(it.getSpareparts())
            }
        })
    }

    interface Listener {
        fun getSpareparts(): ArrayList<Spareparts>
        fun selectedSpareparts(selected: Spareparts)
        fun searchSpareparts(query: String)
    }
}