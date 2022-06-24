package com.github.armkrtchyan.common.shared

import android.os.SystemClock
import android.view.View

class PreventDoubleClickListener(private val onClickListener: View.OnClickListener?) : View.OnClickListener {
    private var mClickedTime = 0L
    override fun onClick(view: View?) {
        if (SystemClock.elapsedRealtime() - mClickedTime > 1000) {
            mClickedTime = SystemClock.elapsedRealtime()
            onClickListener?.onClick(view)
        }
    }
}