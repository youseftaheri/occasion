package com.iranwebyar.occasions.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iranwebyar.occasions.data.local.db.dao.OccasionDao
import com.iranwebyar.occasions.data.model.db.Occasion

@Database(entities = [Occasion::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun occasionDao(): OccasionDao?
}
