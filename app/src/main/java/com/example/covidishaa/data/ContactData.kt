package com.example.covidishaa.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "contact_data")
class ContactData(@ColumnInfo(name = "time") val text:String,
                  @PrimaryKey(autoGenerate = false) var email: String,
                  @ColumnInfo(name = "date") val date : Date
) {

}

