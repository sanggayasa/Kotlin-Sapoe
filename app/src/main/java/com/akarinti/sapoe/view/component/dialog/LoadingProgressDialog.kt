package com.akarinti.sapoe.view.component.dialog

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseDialogFragment

class LoadingProgressDialog: BaseDialogFragment() {

    companion object {
        fun newInstance(): LoadingProgressDialog {
            val fragment = LoadingProgressDialog()
            return fragment
        }
    }

    override fun setupDialogStyle(dialog: Dialog) {
    }

    override fun loadArguments() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DefaultDialog)
    }

    override fun setup() {
    }

    override fun getLayout(): Int = R.layout.loading_default
}