package com.example.crudapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.crudapp.databinding.ActivityMainBinding
import com.example.crudapp.db.SubscriberDB
import com.example.crudapp.db.SubscriberRepository
import com.example.crudapp.viewmodel.SubscriberViewModel
import com.example.crudapp.viewmodel.SubscriberViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val subscriberDao = SubscriberDB.getInstance(application).subscriberDao
        val subscriberRepository = SubscriberRepository(subscriberDao)
        subscriberViewModel = ViewModelProvider(
            this, SubscriberViewModelFactory(subscriberRepository)
        )[SubscriberViewModel::class.java]
        binding.subscriberViewModel = subscriberViewModel
        binding.lifecycleOwner = this
        subscriberViewModel.subscribers.observe(this, Observer {
            Toast.makeText(this, "List Observed", Toast.LENGTH_LONG).show()
        })
    }
}