package com.edusunsoft.transport.orataro.dialog.studentattendance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edusunsoft.transport.orataro.R

class StudentAttendanceAdapter(val context: Context) : RecyclerView.Adapter<StudentAttendanceAdapter.MyViewHolder>() {
    var itemList: List<String> = mutableListOf()
    private var selectedItem: Int = -1
    var callback: RecyclerviewCallbacks<String>? = null


    fun addItemList(filers: List<String>) {
        itemList = filers.toMutableList()
        notifyDataSetChanged()
    }

    fun selectedItem(position: Int) {
        selectedItem = position
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: MyViewHolder, p1: Int) {
        val item = itemList[p1]
        holder.tvName.text = item
    }

    fun setOnClick(click: RecyclerviewCallbacks<String>) {
        callback = click
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_student_attandence, p0, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var tvName: TextView = itemView.findViewById(R.id.tv_title)
        var mCheckBox: CheckBox = itemView.findViewById(R.id.item_checkbox)

        init {
            
            mCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                callback?.onItemClick(adapterPosition, itemList[adapterPosition], isChecked)
            }

        }

    }
}