package com.edusunsoft.transport.orataro.ui.activitytripdetails

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.databinding.ItemTripDetailsBinding
import com.edusunsoft.transport.orataro.ui.activitymaps.GetRouteListModel


class TripDetailsListAdapter(private var tripDetailsViewModel: TripDetailsViewModel, private var routeList: ArrayList<GetRouteListModel.RouteItem>?)
    : RecyclerView.Adapter<TripDetailsListAdapter.TripDetailsListAdapterHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TripDetailsListAdapterHolder {
        return TripDetailsListAdapterHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_trip_details, viewGroup, false) as ItemTripDetailsBinding)
    }

    override fun getItemCount(): Int {
        return routeList?.size!!
    }

    fun setData(routList: ArrayList<GetRouteListModel.RouteItem>?) {

        this.routeList = routList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(routListAdapterHolder: TripDetailsListAdapterHolder, position: Int) {
        this!!.routeList?.get(position)?.let {
            routListAdapterHolder.bindView(tripDetailsViewModel, it)
            if (position == 0) {
                routListAdapterHolder.itemRoutBinding.imgPickupIcon.visibility = View.VISIBLE
                routListAdapterHolder.itemRoutBinding.imgDropOff.visibility = View.GONE
                routListAdapterHolder.itemRoutBinding.imgLocation.visibility = View.GONE
            } else if (position == routeList!!.size - 1) {
                routListAdapterHolder.itemRoutBinding.imgPickupIcon.visibility = View.GONE
                routListAdapterHolder.itemRoutBinding.imgDropOff.visibility = View.VISIBLE
                routListAdapterHolder.itemRoutBinding.imgLocation.visibility = View.GONE
                routListAdapterHolder.itemRoutBinding.view1.visibility = View.GONE

            } else {
                routListAdapterHolder.itemRoutBinding.imgPickupIcon.visibility = View.GONE
                routListAdapterHolder.itemRoutBinding.imgDropOff.visibility = View.GONE
                routListAdapterHolder.itemRoutBinding.imgLocation.visibility = View.VISIBLE
            }
        }

    }

    class TripDetailsListAdapterHolder(var itemRoutBinding: ItemTripDetailsBinding) : RecyclerView.ViewHolder(itemRoutBinding.root) {

        fun bindView(mViewModel: TripDetailsViewModel, rout: GetRouteListModel.RouteItem) {
            itemRoutBinding.mTripDetailsViewModel = mViewModel
            itemRoutBinding.mRout = rout
            itemRoutBinding.executePendingBindings()
        }
    }
}