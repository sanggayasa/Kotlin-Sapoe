package com.akarinti.sapoe.view.component.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.fragment.app.DialogFragment
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseDialogFragment
import com.akarinti.sapoe.objects.Params
import kotlinx.android.synthetic.main.dialog_finish.*


class FinisDialogFragment: BaseDialogFragment() {
    private var text: String? = null
    private var actionText: String? = null
    private var dialogTag: String? = null
    private var icon: Int = R.drawable.popup_warn

    companion object {
        fun newInstance(text: String, action: String, @DrawableRes icon: Int = R.drawable.popup_warn): FinisDialogFragment {
            val fragment = FinisDialogFragment()
            val args = Bundle()
            args.putString(Params.BUNDLE_TEXT, text)
            args.putString(Params.BUNDLE_ACTION_TEXT, action)
            args.putInt(Params.BUNDLE_ICON, icon)
            fragment.arguments = args
            return fragment
        }

        var listener: FinisDialogFragment.Listener? = null

    }

    override fun setupDialogStyle(dialog: Dialog) {

    }

    override fun loadArguments() {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DefaultDialog)
        arguments?.let {
            text = it.getString(Params.BUNDLE_TEXT)
            actionText = it.getString(Params.BUNDLE_ACTION_TEXT)
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
                (parentFragment as? Listener)?.onFinishDialogBtnPressed()
            } else if (activity is Listener) {
                (activity as? Listener)?.onFinishDialogBtnPressed()
            } else if (null != listener) {
                listener?.onFinishDialogBtnPressed()
            }
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        if(parentFragment is Listener) {
            (parentFragment as? Listener)?.onFinishDialogBtnPressed()
        } else if (activity is Listener) {
            (activity as? Listener)?.onFinishDialogBtnPressed()
        } else if (null != listener) {
            listener?.onFinishDialogBtnPressed()
        }
        super.onDismiss(dialog)
    }

    override fun getLayout(): Int = R.layout.dialog_finish

    interface Listener {
        fun onFinishDialogBtnPressed()
    }
}