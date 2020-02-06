package com.urbanbase.udemy_mvvm.di

import com.urbanbase.udemy_mvvm.model.CountriesApi
import com.urbanbase.udemy_mvvm.model.CountriesService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModule {
    private val BASE_URL = "https://raw.githubusercontent.com"


    /**
     * addCallAdapterFactory() => RxJava를 사용해서 Observable, Observer 를 사용하겠다는 의미
     * Observable: event 감지 후 event 를 emmit
     * Observer: subscribe 한 곳에서 event를 receive
     *
     * Single 클래스는 Observable 에 해당한다.
     */
    @Provides
    fun provideCountriesApi(): CountriesApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CountriesApi::class.java)
    }

    @Provides
    fun provideCountriesService(): CountriesService {
        return CountriesService()
    }
}