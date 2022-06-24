package com.github.armkrtchyan.common.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import com.github.armkrtchyan.common.R
import com.github.armkrtchyan.common.shared.extensions.showToast
import com.github.armkrtchyan.common.validators.Validator
import kotlin.properties.Delegates

class CheckBoxACBA : AppCompatCheckBox, Validator {

    private var mIsRequiredForValidation by Delegates.notNull<Boolean>()
    private var mErrorMessage: String? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.CheckBoxACBA).apply {
            mErrorMessage = getString(R.styleable.CheckBoxACBA_errorMessage)
            mIsRequiredForValidation = getBoolean(R.styleable.CheckBoxACBA_mustBeChecked, false)
            recycle()
        }
    }

    override fun isRequiredForValidation() = mIsRequiredForValidation

    override fun isValid(): Boolean {
        if (!isChecked) return setError(mErrorMessage)
        return isChecked
    }

    override fun setError(message: String?): Boolean {
        context.showToast(message)
        return false
    }

    override fun setDefaultState() {

    }
}