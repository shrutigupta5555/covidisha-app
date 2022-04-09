package com.example.covidishaa.medicine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.covidishaa.R
import kotlinx.android.synthetic.main.fragment_medicine.*
import kotlinx.android.synthetic.main.fragment_medicine.view.*


class MedicineFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_medicine, container, false)

        view.button_doner.setOnClickListener {
            val t: FragmentTransaction = this.requireFragmentManager().beginTransaction()
            val mFrag: Fragment = DonateFragment()
            t.replace(R.id.flFragment, mFrag)
            t.commit()
        }

        view.button_receiver.setOnClickListener {
            val t: FragmentTransaction = this.requireFragmentManager().beginTransaction()
            val mFrag: Fragment = NeedMedicineFragment()
            t.replace(R.id.flFragment, mFrag)
            t.commit()
        }


        return view
    }

}