package com.akarinti.sapoe.view.component.dialog

import android.app.Dialog
import android.os.Bundle
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseDialogFragment
import com.akarinti.sapoe.objects.Params
import kotlinx.android.synthetic.main.dialog_warning_popup.*

class WarningPopup: BaseDialogFragment() {

    private var desc: String? = null

    companion object {
        fun newInstance(desc: String?): WarningPopup {
            val fragment = WarningPopup()
            val args = Bundle()
            args.putString(Params.BUNDLE_TEXT, desc)
            fragment.arguments = args
            return fragment
        }

        var listener: Listener? = null

    }

    override fun setupDialogStyle(dialog: Dialog) {
        if (null != dialog.window && null != dialog.window!!.attributes) {
            dialog.setCancelable(false)
        }
        isCancelable = false
    }

    override fun loadArguments() {
        setStyle(STYLE_NORMAL, R.style.DefaultDialog)
        arguments?.let {
            desc = it.getString(Params.BUNDLE_TEXT)
        }
    }

    override fun setup() {
        initView()
        initAction()
    }

    private fun initView() {
    }

    private fun initAction() {
        btnAction.setOnClickListener {
            if(parentFragment is Listener) {
                (parentFragment as? Listener)?.onActionButton()
            } else if (activity is Listener) {
                (activity as? Listener)?.onActionButton()
            } else if (null != listener) {
                listener?.onActionButton()
            }
        }
    }

    override fun getLayout(): Int = R.layout.dialog_warning_popup

    interface Listener {
        fun onActionButton()
    }
}