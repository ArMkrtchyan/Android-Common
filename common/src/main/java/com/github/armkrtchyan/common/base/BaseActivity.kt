package com.github.armkrtchyan.common.base

import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.github.armkrtchyan.common.databinding.LayoutErrorBinding
import com.github.armkrtchyan.common.databinding.LayoutLoadingBinding
import com.github.armkrtchyan.common.state.State
import com.github.armkrtchyan.common.validators.Validator
import kotlinx.coroutines.flow.collectLatest
import java.util.*

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected open val keepBindingAlive: Boolean = true
    private lateinit var _binding: VB
    protected val mBinding: VB
        get() = _binding
    protected abstract val inflate: (LayoutInflater) -> VB
    private val rootGroup by lazy { FrameLayout(this) }
    var mFragmentViewModel: BaseViewModel? = null
    private val layoutLoading by lazy { LayoutLoadingBinding.inflate(layoutInflater) }
    private val layoutError by lazy { LayoutErrorBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!::_binding.isInitialized || !keepBindingAlive) {
            _binding = inflate(layoutInflater)
        }
        rootGroup.removeAllViews()
        rootGroup.addView(_binding.root)
        setContentView(rootGroup)
        lifecycleScope.launchWhenResumed {
            mFragmentViewModel?.stateFlow?.collectLatest(::setState)
        }
    }

    protected fun validate(root: View = _binding.root) {
        if (root is ViewGroup) for (view in root.children) {
            validate(view)
        } else if (root is Validator) root.isValid()
    }

    protected fun setState(state: State) {
        rootGroup.removeView(layoutError.root)
        rootGroup.removeView(layoutLoading.root)
        layoutError.errorMessage.text = ""
        layoutError.retry.setOnClickListener(null)
        when (state) {
            is State.Loading -> {
                rootGroup.addView(layoutLoading.root)
            }
            is State.Empty -> {
            }
            is State.Error -> {
                rootGroup.addView(layoutError.root)
            }
            is State.Success -> {
            }
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val focusedView = currentFocus
            if (focusedView is EditText) {
                val out = Rect()
                focusedView.getGlobalVisibleRect(out)
                if (!out.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    focusedView.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    override fun attachBaseContext(newBase: Context?) {
        val locale = Locale("en")
        val newConfig = Configuration(newBase?.resources?.configuration)
        Locale.setDefault(locale)
        newConfig.setLocale(locale)
        super.attachBaseContext(newBase?.createConfigurationContext(newConfig))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}