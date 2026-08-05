package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.AdStatus
import com.example.data.model.PaymentStatus
import com.example.data.model.UserRole

class Converters {
    @TypeConverter
    fun fromUserRole(value: UserRole): String = value.name

    @TypeConverter
    fun toUserRole(value: String): UserRole = try {
        UserRole.valueOf(value)
    } catch (e: Exception) {
        UserRole.MEMBER
    }

    @TypeConverter
    fun fromAdStatus(value: AdStatus): String = value.name

    @TypeConverter
    fun toAdStatus(value: String): AdStatus = try {
        AdStatus.valueOf(value)
    } catch (e: Exception) {
        AdStatus.PENDING
    }

    @TypeConverter
    fun fromPaymentStatus(value: PaymentStatus): String = value.name

    @TypeConverter
    fun toPaymentStatus(value: String): PaymentStatus = try {
        PaymentStatus.valueOf(value)
    } catch (e: Exception) {
        PaymentStatus.PENDING
    }
}
