package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AdStatus {
    PENDING,
    APPROVED,
    REJECTED
}

@Entity(tableName = "ads")
data class AdEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrisJson: String, // Comma separated image URIs or fallback URLs
    val location: String, // City, State
    val contactNumber: String,
    val whatsappNumber: String,
    val vendorId: Long,
    val vendorName: String,
    val vendorBusinessName: String? = null,
    val status: AdStatus = AdStatus.PENDING,
    val viewsCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
