package com.akarinti.sapoe.view.component

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class GridItemDecoration(val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = spacing
        outRect.right = spacing
        outRect.bottom = spacing

        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = spacing
        } else {
            outRect.top = 0
        }
    }
}