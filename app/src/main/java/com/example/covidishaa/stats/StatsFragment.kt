package com.example.covidishaa.stats

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.covidishaa.R
import kotlinx.android.synthetic.main.fragment_stats.*
import kotlinx.android.synthetic.main.fragment_stats.view.*
import org.json.JSONException
import org.json.JSONObject


class StatsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private fun fetchdata() {

        // Create a String request
        // using Volley Library
        val url = "https://disease.sh/v3/covid-19/all"
        val request = StringRequest(
            Request.Method.GET,
            url,
            { response ->
                // Handle the JSON object and
                // handle it inside try and catch
                try {

                    // Creating object of JSONObject
                    val jsonObject = JSONObject(
                        response
                    )

                    // Set the data in text view
                    // which are available in JSON format
                    // Note that the parameter inside
                    // the getString() must match
                    // with the name given in JSON format
                    tvCases!!.text = jsonObject.getString(
                        "cases"
                    )
                    tvRecovered!!.text = jsonObject.getString(
                        "recovered"
                    )
                    tvCritical!!.text = jsonObject.getString(
                        "critical"
                    )
                    tvActive!!.text = jsonObject.getString(
                        "active"
                    )
                    tvTodayCases!!.text = jsonObject.getString(
                        "todayCases"
                    )
                    tvTotalDeaths!!.text = jsonObject.getString(
                        "deaths"
                    )
                    tvTodayDeaths!!.text = jsonObject.getString(
                        "todayDeaths"
                    )
                    tvAffectedCountries!!.text = jsonObject.getString(
                        "affectedCountries"
                    )
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { error ->
            Toast.makeText(
                activity,
                error.message,
                Toast.LENGTH_SHORT
            )
                .show()
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stats, container, false)


        fetchdata()

        view.indian.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://purva-28.github.io/Covid-19-Analysis-of-India/"))
            startActivity(browserIntent)
        }

        return view
    }

}

