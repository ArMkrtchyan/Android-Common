package com.github.armkrtchyan.common.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitModule(
    private val interceptors: BaseInterceptorsHelper,
) {

    private fun provideOkHttpClient(): OkHttpClient {
        val client =
            OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
        for (interceptor: Interceptor in interceptors.interceptors) {
            client.addInterceptor(interceptor)
        }
        return client.build()
    }

    fun providesRetrofit(): Retrofit =
        Retrofit.Builder()
            .client(provideOkHttpClient())
            .baseUrl("https://www.acbadigital.am/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}