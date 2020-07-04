package com.edusunsoft.transport.orataro.ui.activitystudentattendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.databinding.StudentAttandenceListRowBinding
import com.edusunsoft.transport.orataro.dialog.studentattendance.RecyclerviewCallbacks
import com.edusunsoft.transport.orataro.ui.activitymaps.GetRouteListModel
import com.edusunsoft.transport.orataro.ui.activitymaps.MapsActivity
import com.edusunsoft.transport.orataro.utils.Controller
import org.jetbrains.anko.onCheckedChange
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StudentAttendanceListAdapter(private var studentAttendanceListViewModel: StudentAttendanceListViewModel, var sattendanceList: ArrayList<GetRouteListModel.StudentInfoItem>?) : RecyclerView.Adapter<StudentAttendanceListAdapter.StudentAttendanceListAdapterHolder>() {

    companion object {
        var SelectedStudentList: ArrayList<GetRouteListModel.StudentInfoItem> = ArrayList()
        var callback: RecyclerviewCallbacks<GetRouteListModel.StudentInfoItem>? = null
        private var CurrentDate: String = ""
    }


    fun GetCurrentDate(): String {

        val c = Calendar.getInstance().getTime()

        val CurrntDate = SimpleDateFormat("yyyy-MM-dd")
        return CurrntDate.format(c)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): StudentAttendanceListAdapterHolder {
        return StudentAttendanceListAdapterHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.student_attandence_list_row, viewGroup, false) as StudentAttandenceListRowBinding)
    }

    override fun getItemCount(): Int {
        return sattendanceList?.size!!
    }

    fun setData(sList: ArrayList<GetRouteListModel.StudentInfoItem>?) {

        this.sattendanceList = sList
        notifyDataSetChanged()

    }

    class StudentAttendanceListAdapterHolder(var studattendanceBinding: StudentAttandenceListRowBinding) : RecyclerView.ViewHolder(studattendanceBinding.root) {

        fun bindView(mViewModel: StudentAttendanceListViewModel, rout: GetRouteListModel.StudentInfoItem) {
            studattendanceBinding.studentattendancelistViewModel = mViewModel
            studattendanceBinding.mRout = rout
            studattendanceBinding.executePendingBindings()
        }

    }

    override fun onBindViewHolder(holder: StudentAttendanceListAdapterHolder, position: Int) {

        this!!.sattendanceList?.get(position)?.let {
            holder.bindView(studentAttendanceListViewModel, it)
            if (Controller.isRunningTrip) {
                holder.studattendanceBinding.itemCheckbox.visibility = View.VISIBLE
            } else {
                holder.studattendanceBinding.itemCheckbox.visibility = View.GONE
            }

            holder.studattendanceBinding.itemCheckbox.isChecked = this!!.sattendanceList?.get(holder.layoutPosition)!!.isSelected == true
            this!!.sattendanceList?.get(holder.layoutPosition)!!.StatusTerm = "Present"

            SelectedStudentList = this!!.sattendanceList!!
            Controller.convertArraylistToJsonArrayForStudentAttendance(SelectedStudentList, MapsActivity.BUSROUTEID, Controller.BusTripLogID, GetCurrentDate())


            holder.studattendanceBinding.itemCheckbox.onCheckedChange { compoundButton, isChecked ->

                //                callback?.onItemClick(position, this!!.sattendanceList, isChecked)

                if (isChecked) {

                    this!!.sattendanceList?.get(holder.layoutPosition)!!.StatusTerm = "Present"
                    this!!.sattendanceList?.get(holder.layoutPosition)!!.isSelected = true
                    SelectedStudentList = this!!.sattendanceList!!
                    Controller.convertArraylistToJsonArrayForStudentAttendance(SelectedStudentList, MapsActivity.BUSROUTEID, Controller.BusTripLogID, GetCurrentDate())

                } else {

                    this!!.sattendanceList?.get(holder.layoutPosition)!!.StatusTerm = "Absent"
                    this!!.sattendanceList?.get(holder.layoutPosition)!!.isSelected = false
                    SelectedStudentList = this!!.sattendanceList!!
                    Controller.convertArraylistToJsonArrayForStudentAttendance(SelectedStudentList, MapsActivity.BUSROUTEID, Controller.BusTripLogID, GetCurrentDate())

                }

            }

        }

    }

}
