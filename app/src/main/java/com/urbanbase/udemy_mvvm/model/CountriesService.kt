package com.urbanbase.udemy_mvvm.model

import com.urbanbase.udemy_mvvm.di.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject

class CountriesService {

    /**
     * Dagger의 효과: nice seperation between how we create a variable and where we use it.
     */
    @Inject
    lateinit var api: CountriesApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getCountries(): Single<List<Country>> {
        return api.getCountries()
    }
}