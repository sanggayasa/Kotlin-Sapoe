package com.akarinti.sapoe.view.component.input

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

open class CustomEditText: EditText {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    var onKeyUpListener: ((Int) -> Unit)? = null

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        val superResult = super.onKeyUp(keyCode, event)
        onKeyUpListener?.invoke(keyCode)
        return superResult
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        val enabled = newConfig?.keyboard != Configuration.KEYBOARD_QWERTY
                && newConfig?.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES
        if (enabled && isFocused) {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(this, 0)
        }
    }
}