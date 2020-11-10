package com.akarinti.sapoe.view.component.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseDialogFragment
import com.akarinti.sapoe.objects.Params
import kotlinx.android.synthetic.main.dialog_confim.*

class UpdateDialogFragment: BaseDialogFragment() {

    private var isForce: Boolean = false
    private var url: String? = null

    companion object {
        fun newInstance(forceUpdate: Boolean, url: String?): UpdateDialogFragment {
            val fragment = UpdateDialogFragment()
            val args = Bundle()
            args.putBoolean(Params.BUNDLE_FORCE_UPDATE, forceUpdate)
            args.putString(Params.BUNDLE_APK_URL, url)
            fragment.arguments = args
            return fragment
        }

        var listener: UpdateDialogFragment.Listener? = null

    }

    override fun setupDialogStyle(dialog: Dialog) {
        if (null != dialog.window && null != dialog.window!!.attributes) {
            dialog.setCancelable(false)
        }
        isCancelable = false
    }

    override fun loadArguments() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DefaultDialog)
        arguments?.let {
            isForce = it.getBoolean(Params.BUNDLE_FORCE_UPDATE)
            url = it.getString(Params.BUNDLE_APK_URL)
        }
    }

    override fun setup() {
        initView()
        initAction()
    }

    private fun initView() {
        tvText.setText(if (isForce) R.string.force_update else R.string.optional_update)
        btnAction.setText(R.string.update)
        btnCancel.setText(R.string.batal)
        if (isForce)
            btnCancel.visibility = View.GONE
    }

    private fun initAction() {
        btnAction.setOnClickListener {
            if(parentFragment is Listener) {
                (parentFragment as? Listener)?.onUpdate(url)
            } else if (activity is Listener) {
                (activity as? Listener)?.onUpdate(url)
            } else if (null != listener) {
                listener?.onUpdate(url)
            }
        }
        btnCancel.setOnClickListener {
            if(parentFragment is Listener) {
                (parentFragment as? Listener)?.onCancel()
            } else if (activity is Listener) {
                (activity as? Listener)?.onCancel()
            } else if (null != listener) {
                listener?.onCancel()
            }
            dismiss()
        }
    }

    override fun getLayout(): Int = R.layout.dialog_confim

    interface Listener {
        fun onUpdate(url: String?)
        fun onCancel()
    }
}