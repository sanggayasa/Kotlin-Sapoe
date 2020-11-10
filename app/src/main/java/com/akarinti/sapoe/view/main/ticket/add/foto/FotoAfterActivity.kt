package com.akarinti.sapoe.view.main.ticket.add.foto

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.event.SparepartRefreshEvent
import com.akarinti.sapoe.extension.checkPermissions
import com.akarinti.sapoe.extension.formatDigit
import com.akarinti.sapoe.model.answer.ImageResult
import com.akarinti.sapoe.objects.*
import com.akarinti.sapoe.utils.GlideApp
import com.akarinti.sapoe.utils.LocationUtils
import com.thanosfisherman.mayi.MayI
import com.thanosfisherman.mayi.PermissionBean
import com.thanosfisherman.mayi.PermissionToken
import dagger.android.AndroidInjection
import io.vrinda.kotlinpermissions.DeviceInfo
import kotlinx.android.synthetic.main.activity_picture_question.*
import kotlinx.android.synthetic.main.sticky_button.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject

class FotoAfterActivity : BaseMvpActivity<FotoAfterPresenter>(), FotoAfterContract.View {

    companion object {
        fun newInstance(context: Context, ticketId: String) : Intent =
            context.intentFor<FotoAfterActivity>(
                Params.BUNDLE_TICKET_ID to ticketId)
    }

    @Inject
    override lateinit var presenter: FotoAfterPresenter

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun injectView() {
        AndroidInjection.inject(this)
    }
    private var itemID: String? = null
    private var imageResult: String? = null

    override fun setup() {
        initBundle()
        initView()
        initAction()
        checkButton()
    }
    private fun initBundle() {
        intent?.extras?.let {
            itemID = it.getString(Params.BUNDLE_TICKET_ID)
        }
    }
    override fun getLayout(): Int = R.layout.activity_picture_question

    private fun initView() {
        btnAction.text = getString(R.string.simpan)
        tvTitle.setText(R.string.temuan_after)
        tvFotoTitle.setText(R.string.foto_1)
        tvLatValue.text = DeviceInfo.getLatLong(this)[LocationUtils.LATITUDE_INDEX].formatDigit(5)
        tvLongValue.text = DeviceInfo.getLatLong(this)[LocationUtils.LONGTITUDE_INDEX].formatDigit(5)
        presenter.imageRepository.imageResult?.find { it.orderID == itemID }?.let {
            if (null != it.imageAfter){
                GlideApp.with(this).load(it.imageAfter).into(ivResult)
                btnFotoUlang.visibility = View.VISIBLE
                btnAmbil.visibility = View.GONE
                imageResult = it.imageAfter
            }
        }
    }

    private fun initAction() {
        btnAmbil.setOnClickListener { showPicker() }
        btnFotoUlang.setOnClickListener { showPicker() }
        btnAction.setOnClickListener {
            val temp: MutableList<ImageResult> = (presenter.imageRepository.imageResult ?: ArrayList()).toMutableList()
            val orignData: ImageResult? = temp.find { it.orderID == itemID }
            if (null != orignData) {
                temp.remove(orignData)
                orignData.imageAfter = imageResult
                temp.add(orignData)
            } else {
                temp.add(ImageResult(orderID = itemID, imageAfter = imageResult))
            }
            presenter.imageRepository.imageResult = temp
            RxBus.publish(SparepartRefreshEvent())
            setResult(Activity.RESULT_OK)
            finish()
        }
        ivBack.setOnClickListener { onBackPressed() }
    }

    override fun gotoNextPage() {

    }

    private fun checkButton() {
        btnAction.isEnabled = !imageResult.isNullOrEmpty()
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