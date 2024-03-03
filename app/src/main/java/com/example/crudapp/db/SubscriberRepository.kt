package com.example.crudapp.db

class SubscriberRepository(private val dao: SubscriberDao) {
    val subscribers = dao.getAllSubscribers()

    suspend fun insert(subscriber: Subscriber) {
        dao.insertSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber) {
        dao.deleteSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber) {
        dao.updateSubscriber(subscriber)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }
}