package com.stavro_xhardha.pockettreasure.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "countries")
data class Country(
    @SerializedName("name")
    @PrimaryKey
    @ColumnInfo(name = "name")
    var name: String,
    @SerializedName("capital")
    @ColumnInfo(name = "capital_city")
    var capitalCity: String
){
    public constructor(): this("", "")
}