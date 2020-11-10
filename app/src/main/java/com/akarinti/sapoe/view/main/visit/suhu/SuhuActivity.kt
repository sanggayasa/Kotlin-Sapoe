package com.akarinti.sapoe.view.main.visit.suhu

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.extension.checkPermissions
import com.akarinti.sapoe.extension.formatDigit
import com.akarinti.sapoe.model.questionnaire.ParentCleanOffline
import com.akarinti.sapoe.objects.CodeIntent
import com.akarinti.sapoe.objects.ImagePicker
import com.akarinti.sapoe.objects.ImageTools
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.utils.GlideApp
import com.akarinti.sapoe.utils.LocationUtils
import com.akarinti.sapoe.view.main.camera.CameraFragment
import com.bumptech.glide.signature.ObjectKey
import com.thanosfisherman.mayi.MayI
import com.thanosfisherman.mayi.PermissionBean
import com.thanosfisherman.mayi.PermissionToken
import dagger.android.AndroidInjection
import io.vrinda.kotlinpermissions.DeviceInfo
import kotlinx.android.synthetic.main.activity_suhu_ruangan.*
import kotlinx.android.synthetic.main.button_navigation.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject


class SuhuActivity : BaseMvpActivity<SuhuPresenter>(), SuhuContract.View, CameraFragment.Listener {
    companion object {
        fun newInstance(activity: Activity, scheduleID: String?, isScheduled: Boolean) {
            activity.startActivityForResult(activity.intentFor<SuhuActivity>(
                Params.BUNDLE_SCHEDULE_ID to scheduleID,
                Params.BUNDLE_SCHEDULE_TYPE to isScheduled
            ), CodeIntent.OPEN_QUESTION)
        }
    }
    @Inject
    override lateinit var presenter: SuhuPresenter
    private lateinit var dataCl: ParentCleanOffline
    private var scheduleID: String? = null
    private var imageResult: String? = null
    private var isScheduled: Boolean = false
    override fun getCamera(imagefile: String) {
        imageResult=imagefile
        checkButton()
    }

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun injectView() {
        AndroidInjection.inject(this)
    }

    override fun setup() {
        initAdapter()
        initSpinner()
        initBundle()
        initView()
        initAction()
        checkButton()
    }

    private fun initBundle() {
        intent?.extras?.let {
            scheduleID = it.getString(Params.BUNDLE_SCHEDULE_ID)
            isScheduled = it.getBoolean(Params.BUNDLE_SCHEDULE_TYPE)
        }

        if (null != scheduleID) {
            if (isScheduled)
                dataCl = presenter.inprogressRepository.scheduledCl?.find { it.id == scheduleID }!!
            else
                dataCl = presenter.inprogressRepository.unscheduledCl?.find { it.id == scheduleID }!!

            if (null != dataCl) {
                if (dataCl.suhu_picture != null) {
                    GlideApp.with(this).load(dataCl.suhu_picture)
                        .signature(ObjectKey(System.currentTimeMillis().toString()))
                        .into(ivResult)
                }
                btnAmbil.visibility = if (dataCl.suhu_picture != null) View.GONE else View.VISIBLE
                btnFotoUlang.visibility = if (dataCl.suhu_picture != null) View.VISIBLE else View.GONE
                if (!dataCl.suhu_answer.isNullOrEmpty()) {
                    val kategori = arrayOf(dataCl.suhu_answer)
                    val adapterKategori = ArrayAdapter<String>(
                        this,
                        R.layout.item_spinner, kategori
                    )
                    adapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spSuhu.adapter = adapterKategori
                    spSuhu.isEnabled = false
                }
            }
        }
    }


    override fun getLayout(): Int = R.layout.activity_suhu_ruangan

    private fun initView() {
        if(dataCl.suhu_answer.isNullOrEmpty())
            spSuhu.isEnabled= true
        btnSimpan.isEnabled=false
        tvLatValue.text = DeviceInfo.getLatLong(this)[LocationUtils.LATITUDE_INDEX].formatDigit(5)
        tvLongValue.text = DeviceInfo.getLatLong(this)[LocationUtils.LONGTITUDE_INDEX].formatDigit(5)
    }
    private fun initAction() {
        ivBack.setOnClickListener { onBackPressed() }
        btnAmbil.setOnClickListener { showPicker() }
        btnFotoUlang.setOnClickListener { showPicker() }
        var orignData : ArrayList<ParentCleanOffline> = ArrayList()
        btnSimpan.setOnClickListener {
            if (null != scheduleID) {
                if (isScheduled)
                    orignData = ArrayList(presenter.inprogressRepository.scheduledCl ?: ArrayList())
                else
                    orignData = ArrayList(presenter.inprogressRepository.unscheduledCl ?: ArrayList())

                orignData.remove(dataCl)
                dataCl.suhu_answer = spSuhu.selectedItem.toString()
                dataCl.suhu_picture = imageResult

                orignData.add(dataCl)
                if (isScheduled)
                    presenter.inprogressRepository.scheduledCl = orignData
                else
                    presenter.inprogressRepository.unscheduledCl = orignData

            }
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun gotoNextPage() {
        onBackPressed()
    }
    private fun initAdapter(){
        val kategori = arrayOf("-",">25°C", "<=25°C")

        val adapterKategori= ArrayAdapter<String>(this,
            R.layout.item_spinner,kategori)
        adapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spSuhu.adapter = adapterKategori

    }

    private fun initSpinner(){
        spSuhu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                checkButton()
            }
        }
    }

    private fun checkButton() {
        btnSimpan.isEnabled = (
                spSuhu.selectedItem!=getString(R.string.strip)&&
                        !imageResult.isNullOrEmpty()
                )
    }

    private fun showPicker() {
        if (checkPermissions(Manifest.permission.CAMERA)) {
            startActivityForResult(
                ImagePicker.getPickImageIntent(this, false, true),
                CodeIntent.UPLOAD_IMAGE
            )
        } else {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        MayI.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .onResult(this::permissionResultSingle)
            .onRationale(this::permissionRationaleSingle)
            .check()
    }

    private fun permissionResultSingle(permission: PermissionBean) {
        if (permission.isPermanentlyDenied) {
            AlertDialog.Builder(this).setMessage("This feature need camera permission to run. Do you want to allow it now?"+
                    ImagePicker.getImagePath(this, ImagePicker.TEMP_IMAGE_NAME))
                .setCancelable(false)
                .setPositiveButton(
                    "OK"
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, _ ->
                    dialog.dismiss()
                    showPicker()
                }
                .create().show()
        } else if (permission.isGranted) {
            showPicker()
        }
    }

    private fun permissionRationaleSingle(bean: PermissionBean, token: PermissionToken) {
        token.continuePermissionRequest()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CodeIntent.UPLOAD_IMAGE -> {
                val success = ImagePicker.getImageFromResult(
                    this,
                    resultCode, data, ImagePicker.TEMP_IMAGE_NAME
                )
                if (success) {
                    showLoading()
                    val imageFile = BitmapFactory.decodeFile(
                        ImagePicker.getImagePath(
                            this,
                            ImagePicker.TEMP_IMAGE_NAME
                        )
                    )
                    if (imageFile != null) {
                        btnAmbil.visibility = View.GONE
                        ivIcon.visibility = View.GONE
                        btnFotoUlang.visibility = View.VISIBLE
                        ivResult.setImageBitmap(imageFile)
                        imageResult = "data:image/png;base64,"+ ImageTools.bitmapToBase64(imageFile)
                    }
                    dismissLoading()
                }
                checkButton()
            }
            else -> { }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
