package com.akarinti.sapoe.view.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner

class CustomSpinner(context: Context, attrs: AttributeSet) : AppCompatSpinner(context, attrs) {

    internal var listener: AdapterView.OnItemSelectedListener? = null
    private var lastParent: AdapterView<*>? = null
    private var lastView: View? = null
    private var lastId: Long = 0

    init {
        initListener()
    }

    override fun setSelection(position: Int) {
        if (position == selectedItemPosition && listener != null) {
            listener?.onItemSelected(lastParent, lastView, position, lastId)
        } else {
            super.setSelection(position)
        }

    }

    private fun initListener() {
        super.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?,
                position: Int, id: Long
            ) {
                if (lastParent != null) {
                    listener?.onItemSelected(parent, view, position, id)
                }
                lastParent = parent
                lastView = view
                lastId = id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                listener?.onNothingSelected(parent)
            }
        })
    }

    override fun setOnItemSelectedListener(listener: AdapterView.OnItemSelectedListener?) {
        this.listener = listener
    }
}
