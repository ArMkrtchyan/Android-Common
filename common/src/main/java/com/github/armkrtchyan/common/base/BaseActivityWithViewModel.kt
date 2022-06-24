package com.github.armkrtchyan.common.base

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.collectLatest

abstract class BaseActivityWithViewModel<VB : ViewBinding, VIEWMODEL : BaseViewModel> : BaseActivity<VB>() {

    protected abstract val mViewModel: VIEWMODEL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            mViewModel.stateFlow.collectLatest(::setState)
        }
    }
}