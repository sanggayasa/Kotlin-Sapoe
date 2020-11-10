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
import com.akarinti.sapoe.extension.checkPermissions
import com.akarinti.sapoe.extension.formatDigit
import com.akarinti.sapoe.model.answer.ImageResult
import com.akarinti.sapoe.objects.CodeIntent
import com.akarinti.sapoe.objects.ImagePicker
import com.akarinti.sapoe.objects.ImageTools
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.utils.GlideApp
import com.akarinti.sapoe.utils.LocationUtils
import com.akarinti.sapoe.view.component.dialog.ConfirmDialogFragment
import com.akarinti.sapoe.view.main.ticket.add.spareparts.SparepartsActivity
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

class FotoBeforeActivity : BaseMvpActivity<FotoBeforePresenter>(), FotoBeforeContract.View, ConfirmDialogFragment.Listener {

    companion object {
        fun newInstance(context: Context, ticketId: String, index: Int, cityID: String) : Intent =
            context.intentFor<FotoBeforeActivity>(
                Params.BUNDLE_TICKET_ID to ticketId, Params.BUNDLE_ITEM_INDEX to index, Params.BUNDLE_CITY_ID to cityID)
    }

    @Inject
    override lateinit var presenter: FotoBeforePresenter

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun injectView() {
        AndroidInjection.inject(this)
    }
    private var itemID: String? = null
    private var imageResult: String? = null
    private var cityID: String = ""
    var index: Int = 1

    override fun setup() {
        initBundle()
        initView()
        initAction()
        checkButton()
    }
    private fun initBundle() {
        intent?.extras?.let {
            itemID = it.getString(Params.BUNDLE_TICKET_ID)
            index = it.getInt(Params.BUNDLE_ITEM_INDEX)
            cityID = it.getString(Params.BUNDLE_CITY_ID, "")
        }
    }
    override fun getLayout(): Int = R.layout.activity_picture_question

    private fun initView() {
        btnAction.text = getString(R.string.simpan)
        tvTitle.setText(R.string.temuan_before)
        tvFotoTitle.setText(R.string.foto_1)
        tvLatValue.text = DeviceInfo.getLatLong(this)[LocationUtils.LATITUDE_INDEX].formatDigit(5)
        tvLongValue.text = DeviceInfo.getLatLong(this)[LocationUtils.LONGTITUDE_INDEX].formatDigit(5)
        presenter.imageRepository.imageResult?.find { it.orderID == itemID }?.let {
            if (null != it.imageBefore){
                GlideApp.with(this).load(it.imageBefore).into(ivResult)
                btnFotoUlang.visibility = View.VISIBLE
                btnAmbil.visibility = View.GONE
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
                orignData.imageBefore = imageResult
                temp.add(orignData)
            } else {
                temp.add(ImageResult(orderID = itemID, imageBefore = imageResult))
            }
            presenter.imageRepository.imageResult = temp
            ConfirmDialogFragment.newInstance(Params.TAG_CREATE, getString(R.string.ada_sparepart), getString(R.string.ada), getString(R.string.tidak_ada))
                .show(supportFragmentManager, ConfirmDialogFragment::class.java.canonicalName)
//            setResult(Activity.RESULT_OK)
//            finish()
        }
        ivBack.setOnClickListener { onBackPressed() }
    }

    override fun onCancelDialogBtnPressed(tag: String?) {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onConfirmDialogBtnPressed(tag: String?) {
        if (tag == Params.TAG_CREATE) {
            startActivity(SparepartsActivity.newInstance(this, itemID!!, index, cityID))
            finish()
        }
    }

    override fun onConfirmDialogDismissed() {
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