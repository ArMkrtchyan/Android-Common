package com.github.armkrtchyan.common.retrofit

import okhttp3.Interceptor

abstract class BaseInterceptorsHelper {
    abstract val interceptors: List<Interceptor>
}