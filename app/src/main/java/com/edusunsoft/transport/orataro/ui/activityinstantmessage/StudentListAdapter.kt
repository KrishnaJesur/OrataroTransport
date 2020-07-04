package com.edusunsoft.transport.orataro.ui.activityinstantmessage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.databinding.ItemStudentAttandenceBinding
import com.edusunsoft.transport.orataro.dialog.studentattendance.RecyclerviewCallbacks
import com.edusunsoft.transport.orataro.ui.activitymaps.GetRouteListModel
import com.edusunsoft.transport.orataro.utils.Controller
import org.jetbrains.anko.onCheckedChange


class StudentListAdapter(private var selectRoutViewModel: StudentListViewModel, var studentList: ArrayList<GetRouteListModel.StudentInfoItem>)
    : RecyclerView.Adapter<StudentListAdapter.StudentListAdapterHolder>() {

    companion object {
        var SelectedStudentList: ArrayList<String> = ArrayList()
        var callback: RecyclerviewCallbacks<GetRouteListModel.StudentInfoItem>? = null
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): StudentListAdapterHolder {
        return StudentListAdapterHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context), R.layout.item_student_attandence, viewGroup, false) as ItemStudentAttandenceBinding)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun setData(routList: ArrayList<GetRouteListModel.StudentInfoItem>) {
        this.studentList = routList
        notifyDataSetChanged()
    }

    fun setOnClick(click: RecyclerviewCallbacks<GetRouteListModel.StudentInfoItem>) {
        callback = click
    }


    override fun onBindViewHolder(routListAdapterHolder: StudentListAdapterHolder, position: Int) {
        routListAdapterHolder.bindView(selectRoutViewModel, studentList[position])
    }

    class StudentListAdapterHolder(var studentlistitemRoutBinding: ItemStudentAttandenceBinding) : RecyclerView.ViewHolder(studentlistitemRoutBinding.root) {

        fun bindView(selectRoutViewModel: StudentListViewModel, rout: GetRouteListModel.StudentInfoItem) {

            studentlistitemRoutBinding.selectstudentlistViewModel = selectRoutViewModel
            studentlistitemRoutBinding.mRout = rout
            studentlistitemRoutBinding.executePendingBindings()

            studentlistitemRoutBinding.itemCheckbox.isChecked = rout.isSelected

            if (rout.isSelected) {
                SelectedStudentList.add(rout.EmergencyContact1)
                Controller.convertArraylistToJsonArray(SelectedStudentList, "")
            } else {
                SelectedStudentList.clear()
            }

            studentlistitemRoutBinding.itemCheckbox.onCheckedChange { compoundButton, isChecked ->

                callback?.onItemClick(adapterPosition, rout, isChecked)

                if (isChecked) {
                    SelectedStudentList.add(rout.EmergencyContact1)
                    Controller.convertArraylistToJsonArray(SelectedStudentList, "")
                } else {
                    SelectedStudentList.remove(rout.EmergencyContact1)
                    Controller.convertArraylistToJsonArray(SelectedStudentList, "")
                }

            }

        }

    }

}
