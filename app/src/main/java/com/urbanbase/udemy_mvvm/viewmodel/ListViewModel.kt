package com.urbanbase.udemy_mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.urbanbase.udemy_mvvm.di.DaggerApiComponent
import com.urbanbase.udemy_mvvm.model.CountriesService
import com.urbanbase.udemy_mvvm.model.Country
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * ViewModel 구성요소:  LiveData
 */
class ListViewModel : ViewModel() {

    /**
     * 현재 DI를 이용하여 model 종속성 제거, 그리고 android view 관련 코드가 없다. => Only Business Logic 만 있다.
     */
    @Inject
    lateinit var countriesService: CountriesService  // seperate model
    private val disposable = CompositeDisposable()

    val countries = MutableLiveData<List<Country>>()
    val bLoadError = MutableLiveData<Boolean>()
    val bLoading = MutableLiveData<Boolean>()

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun refresh() {
        fetchCountries()
    }

    private fun fetchCountries() {
        bLoading.value = true
        disposable.add(
            countriesService
                .getCountries()
                .subscribeOn(Schedulers.newThread()) // subscribe to this observable on a new thread , 그래서 getCountries() 프로세스는 백엔드 스레드에서 돈다.
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<Country>>() {
                    override fun onSuccess(value: List<Country>?) {
                        countries.value = value
                        bLoadError.value = false
                        bLoading.value = false
                    }

                    override fun onError(e: Throwable?) {
                        bLoadError.value = true
                        bLoading.value = false
                    }
                })
        )
    }

    private fun fetchMockCountries() {
        val mockData = listOf<Country>(
            Country("Country_A"),
            Country("Country_B"),
            Country("Country_C"),
            Country("Country_D"),
            Country("Country_E"),
            Country("Country_F"),
            Country("Country_G"),
            Country("Country_H"),
            Country("Country_I"),
            Country("Country_J")
        )

        bLoadError.value = false
        bLoading.value = false
        countries.value = mockData
    }

    override fun onCleared() {
        super.onCleared()

        disposable.clear()
    }
}