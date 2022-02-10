package com.example.covidishaa.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {

//@Insert
//fun insert(contactData: ContactData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)  //replaces the existing value with latest
    suspend fun insert(contactData: ContactData)  //suspend is a coroutine to run this fun in bg

    @Delete
    suspend fun delete(contactData: ContactData)

    @Query("Select * from contact_data")
    fun fetchDetails(): LiveData<List<ContactData>>
}