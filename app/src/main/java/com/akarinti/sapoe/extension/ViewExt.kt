package com.akarinti.sapoe.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by ridwan on 03/03/2018.
 */

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}