package com.example.crudapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudapp.databinding.ActivityMainBinding
import com.example.crudapp.db.Subscriber
import com.example.crudapp.db.SubscriberDB
import com.example.crudapp.db.SubscriberRepository
import com.example.crudapp.viewmodel.SubscriberViewModel
import com.example.crudapp.viewmodel.SubscriberViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel
    private lateinit var subscriberAdapter: MyRecyclerViewAdapter
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
        initRecyclerView()
        subscriberViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initRecyclerView() {
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        subscriberAdapter =
            MyRecyclerViewAdapter({ selectedItem: Subscriber -> listClick(selectedItem) })
        binding.subscriberRecyclerView.adapter = subscriberAdapter
        displaySubscriberList()
    }

    private fun displaySubscriberList() {
        subscriberViewModel.subscribers.observe(this, Observer {
            subscriberAdapter.setList(it)
            subscriberAdapter.notifyDataSetChanged()
        })
    }

    private fun listClick(subscriber: Subscriber) {
        //Toast.makeText(this, "Selected Name is ${subscriber.name}", Toast.LENGTH_LONG).show()
        subscriberViewModel.initUpdateAndDelete(subscriber)
    }
}