package com.github.armkrtchyan.common.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.github.armkrtchyan.common.R
import com.github.armkrtchyan.common.shared.PreventDoubleClickListener

class CommonImageView : AppCompatImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private var mIsPreventDoubleClick = true

    private fun init(attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.CommonImageView).apply {
            mIsPreventDoubleClick = getBoolean(R.styleable.CommonImageView_isPreventClick, true)
            recycle()
        }
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        if (mIsPreventDoubleClick) super.setOnClickListener(PreventDoubleClickListener(onClickListener))
        else super.setOnClickListener(onClickListener)
    }
}