package com.iranwebyar.occasions.data.local.db

import com.iranwebyar.occasions.data.model.db.Occasion

interface DbHelper {
    suspend fun deleteAll()
    suspend fun allOccasions(): List<Occasion>?
    suspend fun insertOccasion(occasion: Occasion?)
    suspend fun findOccasionById(id: String?)
}
