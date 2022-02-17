package com.example.covidishaa

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covidishaa.utils.FirebaseUtils
import kotlinx.android.synthetic.main.fragment_profile.view.*
const val TAG = "profile"
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        var current: String? = FirebaseUtils.firebaseAuth.currentUser?.email?.split("@")?.get(0)
        var email : String? = FirebaseUtils.firebaseAuth.currentUser?.email
        var status: String? ;
        view.pfName.text = current
        view.pfEmail.text = email


        val docRef = email?.let { FirebaseUtils.db.collection("users").document(it) }
        docRef?.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
//                Log.d(TAG, "Current data: ${snapshot.data?.get("status")?.toString()}")
                  status = snapshot.data?.get("status")?.toString()
                  if (status == "1"){
                      view.tvRiskStatus.text = "You are safe"
                      view.imgRiskStatus.setImageResource(R.drawable.ic_safe)
                      view.highBtn.visibility = View.GONE

                  } else if (status == "2"){
                      view.tvRiskStatus.text = "You are at low risk"
                      view.imgRiskStatus.setImageResource(R.drawable.ic_low_risk)
                      view.highBtn.visibility = View.GONE
                  } else if(status == "3"){
                      view.tvRiskStatus.text = "You are at high risk"
                      view.imgRiskStatus.setImageResource(R.drawable.ic_high_risk)
                      view.highBtn.visibility = View.VISIBLE

                  } else if (status == "4"){
                      view.tvRiskStatus.text = "You have been infected"
                      view.imgRiskStatus.setImageResource(R.drawable.ic_high_risk)
                      view.highBtn.visibility = View.VISIBLE
                  }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()


    }



}