package com.example.covidishaa.history

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.covidishaa.R
import com.example.covidishaa.data.ContactData
import com.example.covidishaa.medicine.Medicine
import com.example.covidishaa.utils.FirebaseUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.custom_row.view.*

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    private var contactList = emptyList<ContactData>()
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    private lateinit var db : FirebaseFirestore
    var status = arrayOf("Safe", "Low Risk", "High Risk", "Infected" )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent ,false))

    }

    override fun onBindViewHolder(holder: HistoryAdapter.MyViewHolder, position: Int) {

        db = FirebaseFirestore.getInstance()
        val currentItem = contactList[position]

        val medicineReference = db.collection("users").whereEqualTo("unique", currentItem.email)

        medicineReference.get().addOnSuccessListener { documents ->
            for (document in documents) {
                holder.itemView.rvStatus.text = status[Integer.parseInt(document.data["status"].toString()) - 1]
                holder.itemView.rvPhone.text = document.id
                Log.d("history", "${document.id} => ${document.data}")
            }
        }
                .addOnFailureListener { exception ->
                    Log.w("history", "Error getting documents: ", exception)
                }

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