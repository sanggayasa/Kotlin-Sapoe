package com.akarinti.sapoe.view.component.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.fragment.app.DialogFragment
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseDialogFragment
import com.akarinti.sapoe.objects.Params
import kotlinx.android.synthetic.main.dialog_confim.*

class ConfirmDialogFragment: BaseDialogFragment() {
    private var text: String? = null
    private var actionText: String? = null
    private var cancelText: String? = null
    private var dialogTag: String? = null
    private var icon: Int = R.drawable.popup_warn

    companion object {
        fun newInstance(tag: String, text: String, action: String, cancel: String, @DrawableRes icon: Int = R.drawable.popup_warn): ConfirmDialogFragment {
            val fragment = ConfirmDialogFragment()
            val args = Bundle()
            args.putString(Params.BUNDLE_TEXT, text)
            args.putString(Params.BUNDLE_ACTION_TEXT, action)
            args.putString(Params.BUNDLE_CANCEL_TEXT, cancel)
            args.putString(Params.BUNDLE_TAG, tag)
            args.putInt(Params.BUNDLE_ICON, icon)
            fragment.arguments = args
            return fragment
        }

        var listener: ConfirmDialogFragment.Listener? = null

    }

    override fun setupDialogStyle(dialog: Dialog) {

    }

    override fun loadArguments() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DefaultDialog)
        arguments?.let {
            text = it.getString(Params.BUNDLE_TEXT)
            actionText = it.getString(Params.BUNDLE_ACTION_TEXT)
            cancelText = it.getString(Params.BUNDLE_CANCEL_TEXT)
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
        btnCancel.text = cancelText
        ivIcon.setImageResource(icon)
    }

    private fun initAction() {
        btnAction.setOnClickListener {
            if(parentFragment is Listener) {
                (parentFragment as? Listener)?.onConfirmDialogBtnPressed(dialogTag)
            } else if (activity is Listener) {
                (activity as? Listener)?.onConfirmDialogBtnPressed(dialogTag)
            } else if (null != listener) {
                listener?.onConfirmDialogBtnPressed(dialogTag)
            }
            dismiss()
        }
        btnCancel.setOnClickListener {
            if(parentFragment is Listener) {
                (parentFragment as? Listener)?.onCancelDialogBtnPressed(dialogTag)
            } else if (activity is Listener) {
                (activity as? Listener)?.onCancelDialogBtnPressed(dialogTag)
            } else if (null != listener) {
                listener?.onCancelDialogBtnPressed(dialogTag)
            }
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        if(parentFragment is Listener) {
            (parentFragment as? Listener)?.onConfirmDialogDismissed()
        } else if (activity is Listener) {
            (activity as? Listener)?.onConfirmDialogDismissed()
        } else if (null != listener) {
            listener?.onConfirmDialogDismissed()
        }
        super.onDismiss(dialog)
    }

    override fun getLayout(): Int = R.layout.dialog_confim

    interface Listener {
        fun onCancelDialogBtnPressed(tag: String?)
        fun onConfirmDialogBtnPressed(tag: String?)
        fun onConfirmDialogDismissed()
    }
}