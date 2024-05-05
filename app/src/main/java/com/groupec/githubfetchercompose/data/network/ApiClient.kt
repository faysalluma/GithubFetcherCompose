package com.groupec.githubfetchercompose.data.network

import android.content.Context
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.groupec.githubfetchercompose.data.Constants.SERVER_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    fun buildClient(context: Context): Retrofit {
        val gson = GsonBuilder().setLenient().create()

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addNetworkInterceptor(FlipperOkhttpInterceptor(getNetworkFlipperPlugin(context)))
            .build()

        return Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun getNetworkFlipperPlugin(context: Context) = AndroidFlipperClient
    .getInstance(context)
    .getPluginByClass(NetworkFlipperPlugin::class.java)
}