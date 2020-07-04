package com.edusunsoft.transport.orataro.ui.activitylicense

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.databinding.ItemRoutBinding
import com.edusunsoft.transport.orataro.databinding.LdListRowBinding
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRoutResModel
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRoutViewModel
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRouteResponseModel

class LDListAdapter(private var ldViewModel: LDViewModel, private var driverldList: ArrayList<DriverLDResModel.DriverDocumentItem>)
    : RecyclerView.Adapter<LDListAdapter.RoutListAdapterHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RoutListAdapterHolder {
        return RoutListAdapterHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.ld_list_row, viewGroup, false) as LdListRowBinding)
    }

    override fun getItemCount(): Int {
        return driverldList.size
    }

    fun setData(routList: ArrayList<DriverLDResModel.DriverDocumentItem>) {
        this.driverldList = routList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(routListAdapterHolder: RoutListAdapterHolder, position: Int) {
        routListAdapterHolder.bindView(ldViewModel, driverldList[position])
    }

    class RoutListAdapterHolder(var ldListRowBinding: LdListRowBinding) : RecyclerView.ViewHolder(ldListRowBinding.root) {

        fun bindView(ldViewModel: LDViewModel, driverDocumentItem: DriverLDResModel.DriverDocumentItem) {
            ldListRowBinding.mldviewmodel = ldViewModel
            ldListRowBinding.mld = driverDocumentItem
            ldListRowBinding.executePendingBindings()
        }
    }
}