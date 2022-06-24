package com.github.armkrtchyan.common.base

abstract class BaseViewModelFragment<VIEWMODEL : BaseViewModel> : BaseFragment() {
    protected abstract val mViewModel: VIEWMODEL
    override fun onResume() {
        super.onResume()
        if (activity is BaseActivity<*>) {
            (activity as BaseActivity<*>).mFragmentViewModel = mViewModel
        }
    }
}