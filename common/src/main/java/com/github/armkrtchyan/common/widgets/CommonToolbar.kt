package com.github.armkrtchyan.common.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import com.github.armkrtchyan.common.R


class CommonToolbar : Toolbar {
    constructor(context: Context) : super(context, null, R.attr.commonToolbarStyle) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.attr.commonToolbarStyle) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, R.attr.commonToolbarStyle) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.CommonToolbar).apply {
            recycle()
        }
    }
}