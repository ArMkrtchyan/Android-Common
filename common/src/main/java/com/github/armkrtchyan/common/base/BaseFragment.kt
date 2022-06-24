package com.github.armkrtchyan.common.base

import android.content.Context
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    protected lateinit var mActivity: BaseActivity<*>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            mActivity = context
        }
    }
}