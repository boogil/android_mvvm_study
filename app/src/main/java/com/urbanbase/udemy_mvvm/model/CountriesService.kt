package com.urbanbase.udemy_mvvm.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CountriesService {

    private val BASE_URL = "https://raw.githubusercontent.com"
    private val api: CountriesApi

    /**
     * addCallAdapterFactory() => RxJava를 사용해서 Observable, Observer 를 사용하겠다는 의미
     * Observable: event 감지 후 event 를 emmit
     * Observer: subscribe 한 곳에서 event를 receive
     *
     * Single 클래스는 Observable 에 해당한다.
     */
    init {
        api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CountriesApi::class.java)
    }

    fun getCountries(): Single<List<Country>> {
        return api.getCountries()
    }
}