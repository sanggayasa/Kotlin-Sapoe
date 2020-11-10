package com.akarinti.sapoe.view.main.ticket.add


import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.data.body.TicketCreateBody
import com.akarinti.sapoe.data.response.LocationListResponse
import com.akarinti.sapoe.event.RouteFethUpdateEvent
import com.akarinti.sapoe.event.SlaFetchUpdateEvent
import com.akarinti.sapoe.event.TicketRefreshEvent
import com.akarinti.sapoe.extension.getLat
import com.akarinti.sapoe.extension.getLong
import com.akarinti.sapoe.model.Route
import com.akarinti.sapoe.model.ticket.LocationComponent
import com.akarinti.sapoe.model.ticket.TicketSla
import com.akarinti.sapoe.model.ticket.TicketType
import com.akarinti.sapoe.objects.CodeIntent
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.view.component.dialog.ConfirmDialogFragment
import com.akarinti.sapoe.view.component.dialog.ErrorDialogFragment
import com.akarinti.sapoe.view.component.dialog.FinisDialogFragment
import com.akarinti.sapoe.view.main.MainActivity
import com.akarinti.sapoe.view.main.camera.CameraAdapter
import com.akarinti.sapoe.view.main.camera.CameraFragment
import com.akarinti.sapoe.view.main.other.send_location.fragment.SearchRouteDialogFragment
import com.akarinti.sapoe.view.main.ticket.add.fragment.SearchTicketSlaDialogFragment
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_buat_tiket.*
import kotlinx.android.synthetic.main.button_navigation.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject


