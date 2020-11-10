package com.akarinti.sapoe.view.component.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.fragment.app.DialogFragment
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseDialogFragment
import com.akarinti.sapoe.objects.Params
import kotlinx.android.synthetic.main.dialog_error.*

class ErrorDialogFragment: BaseDialogFragment() {
    private var text: String? = null
    private var actionText: String? = null
    private var dialogTag: String? = null
    private var icon: Int = R.drawable.popup_warn

    companion object {
        fun newInstance(tag: String, text: String, action: String, @DrawableRes icon: Int = R.drawable.popup_warn): ErrorDialogFragment {
            val fragment = ErrorDialogFragment()
            val args = Bundle()
            args.putString(Params.BUNDLE_TEXT, text)
            args.putString(Params.BUNDLE_ACTION_TEXT, action)
            args.putString(Params.BUNDLE_TAG, tag)
            args.putInt(Params.BUNDLE_ICON, icon)
            fragment.arguments = args
            return fragment
        }

        var listener: ErrorDialogFragment.Listener? = null

    }

    override fun setupDialogStyle(dialog: Dialog) {

    }

    override fun loadArguments() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DefaultDialog)
        arguments?.let {
            text = it.getString(Params.BUNDLE_TEXT)
            actionText = it.getString(Params.BUNDLE_ACTION_TEXT)
            dialogTag = it.getString(Params.BUNDLE_TAG)
            icon = it.getInt(Params.BUNDLE_ICON)
        }
    }

    override fun setup() {
        initView()
        initAction()
    }

    private fun initView() {
        tvText.text = text
        btnAction.text = actionText
        ivIcon.setImageResource(icon)
    }

    private fun initAction() {
        btnAction.setOnClickListener {
            if(parentFragment is Listener) {
                (parentFragment as? Listener)?.onErrorDialogBtnPressed(dialogTag)
            } else if (activity is Listener) {
                (activity as? Listener)?.onErrorDialogBtnPressed(dialogTag)
            } else if (null != listener) {
                listener?.onErrorDialogBtnPressed(tag)
            }
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        if(parentFragment is Listener) {
            (parentFragment as? Listener)?.onErrorDialogDismissed(tag)
        } else if (activity is Listener) {
            (activity as? Listener)?.onErrorDialogDismissed(tag)
        } else if (null != listener) {
            listener?.onErrorDialogDismissed(tag)
        }
        super.onDismiss(dialog)
    }

    override fun getLayout(): Int = R.layout.dialog_error

    interface Listener {
        fun onErrorDialogBtnPressed(tag: String?)
        fun onErrorDialogDismissed(tag: String?)
    }
}