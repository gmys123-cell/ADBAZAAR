package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PaymentStatus {
    PENDING,
    APPROVED,
    REJECTED
}

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val vendorId: Long,
    val vendorName: String,
    val vendorEmail: String,
    val vendorPhone: String,
    val amount: Double = 201.0,
    val utrNumber: String,
    val paymentNote: String? = null,
    val screenshotUri: String? = null,
    val status: PaymentStatus = PaymentStatus.PENDING,
    val submittedAt: Long = System.currentTimeMillis(),
    val verifiedAt: Long? = null,
    val adminNote: String? = null
)
