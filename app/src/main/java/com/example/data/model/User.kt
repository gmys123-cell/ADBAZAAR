package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    MEMBER,
    VENDOR,
    ADMIN
}

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
    val phone: String,
    val passwordHash: String,
    val role: UserRole,
    val isMember: Boolean = false, // true if Vendor ₹201 membership fee paid and approved by Admin
    val membershipPaidAt: Long? = null,
    val businessName: String? = null,
    val city: String? = null,
    val preferredLanguage: String = "gu"
)
