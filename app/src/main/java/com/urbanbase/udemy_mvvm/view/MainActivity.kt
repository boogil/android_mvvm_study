package com.urbanbase.udemy_mvvm.view

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.urbanbase.udemy_mvvm.R
import com.urbanbase.udemy_mvvm.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ListViewModel
    private val countryAdapter = CountryListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        MVVM: MVC의 Controller 가 ViewModel로 바뀜. 그리고 ViewModel은 UI단에 위치

         <각각의 설명>
           1. View : Ui 관련
           2. Model: 데이터의 처리, 즉 데이터베이스와 통신
           3. ViewModel: View와는 Binding, Command 로 연결하고, Model 과는 데이터를 주고 받는 역할음
           (현재 코드는 Databinding 까지 적용은 되어있지 않아, view 안에서의 binding 코드들이 들어가 있어 의존적 형태를 지속시키고 있다.
           ViewBinding 적용 참고: https://medium.com/@jsuch2362/android-%EC%97%90%EC%84%9C-mvvm-%EC%9C%BC%EB%A1%9C-%EA%B8%B4-%EC%97%AC%EC%A0%95%EC%9D%84-82494151f312 )

          <역할 정리>
           View: '화면을 보여주거나 액션을 받을 수 있는 UI 부분'
           ViewModel: 데이터 가공 후 View에게 전달 (비지니스 로직 담당)

          <효과>
          현재 코드는 View와 비지니스 로직의 의존성을 제거하였다.

        */


        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()  // View 와 데이터를 주고 받

        rv_country.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countryAdapter
        }

        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            viewModel.refresh()
        }

        observeViewModel()  // View binding by code
    }


    fun observeViewModel() {
        with(viewModel) {
            countries.observe(this@MainActivity, Observer { countries ->
                countries?.let {
                    rv_country.visibility = View.VISIBLE
                    countryAdapter.updateData(it) }
            })

            bLoadError.observe(this@MainActivity, Observer { bError ->
                bError?.let {
                    tv_error.visibility = if (it) View.VISIBLE else View.GONE
                }
            })


            bLoading.observe(this@MainActivity, Observer { bLoading ->
                bLoading?.let {
                    progress.visibility = if (it) View.VISIBLE else View.GONE
                    if (it) {
                        tv_error.visibility = View.GONE
                        rv_country.visibility = View.GONE
                    }
                }

            })
        }
    }
}
