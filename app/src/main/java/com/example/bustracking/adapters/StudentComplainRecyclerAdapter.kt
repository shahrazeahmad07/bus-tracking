package com.example.bustracking.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bustracking.R
import com.example.bustracking.modals.ComplainModal

class StudentComplainRecyclerAdapter(val complainModalArrayList: ArrayList<ComplainModal>) : RecyclerView.Adapter<StudentComplainRecyclerAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_student_complain, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvStudentComplain.text = complainModalArrayList[position].complain?.trim()
    }

    override fun getItemCount(): Int {
        return complainModalArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvStudentComplain: TextView

        init {
            tvStudentComplain = itemView.findViewById(R.id.tvStudentComplain)
        }
    }
}