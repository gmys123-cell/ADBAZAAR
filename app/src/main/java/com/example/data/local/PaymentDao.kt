package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PaymentEntity
import com.example.data.model.PaymentStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Query("SELECT * FROM payments WHERE vendorId = :vendorId ORDER BY submittedAt DESC")
    fun getPaymentsByVendorId(vendorId: Long): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE status = 'PENDING' ORDER BY submittedAt DESC")
    fun getPendingPayments(): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments ORDER BY submittedAt DESC")
    fun getAllPayments(): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE id = :paymentId LIMIT 1")
    suspend fun getPaymentById(paymentId: Long): PaymentEntity?

    @Query("SELECT SUM(amount) FROM payments WHERE status = 'APPROVED'")
    fun getTotalApprovedRevenue(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM payments WHERE status = 'PENDING'")
    fun getPendingPaymentsCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity): Long

    @Update
    suspend fun updatePayment(payment: PaymentEntity)

    @Query("UPDATE payments SET status = :status, verifiedAt = :verifiedAt, adminNote = :note WHERE id = :paymentId")
    suspend fun updatePaymentStatus(paymentId: Long, status: PaymentStatus, verifiedAt: Long?, note: String?)
}
