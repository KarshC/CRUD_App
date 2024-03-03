package com.example.crudapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subscriber::class], version = 1)
abstract class SubscriberDB: RoomDatabase() {
    abstract val subscriberDao: SubscriberDao
    companion object{
        @Volatile
        private var INSTANCE: SubscriberDB? = null
        fun getInstance(context: Context): SubscriberDB {
            var instance = INSTANCE
            if(instance == null){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    SubscriberDB::class.java,
                    "subscriber_data_database"
                ).build()
                INSTANCE = instance
            }
            return instance
        }
    }
}