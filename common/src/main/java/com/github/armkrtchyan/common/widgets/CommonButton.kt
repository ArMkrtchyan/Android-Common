package com.github.armkrtchyan.common.widgets

import android.content.Context
import android.util.AttributeSet
import com.github.armkrtchyan.common.R
import com.github.armkrtchyan.common.shared.PreventDoubleClickListener
import com.google.android.material.button.MaterialButton

class CommonButton : MaterialButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.attr.buttonStyle) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, R.attr.buttonStyle) {
        init(attrs)
    }

    private var mIsPreventDoubleClick = true

    private fun init(attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.CommonButton).apply {
            mIsPreventDoubleClick = getBoolean(R.styleable.CommonButton_isPreventClick, true)
            recycle()
        }
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        if (mIsPreventDoubleClick) super.setOnClickListener(PreventDoubleClickListener(onClickListener))
        else super.setOnClickListener(onClickListener)
    }
}