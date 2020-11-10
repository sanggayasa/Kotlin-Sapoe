package com.akarinti.sapoe.view.main.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.view.View
import androidx.core.app.ActivityCompat
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpFragment
import com.akarinti.sapoe.objects.CodeIntent
import com.akarinti.sapoe.objects.ImagePicker
import com.akarinti.sapoe.objects.ImageTools
import com.thanosfisherman.mayi.MayI
import com.thanosfisherman.mayi.PermissionBean
import com.thanosfisherman.mayi.PermissionToken
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_camera.*
import javax.inject.Inject


class CameraFragment :BaseMvpFragment<CameraPresenter>(), CameraContract.View {
    override fun gotoNextPage() {

    }
    @Inject
    override lateinit var presenter: CameraPresenter

    override fun injectView() {
        AndroidSupportInjection.inject(this)
    }

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun setup() {
        initView()
        initAction()
    }
    override fun getLayout(): Int = R.layout.fragment_camera

    fun initAction(){
        btnAmbil.setOnClickListener { showPicker() }
        btnAmbilUlang.setOnClickListener { showPicker() }

    }
    fun initView() {
    }

    private fun checkPermissions(): Boolean {
        context?.apply {
            return (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED)
        }
        return false
    }

    private fun requestPermission() {
        activity?.let {
            MayI.withActivity(it)
                .withPermission(Manifest.permission.CAMERA)
                .onResult(this::permissionResultSingle)
                .onRationale(this::permissionRationaleSingle)
                .check()
        }
    }

    private fun showPicker(camera: Boolean = true) {
        checkPermissions().let {
            if (!camera || it) {
                context?.let {
                    startActivityForResult(
                        ImagePicker.getPickImageIntent(it, true, camera),
                        CodeIntent.UPLOAD_IMAGE
                    )
                }
            } else {
                requestPermission()
            }
        }
    }
    private fun permissionResultSingle(permission: PermissionBean) {
        if (permission.isPermanentlyDenied) {
            android.app.AlertDialog.Builder(context).setMessage("This feature need camera permission to run. Do you want to allow it now?"+
                    ImagePicker.getImagePath(context!!,
                        ImagePicker.TEMP_IMAGE_NAME))
                .setCancelable(false)
                .setPositiveButton(
                    "OK"
                ) { dialog, which ->
                    dialog.dismiss()
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, which ->
                    dialog.dismiss()
                    showPicker(false)
                }
                .create().show()
        } else if (permission.isGranted) {
            showPicker()
        } else {
            showPicker(false)
        }
    }
    private fun permissionRationaleSingle(bean: PermissionBean, token: PermissionToken) {
        if (bean.simpleName.toLowerCase().contains("contacts")) {
            token.skipPermissionRequest()
        } else {
            token.continuePermissionRequest()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var listener: Listener? = null
        if (parentFragment is Listener) {
            listener = parentFragment as? Listener
        } else if (activity is Listener) {
            listener = activity as? Listener
        }

        context?.let {
            when (requestCode) {
                CodeIntent.UPLOAD_IMAGE -> {
                    val success = ImagePicker.getImageFromResult(
                        activity!!,
                        resultCode, data, ImagePicker.TEMP_IMAGE_NAME)
                    if (success) {
                        showLoading()
                        val imageFile = BitmapFactory.decodeFile(
                            ImagePicker.getImagePath(context!!,
                                ImagePicker.TEMP_IMAGE_NAME))
                        if(imageFile!=null) {
                            dismissLoading()
                            btnAmbil.visibility= View.GONE
                            ivFoto.visibility= View.GONE
                            btnAmbilUlang.visibility= View.VISIBLE
                            ivResult.setImageBitmap(imageFile)
                            listener?.let {
                                it.getCamera("data:image/png;base64,"+
                                    ImageTools.bitmapToBase64(
                                        imageFile
                                    )
                                )
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }
    interface Listener{
        fun getCamera(imagefile:String)
    }
}
