package com.urbanbase.udemy_mvvm

import android.util.Log
import androidx.annotation.IntegerRes
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    val TEST_LOG = "boogil_test"

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.urbanbase.udemy_mvvm", appContext.packageName)


        val datas = arrayListOf(1, 2, 3, 4, 5)

        // 설명:
        // map: 최초에 들어온 데이터를 다른 형태로 변경 시킬 수 있다.
        // Observable과 Subject 차이: Observable 은 내부에서, Subject 는 외부에서 데이터를 주입
        /*Observable.fromIterable(datas)
            .map { it.toString() + 'a' }
            .subscribe { Log.i(TEST_LOG, it) }*/

        /*Observable.fromIterable(datas)
            .filter { (it % 2) != 0 }
            .map { it.toString() + "_test" }
            .subscribe { Log.i(TEST_LOG, it) }*/

        Observable.fromIterable(datas)
            .filter { (it % 2) != 0 }
            .map { value -> value.toString() }
            .flatMap { value -> Observable.range(0, 10).map { index -> value } }
            .collect({ ArrayList<String>() }, { chars, value -> chars.add(value.toString()) })
            .subscribe {
                    values ->
                Log.d(TEST_LOG, values.toString()) }


        Log.i(TEST_LOG, "test start ----- ")

        val subject = PublishSubject.create<String>()

        subject
            .map { data -> data.toString() + "_add" }
            .flatMap { value -> Observable.range(0, 10).map { index -> value } }
            .collect({ ArrayList<String>() } , { list, value -> list.add(value)})
            .subscribe {
                    list ->
                Log.i(TEST_LOG, "result: " + list.toString())}



        subject.onNext("q")
        subject.onComplete()
//        subject.onNext(2)
//        subject.onNext(5)
//        subject.onNext(5)
//        subject.onNext(5)




        val observable = Observable.create<Int> { emitter ->
            emitter.onNext(1)
            emitter.onNext(2)
            emitter.onNext(3)
            emitter.onNext(4)
            emitter.onNext(5)
            emitter.onComplete()
        }

        observable.subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.io())
            .filter { data -> (data % 2) != 0 }
            .map { data -> data.toString() + "a" }
            .observeOn(Schedulers.computation())
            .flatMap { value -> Observable.range(0 , 10).map { index -> value } }
            .collect({ ArrayList<String>() }, {list, value -> list.add(value)})
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list -> Log.i(TEST_LOG, list.toString()) }

    }
}
