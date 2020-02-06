package com.urbanbase.udemy_mvvm.di

import com.urbanbase.udemy_mvvm.model.CountriesService
import com.urbanbase.udemy_mvvm.viewmodel.ListViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service: CountriesService)

    fun inject(viewModel: ListViewModel)
}