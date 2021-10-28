package com.iranwebyar.occasions.data.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "occasions")
class Occasion {
    @PrimaryKey
    var resId: Long? = 0
    var id: String? = "-"
    var name: String? = "-"
    var address: String? = "-"
    var crossStreet: String? = "-"
    var lat: Double? = 0.0
    var lng: Double? = 0.0
    var distance: Double? = 0.0
    var postalCode: String? = "-"
    var country: String? = "-"
    var state: String? = "-"
    var city: String? = "-"
}
