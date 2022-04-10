package com.example.covidishaa.medicine

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.covidishaa.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_need_medicine.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NeedMedicineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NeedMedicineFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var db: FirebaseFirestore
    private lateinit var posts: MutableList<Medicine>
    private lateinit var adapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_need_medicine, container, false)

    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
//        rvContacts.apply {
//            // set a LinearLayoutManager to handle Android
//            // RecyclerView behavior
//            layoutManager = LinearLayoutManager(context)
//            // set the custom adapter to the RecyclerView
//            adapter = ContactAdapter(context, posts)
//        }

        //get firebase data
        db = FirebaseFirestore.getInstance()

        //initialise post
        posts = mutableListOf()
        adapter = ContactAdapter(requireContext(), posts)


        rvContacts.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )

        rvContacts.adapter = adapter
        rvContacts.layoutManager = LinearLayoutManager(context)


        val medicineReference = db.collection("medicine").whereEqualTo("verified", "true")

        medicineReference.addSnapshotListener{ snapshot, exception ->
            if (exception != null || snapshot == null){
                Log.e("chemck", "Exception while querying", exception)
                return@addSnapshotListener
            }

            val medicineList = snapshot.toObjects(Medicine::class.java)
            posts.clear()
            posts.addAll(medicineList)
            adapter.notifyDataSetChanged()
            for (med in medicineList){
                Log.i("chemck", "Med ${med}")
            }
        }
    }

    private fun createContacts(): List<Medicine> {
        val contacts = mutableListOf<Medicine>()





        for (i in 1..150) contacts.add(Medicine("Person #$i", "f", "f", "f"))
        return contacts
    }


}