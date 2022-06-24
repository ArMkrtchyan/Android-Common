package com.github.armkrtchyan.common.widgets

import android.content.Context
import android.util.AttributeSet
import com.github.armkrtchyan.common.R
import com.github.armkrtchyan.common.shared.extensions.isValidEmail
import com.github.armkrtchyan.common.shared.extensions.isValidPassword
import com.github.armkrtchyan.common.shared.extensions.isValidRegex
import com.github.armkrtchyan.common.validators.Validator
import com.github.armkrtchyan.common.validators.ValidatorEnum
import com.google.android.material.textfield.TextInputEditText

class EditTextACBA : TextInputEditText, Validator {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private var mErrorMessage: String? = null
    private var mWithRegex: String? = null
    private var mNewRegex: String? = null
    private var mMinLength = 0
    private var mIsRequiredForValidation = false
    private lateinit var mValidatorEnum: ValidatorEnum
    private var mTextInputLayoutACBA: TextInputLayoutACBA? = null

    private fun init(attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.EditTextACBA).apply {
            mValidatorEnum = ValidatorEnum.values()[getInt(R.styleable.EditTextACBA_validator, 0)]
            mIsRequiredForValidation = getBoolean(R.styleable.EditTextACBA_isRequiredForValidation, false)
            mErrorMessage = getString(R.styleable.EditTextACBA_errorMessage)
            mWithRegex = getString(R.styleable.EditTextACBA_withRegex)
            mNewRegex = getString(R.styleable.EditTextACBA_changeRegexTo)
            mMinLength = getInt(R.styleable.EditTextACBA_minLength, 0)
            recycle()
        }
        if (mIsRequiredForValidation) onFocusChangeListener = null
    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        super.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) isValid()
            else setDefaultState()
            l?.onFocusChange(view, hasFocus)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (parent.parent is TextInputLayoutACBA) mTextInputLayoutACBA = parent.parent as TextInputLayoutACBA
    }

    override fun isRequiredForValidation() = mIsRequiredForValidation

    override fun isValid(): Boolean {
        if (isRequiredForValidation()) {
            val target = text.toString()
            if (target.isEmpty()) return setError(context.getString(R.string.empty_error_message))
            if (target.length < mMinLength) return setError(String.format(context.getString(R.string.minimum_length_error_message), mMinLength))
            return when (mValidatorEnum) {
                ValidatorEnum.EMAIL -> if (!target.isValidEmail(newRegex = mNewRegex, withRegex = mWithRegex)) setError(mErrorMessage) else true
                ValidatorEnum.PASSWORD -> if (!target.isValidPassword(newRegex = mNewRegex, withRegex = mWithRegex)) setError(mErrorMessage) else true
                ValidatorEnum.REG_EX -> if (!target.isValidRegex(withRegex = mWithRegex ?: "")) setError(mErrorMessage) else true
            }
        } else return true
    }

    override fun setError(message: String?): Boolean {
        mTextInputLayoutACBA?.error = message
        mTextInputLayoutACBA?.isErrorEnabled = true
        return false
    }

    override fun setDefaultState() {
        mTextInputLayoutACBA?.error = null
        mTextInputLayoutACBA?.isErrorEnabled = false
    }
}
