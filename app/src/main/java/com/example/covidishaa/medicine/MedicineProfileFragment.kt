package com.example.covidishaa.medicine

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.covidishaa.R
import com.example.covidishaa.utils.FirebaseUtils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_medicine_profile.view.*
import kotlinx.android.synthetic.main.fragment_medicine_profile.view.tvAddress


class MedicineProfileFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_medicine_profile, container, false)

        val currentUser = FirebaseUtils.firebaseAuth.currentUser!!.phoneNumber.toString()

        val db = FirebaseFirestore.getInstance()

        db.collection("medicine").whereEqualTo("phone", currentUser).get().addOnSuccessListener { documents ->

            for (document in documents){
                if (document != null) {
                    view.tvAddress.text = "Verified:  ${document.data?.get("verified").toString()}"
                    Glide.with(requireContext()).load(document.data?.get("url")).into(view.ivPrescription)
                    Log.d("TAG", "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d("TAG", "No such document")
                }

            }

        }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }

        return view
    }

}