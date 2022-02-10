package com.example.covidishaa.data


import androidx.lifecycle.LiveData

class ContactRepository(private  val contactDao: ContactDao) {
    val realAllData: LiveData<List<ContactData>> = contactDao.fetchDetails()

    suspend fun addContact(contactData: ContactData) {
        contactDao.insert(contactData)
    }
    suspend fun deleteContact(contactData: ContactData){
        contactDao.delete(contactData)
    }
}