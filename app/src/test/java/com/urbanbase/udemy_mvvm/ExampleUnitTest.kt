package com.urbanbase.udemy_mvvm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.urbanbase.udemy_mvvm.model.CountriesService
import com.urbanbase.udemy_mvvm.model.Country
import com.urbanbase.udemy_mvvm.viewmodel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 * TDD의 활용 범위: business Logic 만을 위한 테스팅이기 때문에, 받은 데이터를 가지고 분기 행위를 테스팅해볼 수 있다.
 * 예를들어,
 * 1) 데이터 통신 에러에 대한 대응
 * 2) 회원 권한에 따른 분기 테스트 (미가입, 가입, 프리미엄)
 *
 * Urbanbase AR 에서의
 * 1) 좋아요 눌렀을 때의 화면 상태값 체크
 * 2) 실제 api 리턴값을 빠르게 확인 가능 (에뮬레이터나 폰 디버깅은 느림)
 */
class ExampleUnitTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()


    @Mock
    lateinit var countriesService: CountriesService

    @InjectMocks
    var listViewModel = ListViewModel()

    private var testSingle: Single<List<Country>>? =  null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getCountriesSuccess() {
        val country = Country("countryName", "capital", "url")
        val countriesList = arrayListOf(country)

        testSingle = Single.just(countriesList)

        /**
         * countriesService.getCountries() 부분만 mock 데이터를 연결한다.
         */
        Mockito.`when`(countriesService.getCountries()).thenReturn(testSingle)

        listViewModel.refresh()

        assertEquals(1, listViewModel.countries.value?.size)
        assertEquals(false, listViewModel.bLoadError.value)
        assertEquals(false, listViewModel.bLoading.value)
    }


    @Test
    fun getCountriesFail() {

        testSingle = Single.error(Throwable())

        Mockito.`when`(countriesService.getCountries()).thenReturn(testSingle)

        listViewModel.refresh()

        assertEquals(true, listViewModel.bLoadError.value)
        assertEquals(false, listViewModel.bLoadError.value)
    }

    @Test
    fun apiUrlValidateCheck() {
        listViewModel.refresh()

        assertEquals(20, listViewModel.countries.value?.size)
    }

    @Before
    fun setUpRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }

    }
}
