package com.github.armkrtchyan.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.viewbinding.ViewBinding
import com.github.armkrtchyan.common.shared.Inflater
import com.github.armkrtchyan.common.validators.Validator

abstract class BaseViewBindingFragment<VB : ViewBinding> : BaseFragment() {

    protected open val keepBindingAlive: Boolean = true
    private lateinit var _binding: VB
    protected val mBinding: VB
        get() = _binding
    protected abstract val inflate: Inflater<VB>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (!::_binding.isInitialized || !keepBindingAlive) {
            _binding = inflate(inflater, container, false)
        }
        return _binding.root
    }

    protected fun validate(root: View = mBinding.root) {
        if (root is ViewGroup) {
            for (view in root.children) {
                validate(view)
            }
        } else if (root is Validator) {
            if (root.isRequiredForValidation()) {
                root.isValid()
            }
        }
    }
}