package com.example.namtd8_androidbasic_day8.data.network

import com.example.namtd8_androidbasic_day8.utils.Const.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
    fun <Api> buildAPI(
        api: Class<Api>
    ): Api  {
        val clientBuilder = OkHttpClient.Builder()

        val loggingInterceptor = HttpLoggingInterceptor()

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        clientBuilder.addInterceptor(loggingInterceptor)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                clientBuilder.build()
            ).addConverterFactory(
                GsonConverterFactory.create()
            ).build().create(api)
    }
}