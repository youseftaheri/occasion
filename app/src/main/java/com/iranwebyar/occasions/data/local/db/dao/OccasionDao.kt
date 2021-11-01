package com.iranwebyar.occasions.data.local.db.dao

import androidx.room.*
import com.iranwebyar.occasions.data.model.db.Occasion

@Dao
interface OccasionDao {
    @Query("DELETE FROM occasions")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(occasion: Occasion?)

    @Query("SELECT * FROM occasions WHERE id LIKE :id LIMIT 1")
    suspend fun findById(id: Long?): Occasion?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(occasion: Occasion?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(occasions: List<Occasion?>?)

    @Query("SELECT * FROM occasions")
    suspend fun loadAll(): List<Occasion>?

    @Query("SELECT * FROM occasions WHERE id IN (:occasionIds)")
    suspend fun loadAllByIds(occasionIds: List<Int?>?): List<Occasion?>?
}
