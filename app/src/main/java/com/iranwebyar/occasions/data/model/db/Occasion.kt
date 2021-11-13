package com.iranwebyar.occasions.data.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "occasions")
data class Occasion (
//    @PrimaryKey
//    var occasionId: Long? = 0
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var type: String? = "-",
    var title: String? = "-",
    var image: String? = "-",
    var date: String? = "-",
    var time: String? = "-",
    var alarm: Boolean? = true,
    var notification: Boolean? = true
)
