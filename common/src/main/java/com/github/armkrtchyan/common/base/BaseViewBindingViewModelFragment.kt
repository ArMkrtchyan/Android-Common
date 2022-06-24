package com.github.armkrtchyan.common.base

import androidx.viewbinding.ViewBinding

abstract class BaseViewBindingViewModelFragment<VB : ViewBinding, VIEWMODEL : BaseViewModel> : BaseViewBindingFragment<VB>() {
    protected abstract val mViewModel: VIEWMODEL
    override fun onResume() {
        super.onResume()
        if (activity is BaseActivity<*>) {
            (activity as BaseActivity<*>).mFragmentViewModel = mViewModel
        }
    }
}