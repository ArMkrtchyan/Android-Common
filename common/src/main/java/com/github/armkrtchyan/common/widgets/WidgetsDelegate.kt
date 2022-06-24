package com.github.armkrtchyan.common.widgets

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment

inline fun <reified T : View> Fragment.widget(): Lazy<T> {
    return lazy {
        when (T::class) {
            CommonButton::class.java -> CommonButton(requireContext()) as T
            else -> throw IllegalArgumentException("")
        }
    }
}

inline fun <reified T : View> Activity.widget(): Lazy<T> {
    return lazy {
        when (T::class) {
            CommonButton::class -> CommonButton(this) as T
            else -> throw IllegalArgumentException("")
        }
    }
}