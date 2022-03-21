package com.example.covidishaa

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.covidishaa.data.ContactViewModel
import com.example.covidishaa.extensions.Extensions.toast
import com.example.covidishaa.utils.FirebaseUtils
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

const val TAG = "profile"
class ProfileFragment : Fragment() {
    var email: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        lateinit var  mContactViewModel: ContactViewModel
        var current: String? = FirebaseUtils.firebaseAuth.currentUser?.email?.split("@")?.get(0)
        email  = FirebaseUtils.firebaseAuth.currentUser?.email
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

        fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
            observe(lifecycleOwner, object : Observer<T> {
                override fun onChanged(t: T?) {
                    observer.onChanged(t)
                    removeObserver(this)
                }
            })
        }

        fun calculateDiff(currItem: Long, dateItem: Long): Long {
            val diff: Long = currItem - dateItem
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            return days
        }
        lateinit var  mContactViewModel: ContactViewModel
        var data : HashMap<String, MutableList<String>> = HashMap<String, MutableList<String>>()

        var currDate = Date()



        view?.highBtn?.setOnClickListener(){
            email?.let { it1 -> Log.i("hemhe", it1) }
            mContactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
            mContactViewModel.readAllData.observeOnce(viewLifecycleOwner, Observer { contact ->
                for (item in contact) {

                    val dateItem = SimpleDateFormat("dd/MM/yyyy").parse(item.text)
                    val duration: String = calculateDiff(currDate.time, dateItem.time).toString()

                    if (data.containsKey(duration)) {
                        val list = data.get(duration)
                        list?.add(item.email)
                        if (list != null) {
                            data.put(duration, list)
                        }

                    } else {
                        val list = mutableListOf<String>()
                        list.add(item.email)
                        data.put(duration, list)
                    }




//                    Log.i(TAG, duration.toString())
//                    val diff : Long = currDate.time - dateItem.time
//                    Log.i(TAG, "date: $currDate, purana: $dateItem, og: ${item.text}, diff: $diff")
                }



                email?.let { it1 ->


                    FirebaseUtils.db.collection("contacts").document(it1).set(data).addOnSuccessListener {
//                           add http call to notify

                        Fuel.get("https://covidisha.herokuapp.com/notify/$it1").response { request, response, result ->
                            Log.i("hemhe", response.toString())
                        }
                        Toast.makeText(activity, "Data uploaded successfully", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(activity, "There was some error, try again later", Toast.LENGTH_SHORT).show()
                    }
                }

            })

        }


    }



}