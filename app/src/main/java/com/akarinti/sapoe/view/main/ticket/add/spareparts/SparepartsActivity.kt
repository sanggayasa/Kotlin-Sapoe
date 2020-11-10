package com.akarinti.sapoe.view.main.ticket.add.spareparts

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.data.body.SparepartsBody
import com.akarinti.sapoe.event.SlaFetchUpdateEvent
import com.akarinti.sapoe.event.SparepartRefreshEvent
import com.akarinti.sapoe.extension.isValidQuantity
import com.akarinti.sapoe.model.ticket.Category
import com.akarinti.sapoe.model.ticket.Spareparts
import com.akarinti.sapoe.objects.CodeIntent
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.objects.RxBus
import com.akarinti.sapoe.view.main.ticket.add.foto.FotoAfterActivity
import com.akarinti.sapoe.view.main.ticket.add.fragment.SearchSparepartsListFragment
import com.akarinti.sapoe.view.main.ticket.add.spareparts.foto.CameraAfterAdapter
import com.akarinti.sapoe.view.main.ticket.add.spareparts.foto.CameraAfterFragment
import com.akarinti.sapoe.view.main.ticket.add.spareparts.foto.CameraBeforeAdapter
import com.akarinti.sapoe.view.main.ticket.add.spareparts.foto.CameraBeforeFragment
import com.jakewharton.rxbinding3.widget.textChanges
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_spareparts.*
import kotlinx.android.synthetic.main.button_navigation.*
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject

class SparepartsActivity : BaseMvpActivity<SparepartsPresenter>(),
    SparepartsContract.View, CameraAfterFragment.Listener, CameraBeforeFragment.Listener, SearchSparepartsListFragment.Listener{
    companion object {
        fun newInstance(context: Context, ticketId: String, index: Int, cityID: String) : Intent =
            context.intentFor<SparepartsActivity>(Params.BUNDLE_TICKET_ID to ticketId, Params.BUNDLE_ITEM_INDEX to index, Params.BUNDLE_CITY_ID to cityID)
    }
    @Inject
    override lateinit var presenter: SparepartsPresenter

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
        fetchList()
    }

    var sparepartsList = ArrayList<Spareparts>()
    var selectedSpareList: Spareparts? = null
    var category: String = ""
    var imageBefore: String = ""
    var imageAfter: String = ""
    var sparepartId: String = ""
    var ticketId: String = ""
    var index: Int = 1
    private var cityID: String = ""
    lateinit var selectedCat: Category
    private var isQuantityOK = false

    override fun getLayout(): Int = R.layout.activity_spareparts

    private fun initBundle(){
        intent?.extras?.let {
            ticketId = it.getString(Params.BUNDLE_TICKET_ID)?: ""
            index = it.getInt(Params.BUNDLE_ITEM_INDEX) + 1
            cityID = it.getString(Params.BUNDLE_CITY_ID, "-")
        }
    }

    private fun initView() {
        vpCameraBefore.adapter= CameraBeforeAdapter(supportFragmentManager)
        vpCameraAfter.adapter= CameraAfterAdapter(supportFragmentManager)
        btnSimpan.isEnabled=false
        tvTitle.setText(R.string.sparepart)
        tvStatus.text = String.format(getString(R.string.spareparts1), index)
        spJenis.isEnabled = false
    }

    private fun initAction() {
        ivBack.setOnClickListener { onBackPressed() }
        btnSimpan.setOnClickListener {
            showLoading()
            presenter.createSparepart(ticketId, SparepartsBody(sparepartList = listOf(SparepartsBody.Sparepart(imageAfter, imageBefore, sparepartId, etQuantity.text.toString().toFloatOrNull()))))
        }
        tvHapus.setOnClickListener {
            ivResult.setImageBitmap(null)
            ivFoto.visibility=View.VISIBLE
            imageAfter=""
            imageBefore=""
            checkButton()
        }
        spCategory.setOnClickListener {spKategoriParts.performClick() }
        spJenis.setOnClickListener {
                SearchSparepartsListFragment.newInstance()
                    .show(
                        supportFragmentManager,
                        SearchSparepartsListFragment::class.java.canonicalName
                    )
        }
        btAddParts.setOnClickListener {
            showLoading()
            presenter.createSparepartNext(ticketId, SparepartsBody(sparepartList = listOf(SparepartsBody.Sparepart(imageAfter, imageBefore, sparepartId, etQuantity.text.toString().toFloatOrNull()))))
        }
    }

    override fun onCreateSparepart() {
        RxBus.publish(SparepartRefreshEvent())
        startActivityForResult(FotoAfterActivity.newInstance(this, ticketId), CodeIntent.OPEN_QUESTION)
        finish()
    }

    override fun onCreateNext() {
        RxBus.publish(SparepartRefreshEvent())
        startActivity(newInstance(this, ticketId, index, cityID))
        finish()
    }

    override fun onSparepartsList(list: List<Spareparts>?) {
        dismissLoading()
        list?.let {
            sparepartsList = ArrayList(it)
        }
        RxBus.publish(SlaFetchUpdateEvent())
        spJenis.isEnabled = true
    }

    override fun onSparepartsCat(list: List<Category>?) {
        list?.let {
            val kategoriParts = ArrayAdapter(this, R.layout.item_spinner_divider_small, R.id.tvSpinnerItem, it.map { item -> item.name })
            spKategoriParts.apply {
                adapter = kategoriParts
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        spCategory.text = kategoriParts.getItem(p2)
                        selectedCat = it[p2]
                        presenter.getSparepartsList(it[p2].id!!, "", cityID)
                        checkButton()
                    }
                }
            }
        }
        dismissLoading()
    }

    private fun fetchList(){
       presenter.getSparepartsCat()
    }

    private fun checkButton() {
        val isEnableNext = (!imageBefore.isNullOrEmpty()&&
                spCategory.text!=getString(R.string.pilih_cat_sparepart)&&
                spJenis.text!=getString(R.string.pilih_jenis_tiket) && isQuantityOK)
        btnSimpan.isEnabled = isEnableNext
        btAddParts.isEnabled = isEnableNext
        btAddParts.visibility = if (isEnableNext) View.VISIBLE else View.GONE
    }

    override fun initSubscription() {
        addUiSubscription(etQuantity.textChanges().observeOn(AndroidSchedulers.mainThread()).subscribe {
            isQuantityOK = it.isValidQuantity()
            if (it.isNotEmpty()) {
                tvQuantityWarning.visibility = if (isQuantityOK) View.INVISIBLE else View.VISIBLE
            }
            checkButton()
        })
        super.initSubscription()
    }

    override fun getCameraAfter(imagefile: String) {
        imageAfter=imagefile
        checkButton()
    }

    override fun getCameraBefore(imagefile: String) {
        imageBefore=imagefile
        checkButton()
    }

    override fun getSpareparts(): ArrayList<Spareparts> =sparepartsList

    override fun selectedSpareparts(selected: Spareparts) {
        selectedSpareList= selected
        sparepartId= selected.id.toString()
        spJenis.text=selected.name
        checkButton()
    }

    override fun searchSpareparts(query: String) {
        showLoading()
        presenter.getSparepartsList(selectedCat.id!!, query, cityID)
    }

}