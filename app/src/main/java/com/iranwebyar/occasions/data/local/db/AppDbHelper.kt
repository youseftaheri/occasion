package com.iranwebyar.occasions.data.local.db

import com.iranwebyar.occasions.data.model.db.Occasion
import javax.inject.Inject
import javax.inject.Singleton

//@Singleton
class AppDbHelper @Inject constructor(private val mAppDatabase: AppDatabase) : DbHelper {

    override suspend fun deleteAll() {
        mAppDatabase.occasionDao()!!.deleteAll()
    }

    override suspend fun allOccasions(): List<Occasion>?{
        return mAppDatabase.occasionDao()!!.loadAll()
    }

    override suspend fun insertOccasion(occasion: Occasion?) {
        mAppDatabase.occasionDao()!!.insert(occasion)
    }

    override suspend fun findOccasionById(id: Long?) {
        mAppDatabase.occasionDao()!!.findById(id)
    }
}
