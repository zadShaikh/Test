//package com.mycardsapplication.data.local.dataBase
//
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.mycardsapplication.data.local.dataBase.daos.*
//import com.mycardsapplication.data.local.dataBase.model.*
//
//
//// UserDatabase represents database and contains the database holder and server the main access
//// point for the underlying connection to your app's persisted, relational data.
//
//@Database(entities = [(Customer::class),(Customer_Doc::class),(Mycards::class),(Customer_UserData::class),(Customer_Ocr::class),(Text_Note::class),(Voice_Note::class),(Notification_Count_myContact::class),(Notification_Count_myColleagues::class)], version = 1, exportSchema = false)
//abstract class AppDatabase : RoomDatabase() {
//
//    abstract fun customerDao(): customerDao
//    abstract fun customer_DocDao(): customer_DocDao
//    abstract fun mycardsDao(): mycardsDao
//    abstract fun cutomerUserDataDao(): cutomerUserDataDao
//    abstract fun customer_OCRDao():customer_OCRDao
//    abstract fun textNotesDao(): TextNotesDao
//    abstract fun voiceNotesDao(): VoiceNotesDao
//    abstract fun Notification_mycontact_Dao():Notification_mycontact_Dao
//    abstract fun Notification_mycolleagues_Dao(): Notification_mycolleagues_Dao
//
//    companion object {
//        @Volatile
//        public var sInstance: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase{
//            val tempInstance = sInstance
//
//            if (tempInstance != null) {
//                return tempInstance
//            }
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "myCards_database")
//                    .fallbackToDestructiveMigration()
//                    .allowMainThreadQueries()
//                    .build()
//                sInstance = instance
//                return instance
//            }
//        }
//    }
//
//
//}
