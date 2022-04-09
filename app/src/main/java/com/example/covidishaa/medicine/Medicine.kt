package com.example.covidishaa.medicine

import com.google.firebase.firestore.PropertyName

data class Medicine(
        @get:PropertyName("fname") @set:PropertyName("fname") var fname: String = "",
        @get:PropertyName("lname") @set:PropertyName("lname") var lname: String = "",
        @get:PropertyName("verified") @set:PropertyName("verified")  var verified: String = "",
        @get:PropertyName("url") @set:PropertyName("url")  var imageUrl : String = "",
        @get:PropertyName("phone") @set:PropertyName("phone")  var phone : String = "")

