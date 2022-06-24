package com.github.armkrtchyan.common.shared.extensions

import android.util.Log

inline fun <reified T> T.log(tag: String = "Common", prefix: String = ""): T {
    Log.d(tag, prefix + this.toString())
    return this
}