package com.example.test.data

import com.example.test.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Singleton

@Singleton
class AppInterceptor : Interceptor {
    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
        val token = BuildConfig.TOKEN
        val request =
            chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        proceed(request)
    }
}