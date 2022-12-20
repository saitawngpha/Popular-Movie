package com.saitawngpha.popularmovie.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.saitawngpha.popularmovie.BuildConfig
import com.saitawngpha.popularmovie.api.ApiServices
import com.saitawngpha.popularmovie.utils.Constants.API_KEY
import com.saitawngpha.popularmovie.utils.Constants.BASE_URL
import com.saitawngpha.popularmovie.utils.Constants.NETWORK_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @Author: ၸၢႆးတွင်ႉၾႃႉ
 * @Date: 12/19/22
 */

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun ProvideBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun ConnectionTimeOut() = NETWORK_TIMEOUT

    @Provides
    @Singleton
    fun ProvideGson() : Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun ProvideOkHttpClient() = if(BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val requestInterceptor = Interceptor{
            val url = it.request()
                .url
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request = it.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor it.proceed(request)
        }

        OkHttpClient
            .Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }else{
        OkHttpClient
            .Builder()
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, gson: Gson, client: OkHttpClient): ApiServices =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiServices::class.java)


}