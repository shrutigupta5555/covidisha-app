package com.example.covidishaa.data


import android.content.Context
import android.provider.ContactsContract
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.covidishaa.utils.Converters

@Database(entities = [ContactData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ContactDatabase: RoomDatabase() {
    abstract  fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null

        fun getDatabase(context: Context): ContactDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDatabase::class.java,
                    "contact_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}