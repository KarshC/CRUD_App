package com.example.crudapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crudapp.db.Subscriber
import com.example.crudapp.db.SubscriberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SubscriberViewModel(private val subscriberRepository: SubscriberRepository): ViewModel() {
    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()

    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    val subscribers = subscriberRepository.subscribers

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate(){
        val name = inputName.value!!
        val email = inputEmail.value!!
        insert(Subscriber(0, name, email))
        inputName.value = ""
        inputEmail.value = ""
    }

    fun clearAllOrDelete(){
        deleteAll()
    }

    fun insert(subscriber: Subscriber){
        viewModelScope.launch(Dispatchers.IO){
            subscriberRepository.insert(subscriber)
        }
    }

    fun update(subscriber: Subscriber){
        viewModelScope.launch(Dispatchers.IO){
            subscriberRepository.update(subscriber)
        }
    }

    fun delete(subscriber: Subscriber){
        viewModelScope.launch(Dispatchers.IO){
            subscriberRepository.delete(subscriber)
        }
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) { subscriberRepository.deleteAll() }
}