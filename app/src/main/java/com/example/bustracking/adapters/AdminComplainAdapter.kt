package com.example.bustracking.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bustracking.R
import com.example.bustracking.modals.ComplainModal

class AdminComplainAdapter(private val complainModalArrayList: ArrayList<ComplainModal>):  RecyclerView.Adapter<AdminComplainAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_complain, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvUserName.text = complainModalArrayList[position].author
        holder.tvComplain.text = complainModalArrayList[position].complain
    }

    override fun getItemCount(): Int {
        return complainModalArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvUserName: TextView
        var tvComplain: TextView

        init {
            tvUserName = itemView.findViewById(R.id.tvUserName)
            tvComplain = itemView.findViewById(R.id.tvComplain)
        }
    }
}