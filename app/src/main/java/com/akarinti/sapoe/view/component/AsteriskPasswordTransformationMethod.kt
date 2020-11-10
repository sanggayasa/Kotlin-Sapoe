package com.akarinti.sapoe.view.component

import android.text.method.PasswordTransformationMethod
import android.view.View

class AsteriskPasswordTransformationMethod: PasswordTransformationMethod() {
    companion object {
        fun getInstance(): AsteriskPasswordTransformationMethod {
            return sInstance!!
        }

        private var sInstance: AsteriskPasswordTransformationMethod? = null
            get() {
                if(field == null) field = AsteriskPasswordTransformationMethod()
                return field
            }
    }

    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        return PasswordCharSequence(source)
    }

    private inner class PasswordCharSequence(private val mSource: CharSequence)// Store char sequence
        : CharSequence {
        override val length: Int
            get() = mSource.length

        override fun get(index: Int): Char = '*'

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return mSource.subSequence(startIndex, endIndex) // Return default
        }
    }
}