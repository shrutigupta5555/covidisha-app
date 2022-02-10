package com.example.covidishaa.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactViewModel(application: Application): AndroidViewModel(application) {
    private val readAllData : LiveData<List<ContactData>>
    private val repository: ContactRepository

    init {
        val contactDao = ContactDatabase.getDatabase(application).contactDao()
        repository = ContactRepository(contactDao)
        readAllData = repository.realAllData
    }

    fun addContact(contact: ContactData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addContact(contact)
        }
    }
}