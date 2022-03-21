package com.example.covidishaa.vaccine

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.covidishaa.R
import com.example.covidishaa.history.HistoryAdapter
import kotlinx.android.synthetic.main.fragment_vaccine.*
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.fragment_vaccine.view.*
import org.json.JSONException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VaccineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VaccineFragment : Fragment() {
    // TODO: Rename and change types of parameters
    // creating a variable for our button.


    // creating variable for our edit text.
    lateinit var pinCodeEdt: EditText

    // creating a variable for our recycler view.
    lateinit var centersRV: RecyclerView

    // creating a variable for adapter class.
    lateinit var centerRVAdapter: CenterRVAdapter

    // creating a variable for our list
    lateinit var centerList: List<CenterRvModal>

    // creating a variable for progress bar.
    lateinit var loadingPB: ProgressBar

    public fun getAppointments(pinCode: String, date: String) {
        val url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=" + pinCode + "&date=" + date
        val queue = Volley.newRequestQueue(activity)

        // on below line we are creating a request
        // variable for making our json object request.
        val request =
            // as we are getting json object response and we are making a get request.
            JsonObjectRequest(
                Request.Method.GET, url, null, { response ->
                    // this method is called when we get successful response from API.
                    Log.e("TAG", "SUCCESS RESPONSE IS $response")
                    // we are setting the visibility of progress bar as gone.
                    view?.idPBLoading?.setVisibility(View.GONE)
                    // on below line we are adding a try catch block.
                    try {
                        // in try block we are creating a variable for center
                        // array and getting our array from our object.
                        val centerArray = response.getJSONArray("centers")

                        // on below line we are checking if the length of the array is 0.
                        // the zero length indicates that there is no data for the given pincode.
                        if (centerArray.length().equals(0)) {
                            Toast.makeText(activity, "No Center Found", Toast.LENGTH_SHORT).show()
                        }
                        for (i in 0 until centerArray.length()) {

                            // on below line we are creating a variable for our center object.
                            val centerObj = centerArray.getJSONObject(i)

                            // on below line we are getting data from our session
                            // object and we are storing that in a different variable.
                            val centerName: String = centerObj.getString("name")
                            val centerAddress: String = centerObj.getString("address")
                            val centerFromTime: String = centerObj.getString("from")
                            val centerToTime: String = centerObj.getString("to")
                            val fee_type: String = centerObj.getString("fee_type")

                            // on below line we are creating a variable for our session object
                            val sessionObj = centerObj.getJSONArray("sessions").getJSONObject(0)
                            val ageLimit: Int = sessionObj.getInt("min_age_limit")
                            val vaccineName: String = sessionObj.getString("vaccine")
                            val avaliableCapacity: Int = sessionObj.getInt("available_capacity")

                            // after extracting all the data we are passing this
                            // data to our modal class we have created
                            // a variable for it as center.
                            val center = CenterRvModal(
                                centerName,
                                centerAddress,
                                centerFromTime,
                                centerToTime,
                                fee_type,
                                ageLimit,
                                vaccineName,
                                avaliableCapacity
                            )
                            // after that we are passing this modal to our list on the below line.
                            centerList = centerList + center
                        }

                        // on the below line we are passing this list to our adapter class.
                        centerRVAdapter = CenterRVAdapter(centerList)

                        // on the below line we are setting layout manager to our recycler view.
                        view?.centersRV?.layoutManager = LinearLayoutManager(requireContext())

                        // on the below line we are setting an adapter to our recycler view.
                        view?.centersRV?.adapter = centerRVAdapter

                        // on the below line we are notifying our adapter as the data is updated.
                        centerRVAdapter.notifyDataSetChanged()

                    } catch (e: JSONException) {
                        // below line is for handling json exception.
                        e.printStackTrace();
                    }
                },
                { error ->
                    // this method is called when we get any
                    // error while fetching data from our API
                    Log.e("TAG", "RESPONSE IS $error")
                    // in this case we are simply displaying a toast message.
                    Toast.makeText(activity, "Fail to get response", Toast.LENGTH_SHORT).show()
                })
        // at last we are adding
        // our request to our queue.
        queue.add(request)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_vaccine, container, false)


        centerList = ArrayList<CenterRvModal>()



        view.idBtnSearch.setOnClickListener {

            // inside on click listener we are getting data from
            // edit text and creating a val for ite on below line.
            val pinCode = view.idEdtPinCode.text.toString()

            // on below line we are validating
            // our pin code as 6 digit or not.
            if (pinCode.length != 6) {

                // this method is called when users enter invalid pin code.
                Toast.makeText(activity, "Please enter valid pin code", Toast.LENGTH_SHORT).show()
            } else {

                // if the pincode is correct.
                // first of all we are clearing our array list this
                // will clear the data in it if already present.
                (centerList as ArrayList<CenterRvModal>).clear()

                // on below line we are getting instance of our calendar.
                val c = Calendar.getInstance()

                // on below line we are getting our current year, month and day.
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                // on below line we are creating our date picker dialog.
                val dpd = context?.let { it1 ->
                    DatePickerDialog(
                        it1,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            // after that we are making our progress bar as visible.
                            view?.idPBLoading
                                ?.setVisibility(View.VISIBLE)

                            // on below line we are creating a date string for our date
                            val dateStr: String = """$dayOfMonth - ${monthOfYear + 1} - $year"""

                            // on below line we are calling a method to get
                            // the appointment info for vaccination centers
                            // and we are passing our pin code to it.
                            getAppointments(pinCode, dateStr)
                        },
                        year,
                        month,
                        day
                    )
                }
                // calling a method to display
                // our datepicker dialog.
                dpd?.show()
            }


    }


        // below is the method for getting data from API.



        return view
    }





}

