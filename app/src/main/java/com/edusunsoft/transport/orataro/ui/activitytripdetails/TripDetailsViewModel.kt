package com.edusunsoft.transport.orataro.ui.activitytripdetails

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.LinearLayoutManager
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.ui.activitymaps.MapsViewModel
import kotlinx.android.synthetic.main.no_data_found_layout.view.*

class TripDetailsViewModel(private var navigator: TripDetailsNavigator, context: Context)
    : BaseViewModel<TripDetailsNavigator>(navigator, context) {

    lateinit var layoutManager: LinearLayoutManager
    lateinit var tripdetailslistListAdapter: TripDetailsListAdapter
    var isShow: ObservableBoolean = ObservableBoolean(false)

    init {

        if (MapsViewModel.RouteList!!.isNotEmpty()) {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            tripdetailslistListAdapter = TripDetailsListAdapter(this@TripDetailsViewModel, ArrayList())
            tripdetailslistListAdapter.setData(MapsViewModel.RouteList)

        } else {

            isShow.set(true)
            navigator.DisplayMessageForNoRouteListAvailable()

        }


    }


}