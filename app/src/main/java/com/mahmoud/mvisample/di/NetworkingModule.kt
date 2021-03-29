package com.mahmoud.mvisample.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mahmoud.mvisample.BuildConfig
import com.mahmoud.mvisample.boundaries.IRepository
import com.mahmoud.mvisample.data.IDataSource
import com.mahmoud.mvisample.data.remote.RemoteDataSource
import com.mahmoud.mvisample.data.Repository
import com.mahmoud.mvisample.data.remote.IApiService
import com.mahmoud.mvisample.domain.Constants.BASE_URL
import com.mahmoud.mvisample.domain.Constants.TIME_OUT
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkingBinds() {
    @Binds
    abstract fun bindHttpLogger(
        httpLogger: HttpLogger,
    ): HttpLoggingInterceptor.Logger

    @Binds
    abstract fun bindRemoteDataSource(
        remoteDataSource: RemoteDataSource,
    ): IDataSource

    @Binds
    abstract fun bindRepository(
        remoteDataSource: Repository,
    ): IRepository
}

@Module
@InstallIn(SingletonComponent::class)
class NetworkingModule {

    @Provides
    fun provideLoggingInterceptor(logger: HttpLoggingInterceptor.Logger): HttpLoggingInterceptor =
        HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun provideChuckInterceptor(@ApplicationContext appContext: Context): ChuckerInterceptor =
        ChuckerInterceptor.Builder(context = appContext)
            .alwaysReadResponseBody(true)
            .build()

    private fun OkHttpClient.Builder.addDebugInterceptors(
        loggingInterceptor: HttpLoggingInterceptor,
        chuck: ChuckerInterceptor,
    ): OkHttpClient.Builder {
        if (BuildConfig.DEBUG) {
            addInterceptor(loggingInterceptor)
            addInterceptor(chuck)
        }
        return this
    }

    @Provides
    fun provideHttpClient(
        headersInterceptor: HeadersInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        chuck: ChuckerInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addDebugInterceptors(loggingInterceptor, chuck)
        .addInterceptor(headersInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().serializeNulls().create()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @Provides
    fun provideServiceApi(retrofit: Retrofit): IApiService {
        return retrofit.create(IApiService::class.java)
    }

}