class TiketActivity : BaseMvpActivity<TiketPresenter>(), TiketContract.View, ConfirmDialogFragment.Listener,
    SearchRouteDialogFragment.Listener, CameraFragment.Listener, FinisDialogFragment.Listener, ErrorDialogFragment.Listener, SearchTicketSlaDialogFragment.Listener {

    companion object {
        fun newInstance(activity: Activity, orderID: String?, orderType: String?, scheduleType: String?, itemId:String?) {
            activity.startActivityForResult(activity.intentFor<TiketActivity>(
                Params.BUNDLE_ORDER_ID to orderID,
                Params.BUNDLE_ORDER_TYPE to orderType,
                Params.BUNDLE_TICKET_TYPE to scheduleType,
                Params.BUNDLE_TICKET_ITEMID to itemId
            ), CodeIntent.OPEN_QUESTION)
        }
        fun newInstance(activity: Activity, orderID: String?, orderType: String?, scheduleType: String?, itemId:String?, selectedRoute: Route) {
            activity.startActivityForResult(activity.intentFor<TiketActivity>(
                Params.BUNDLE_ORDER_ID to orderID,
                Params.BUNDLE_ORDER_TYPE to orderType,
                Params.BUNDLE_TICKET_TYPE to scheduleType,
                Params.BUNDLE_TICKET_ITEMID to itemId,
                Params.BUNDLE_ROUTE to selectedRoute
            ), CodeIntent.OPEN_QUESTION)
        }
    }
    var imageFill: String = ""
    var locations = ArrayList<Route>()
    var ticketSla = ArrayList<TicketSla>()
    var mData: LocationListResponse.LocationListData? = null

    private var orderID: String? = null
    private var orderType: String? = null
    private var itemId: String? = null
    lateinit var selectedRoute: Route
    lateinit var selectedSla: TicketSla
    lateinit var selectedType: TicketType
    lateinit var selectedCat: LocationComponent
    private var scheduleType: String? = null
    private var childIndex: Int = -1
    private var currentRoute: Route? = null

    @Inject
    override lateinit var presenter: TiketPresenter

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun injectView() {
        AndroidInjection.inject(this)
    }

    override fun setup() {
        initBundle()
        initView()
        initAction()
        initData()
    }

    override fun getLayout(): Int = R.layout.activity_buat_tiket

    private fun initView() {
        vpCamera.adapter = CameraAdapter(supportFragmentManager)
        tvTitle.setText(R.string.buat_tiket)
        btnSimpan.text = getString(R.string.lapor)
        spCat.isEnabled = false
        spCat.alpha = 0.4f
        spJenis.isEnabled = false
        spJenis.alpha = 0.4f
        checkButton()
    }
    private fun initBundle(){
        intent?.extras?.let {
            orderID = it.getString(Params.BUNDLE_ORDER_ID)
            orderType = it.getString(Params.BUNDLE_ORDER_TYPE)
            itemId = it.getString(Params.BUNDLE_TICKET_ITEMID)
            scheduleType = it.getString(Params.BUNDLE_TICKET_TYPE)
            childIndex = it.getInt(Params.BUNDLE_TICKET_CHILD_IDX)
            currentRoute = it.getParcelable(Params.BUNDLE_ROUTE)
        }
    }

    private fun initAction() {
        ivBack.setOnClickListener { onBackPressed() }
        btnSimpan.setOnClickListener {
            ConfirmDialogFragment.newInstance(Params.TAG_TICKET, getString(R.string.yakin_lapor_tiket)
                , getString(R.string.lapor), getString(R.string.batal))
                .show(supportFragmentManager, ConfirmDialogFragment::class.java.canonicalName)
        }
        tvLocation.setOnClickListener {
            SearchRouteDialogFragment.newInstance()
                .show(supportFragmentManager, SearchRouteDialogFragment::class.java.canonicalName)
        }
        spCat.setOnClickListener { spKategoriTicket.performClick() }
        spType.setOnClickListener { spTypeTicket.performClick() }
        spJenis.setOnClickListener {
            SearchTicketSlaDialogFragment.newInstance()
                .show(supportFragmentManager, SearchTicketSlaDialogFragment::class.java.canonicalName)
        }
    }

    private fun initData() {
        showLoading()
        presenter.getLocationList("")
        presenter.getTicketTypeList()
    }

    // Lokasi Rute
    override fun onLocationSet(data: LocationListResponse.LocationListData) {
        dismissLoading()
        mData = data
        RxBus.publish(RouteFethUpdateEvent())
        currentRoute?.let { selectedRoute(it) }
    }

    override fun getRoutePager(query: String, page: Int) {
        presenter.getLocationList(queryInput = query, page = page)
    }

    override fun getResponse(): LocationListResponse.LocationListData? = mData

    override fun selectedRoute(selected: Route) {
        if (null != selected.lat && null != selected.long) {
            selectedRoute = selected
            tvLocation.text = selected.name
            selected.id?.let {
                showLoading()
                presenter.getCatTicketList(it)
            }
        } else {
            tvLocation.setText(R.string.pilih_rute_hint)
            spCat.isEnabled = false
            spCat.alpha = 0.4f
            spCat.setText(R.string.pilih_cat_tiket)
            ErrorDialogFragment.newInstance(Params.TAG_CREATE, getString(R.string.need_location_request), getString(R.string.ok_mengerti))
                .show(supportFragmentManager, ErrorDialogFragment::class.java.canonicalName)
        }
        checkButton()
    }

    // Kategori Ticket
    override fun onCatTicket(list: List<LocationComponent>?) {
        list?.let {
            val kategoriTicket = ArrayAdapter(this, R.layout.item_spinner_divider_small, R.id.tvSpinnerItem, it.map { item -> item.name })
            spKategoriTicket.apply {
                adapter = kategoriTicket
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        spCat.text = kategoriTicket.getItem(p2)
                        selectedCat = it[p2]
                        checkButton()
                    }
                }
            }
        }
        spCat.isEnabled = (list ?: listOf()).isNotEmpty()
        spCat.alpha = if ((list ?: listOf()).isNotEmpty()) 1f else 0.4f
        spCat.setText(R.string.pilih_cat_tiket)
        dismissLoading()
    }

    // Tipe Ticket
    override fun onTicketType(list: List<TicketType>?) {
        list?.let {
            val typeTicketAdapter = ArrayAdapter(this, R.layout.item_spinner_divider_small, R.id.tvSpinnerItem, it.map { item -> item.name })
            spTypeTicket.apply {
                adapter = typeTicketAdapter
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        spType.text = typeTicketAdapter.getItem(p2)
                        selectedType = it[p2]
                        checkButton()
                        showLoading()
                        presenter.getTicketSlaList(it[p2].name, "")
                    }
                }
            }
        }
        dismissLoading()
    }

    // Jenis Ticket - SLA
    override fun onTicketSla(list: List<TicketSla>?) {
        list?.let {
            ticketSla = ArrayList(it)
        }
        spJenis.isEnabled = (list?: listOf()).isNotEmpty()
        spJenis.alpha = if ((list ?: listOf()).isNotEmpty()) 1f else 0.4f
        spJenis.setText(R.string.pilih_jenis_tiket)
        dismissLoading()
        RxBus.publish(SlaFetchUpdateEvent())
    }

    override fun getSla(): ArrayList<TicketSla> = ticketSla

    override fun selectedSla(selected: TicketSla) {
        selectedSla = selected
        spJenis.text = selected.description
        checkButton()
    }

    override fun searchSla(query: String) {
        showLoading()
        presenter.getTicketSlaList(selectedType.name, query)
    }

    override fun gotoNextPage() {
        dismissLoading()
        btnSimpan.isEnabled = true
        RxBus.publish(TicketRefreshEvent())
        FinisDialogFragment.newInstance(getString(R.string.sukses_tiket), getString(R.string.ok), R.drawable.popup_success)
            .show(supportFragmentManager, FinisDialogFragment::class.java.canonicalName)
    }

    override fun errorScreen(message: String?) {
        checkButton()
        super.errorScreen(message)
    }

    override fun errorConnection() {
        checkButton()
        super.errorConnection()
    }

    private fun checkButton() {
        btnSimpan.isEnabled = (tvLocation.text != getString(R.string.pilih_rute_hint)&&
                spCat.text!=getString(R.string.pilih_cat_tiket)&&
                spType.text!=getString(R.string.pilih_type_tiket) &&
                spJenis.text!=getString(R.string.pilih_jenis_tiket) &&
                imageFill!="")
    }

    override fun getCamera(imagefile: String) {
        imageFill = imagefile
        checkButton()
    }
    override fun onFinishDialogBtnPressed() {
        if(orderID.isNullOrEmpty()) {
            startActivity(intentFor<MainActivity>())
            finishAffinity()
        }
        else
        {
            setResult(Activity.RESULT_OK)
            finish()
        }
        dismissLoading()
    }

    override fun onCancelDialogBtnPressed(tag: String?) {
        //finish()
    }

    override fun onConfirmDialogBtnPressed(tag: String?) {
        if (null != selectedRoute && null != selectedCat && null != selectedType && null != selectedSla) {
            btnSimpan.isEnabled = false
            showLoading()
            presenter.createTicket(TicketCreateBody(
                selectedRoute.id!!,
                selectedSla.id,
                selectedCat.type,
                selectedCat.id,
                selectedType.name,
                etKeterangan.text.toString(),
                imageFill,
                orderType,
                orderID
            ), getLat(), getLong())
        }
    }

    override fun onConfirmDialogDismissed() {}

    override fun errorScreen(message: String?, code: Int?) {
        if (code == 400) {
            ErrorDialogFragment.newInstance(
                Params.TAG_TAKE, message?:"-", getString(R.string.ok_mengerti)
            ).show(supportFragmentManager, ErrorDialogFragment::class.java.canonicalName)
        } else {
            super.errorScreen(message, code)
        }
    }

    override fun onErrorDialogBtnPressed(tag: String?) {
        if (tag == Params.TAG_TAKE)
            finish()
    }

    override fun onErrorDialogDismissed(tag: String?) {
    }
}