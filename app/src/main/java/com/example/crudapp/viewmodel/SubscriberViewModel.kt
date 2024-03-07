package com.example.crudapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crudapp.Event
import com.example.crudapp.db.Subscriber
import com.example.crudapp.db.SubscriberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.file.Files.delete

class SubscriberViewModel(private val subscriberRepository: SubscriberRepository) : ViewModel() {
    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message : LiveData<Event<String>>
        get() = statusMessage

    val subscribers = subscriberRepository.subscribers

    private var isUpdateOrDelete = false
    private lateinit var subscribeToUpdateOrDelete: Subscriber

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        if (isUpdateOrDelete) {
            subscribeToUpdateOrDelete.name = inputName.value!!
            subscribeToUpdateOrDelete.email = inputEmail.value!!
            update(subscribeToUpdateOrDelete)
        } else {
            val name = inputName.value!!
            val email = inputEmail.value!!
            insert(Subscriber(0, name, email))
            inputName.value = ""
            inputEmail.value = ""
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(subscribeToUpdateOrDelete)
        } else {
            deleteAll()
        }
    }

    fun insert(subscriber: Subscriber) {
        viewModelScope.launch(Dispatchers.IO) {
            subscriberRepository.insert(subscriber)
            withContext(Dispatchers.Main){
                statusMessage.value = Event("Subscriber ${subscriber.name} inserted successfully.")
            }
        }
    }

    fun update(subscriber: Subscriber) {
        viewModelScope.launch(Dispatchers.IO) {
            subscriberRepository.update(subscriber)
            withContext(Dispatchers.Main) {
                statusMessage.value = Event("Subscriber ${subscriber.name} updated successfully.")
                inputName.value = ""
                inputEmail.value = ""
                isUpdateOrDelete = false
                saveOrUpdateButtonText.value = "Save"
                clearAllOrDeleteButtonText.value = "Clear All"
            }
        }
    }

    fun delete(subscriber: Subscriber) {
        viewModelScope.launch(Dispatchers.IO) {
            subscriberRepository.delete(subscriber)
            withContext(Dispatchers.Main) {
                statusMessage.value = Event("Subscriber ${subscriber.name} deleted successfully.")
                inputName.value = ""
                inputEmail.value = ""
                isUpdateOrDelete = false
                saveOrUpdateButtonText.value = "Save"
                clearAllOrDeleteButtonText.value = "Clear All"
            }
        }
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        subscriberRepository.deleteAll()
        withContext(Dispatchers.Main){
            statusMessage.value = Event("All Subscribers deleted successfully.")
        }
    }

    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscribeToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }
}