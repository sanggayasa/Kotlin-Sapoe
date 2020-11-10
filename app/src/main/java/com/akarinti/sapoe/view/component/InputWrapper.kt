package com.akarinti.sapoe.view.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputLayout

class InputWrapper : TextInputLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
        defStyleAttr) {}

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child is EditText && childCount > 1) {
            val frame = getChildAt(0)
            removeViewAt(0)
            addView(frame)
            if (!isHintEnabled) {
                val lp = frame.layoutParams as LinearLayout.LayoutParams
                lp.topMargin = 0
                frame.requestLayout()
            }
        }
        super.addView(child, index, params)

    }
}