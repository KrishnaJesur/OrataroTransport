package com.edusunsoft.transport.orataro.adapters

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.databinding.ItemRoutBinding
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRoutResModel
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRoutViewModel
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRouteResponseModel

class RoutListAdapter(private var selectRoutViewModel: SelectRoutViewModel, private var routList: ArrayList<SelectRouteResponseModel.RouteItem>)
    : RecyclerView.Adapter<RoutListAdapter.RoutListAdapterHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RoutListAdapterHolder {
        return RoutListAdapterHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_rout, viewGroup, false) as ItemRoutBinding)
    }

    override fun getItemCount(): Int {
        return routList.size
    }

    fun setData(routList: ArrayList<SelectRouteResponseModel.RouteItem>) {
        this.routList = routList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(routListAdapterHolder: RoutListAdapterHolder, position: Int) {
        routListAdapterHolder.bindView(selectRoutViewModel, routList[position])
    }

    class RoutListAdapterHolder(var itemRoutBinding: ItemRoutBinding) : RecyclerView.ViewHolder(itemRoutBinding.root) {

        fun bindView(selectRoutViewModel: SelectRoutViewModel, rout: SelectRouteResponseModel.RouteItem) {
            itemRoutBinding.mSelectRoutViewModel = selectRoutViewModel
            itemRoutBinding.mRout = rout
            itemRoutBinding.executePendingBindings()
        }

    }
    
}