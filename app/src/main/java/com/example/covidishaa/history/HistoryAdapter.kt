package com.example.covidishaa.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.covidishaa.R
import com.example.covidishaa.data.ContactData
import kotlinx.android.synthetic.main.custom_row.view.*

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    private var contactList = emptyList<ContactData>()
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent ,false))

    }

    override fun onBindViewHolder(holder: HistoryAdapter.MyViewHolder, position: Int) {
        val currentItem = contactList[position]
        holder.itemView.rvEmailId.text = currentItem.email
        holder.itemView.rvDate.text = currentItem.text
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    fun setData(contact: List<ContactData>){
        this.contactList = contact
        notifyDataSetChanged()
    }


}