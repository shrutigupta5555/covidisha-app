package com.example.covidishaa.medicine

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.covidishaa.R
import com.example.covidishaa.utils.FirebaseUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_donate.*
import kotlinx.android.synthetic.main.fragment_donate.view.*
import kotlinx.android.synthetic.main.fragment_medicine.*
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DonateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DonateFragment : Fragment() {
    lateinit var imageView: ImageView
    lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null

    var button_date: Button? = null
    var textview_date: TextView? = null
    var cal = Calendar.getInstance()

    var button_da: Button? = null
    var textview_da: TextView? = null
    var tv_fname: TextView? = null
    var tv_lname: EditText? = null
    var ca = Calendar.getInstance()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    var storageReference = FirebaseStorage.getInstance().reference
    //this method will upload the file
    private fun uploadFile(fname : String, lname: String, d1: String, d2: String) {
        //if there is a file to upload
        Log.i("chemck", imageUri.toString())
        if (imageUri != null) {
            //displaying a progress dialog while upload is going on
            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Uploading")
            progressDialog.show()
            val currentUser:String = FirebaseUtils.firebaseAuth.currentUser?.phoneNumber.toString()
            val riversRef: StorageReference = storageReference.child("images/" + currentUser + ".jpg")

            riversRef.putFile(imageUri!!)
                    .addOnSuccessListener { task ->

                        //if the upload is successfull
                        //hiding the progress dialog
                        progressDialog.dismiss()

                        //upload data to firebase
                        riversRef.downloadUrl.addOnCompleteListener {
                            val docref = FirebaseUtils.db.collection("medicine")
                            val downloaduri =  it.result.toString()
                            val data = hashMapOf(
                                    "fname" to fname,
                                    "lname" to lname,
                                    "dl" to d1,
                                    "d2" to d2,
                                    "url" to downloaduri
                            )

                            docref.document(currentUser).set(data).addOnSuccessListener { documentReference ->
                                Log.d("TAG", "DocumentSnapshot written ")
                            }
                                    .addOnFailureListener { e ->
                                        Log.w("TAG", "Error adding document", e)
                                    }

                        }



                        //and displaying a success toast
                        Toast.makeText(context, "File Uploaded ", Toast.LENGTH_LONG).show()
                    }



                    .addOnFailureListener { exception -> //if the upload is not successfull
                        //hiding the progress dialog
                        progressDialog.dismiss()

                        //and displaying error message
                        Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
                    }

                    .addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot?> {
                        override fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) {
                            //calculating progress percentage
                            val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + progress.toInt() + "%...")
                        }
                    }



                    )

        } else {
            //you can display an error toast
        }
    }
    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.getTime())
    }

    private fun updateDate() {
        val myFor = "MM/dd/yyyy" // mention the format you need
        val sd = SimpleDateFormat(myFor, Locale.US)
        textview_da!!.text = sd.format(ca.getTime())
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContentView(R.layout.fragment_donate)

        imageView = this.ImageView
        button = this.ButtonLoadPicture
        button.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }



        // get the references from layout file
        tv_fname = this.et_fname
        tv_lname = this.et_lname
        textview_date = this.text_view_date_1
        button_date = this.button_date_1

        textview_da = this.text_view_date_2
        button_da = this.button_date_2


        textview_date!!.text = "--/--/----"
        textview_da!!.text = "--/--/----"



        // create an OnDateSetListener
        val dateSetList = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                ca.set(Calendar.YEAR, year)
                ca.set(Calendar.MONTH, monthOfYear)
                ca.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDate()
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        button_date!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(requireContext(),
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })

        button_submit!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val fname = (tv_fname as EditText?)?.text.toString()
                val lname = tv_lname?.text.toString()
                val d1 = textview_date!!.text.toString()
                val d2 = textview_da!!.text.toString()

                //upload image
                uploadFile(fname, lname, d1, d2)

            }

        })

        button_da!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(requireContext(),
                        dateSetList,
                        // set DatePickerDialog to point to today's date when it loads up
                        ca.get(Calendar.YEAR),
                        ca.get(Calendar.MONTH),
                        ca.get(Calendar.DAY_OF_MONTH)).show()
            }

        })





//        view.findViewById<Button>(R.id.button_submit).setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }
    }

    private fun setContentView(fragmentSecond: Int) {

    }
}