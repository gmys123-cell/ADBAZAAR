package com.example.data.repository

import com.example.data.local.AdDao
import com.example.data.local.CategoryDao
import com.example.data.local.PaymentDao
import com.example.data.local.UserDao
import com.example.data.model.AdEntity
import com.example.data.model.AdStatus
import com.example.data.model.CategoryEntity
import com.example.data.model.PaymentEntity
import com.example.data.model.PaymentStatus
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import kotlinx.coroutines.flow.Flow

class AdBazaarRepository(
    private val userDao: UserDao,
    private val adDao: AdDao,
    private val categoryDao: CategoryDao,
    private val paymentDao: PaymentDao
) {
    // Users & Auth
    fun getUserByIdFlow(userId: Long): Flow<UserEntity?> = userDao.getUserByIdFlow(userId)

    suspend fun getUserByEmail(email: String): UserEntity? = userDao.getUserByEmail(email)

    suspend fun getUserById(userId: Long): UserEntity? = userDao.getUserById(userId)

    suspend fun registerUser(user: UserEntity): Long = userDao.insertUser(user)

    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)

    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()

    fun getTotalVendorCount(): Flow<Int> = userDao.getTotalVendorCount()

    fun getActiveVendorCount(): Flow<Int> = userDao.getActiveVendorCount()

    // Ads
    val approvedAds: Flow<List<AdEntity>> = adDao.getApprovedAds()

    fun getAdsByVendorId(vendorId: Long): Flow<List<AdEntity>> = adDao.getAdsByVendorId(vendorId)

    fun getPendingAds(): Flow<List<AdEntity>> = adDao.getPendingAds()

    fun getAllAds(): Flow<List<AdEntity>> = adDao.getAllAds()

    suspend fun getAdById(adId: Long): AdEntity? = adDao.getAdById(adId)

    suspend fun postAd(ad: AdEntity): Long = adDao.insertAd(ad)

    suspend fun updateAd(ad: AdEntity) = adDao.updateAd(ad)

    suspend fun updateAdStatus(adId: Long, status: AdStatus) = adDao.updateAdStatus(adId, status)

    suspend fun incrementAdViews(adId: Long) = adDao.incrementAdViews(adId)

    suspend fun deleteAd(adId: Long) = adDao.deleteAd(adId)

    fun getTotalAdsCount(): Flow<Int> = adDao.getTotalAdsCount()

    fun getApprovedAdsCount(): Flow<Int> = adDao.getApprovedAdsCount()

    fun getPendingAdsCount(): Flow<Int> = adDao.getPendingAdsCount()

    // Categories
    val allCategories: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    // Payments & Membership
    fun getPaymentsByVendorId(vendorId: Long): Flow<List<PaymentEntity>> = paymentDao.getPaymentsByVendorId(vendorId)

    fun getPendingPayments(): Flow<List<PaymentEntity>> = paymentDao.getPendingPayments()

    fun getAllPayments(): Flow<List<PaymentEntity>> = paymentDao.getAllPayments()

    fun getTotalApprovedRevenue(): Flow<Double?> = paymentDao.getTotalApprovedRevenue()

    fun getPendingPaymentsCount(): Flow<Int> = paymentDao.getPendingPaymentsCount()

    suspend fun submitPayment(payment: PaymentEntity): Long = paymentDao.insertPayment(payment)

    suspend fun verifyPayment(paymentId: Long, approve: Boolean, adminNote: String?) {
        val payment = paymentDao.getPaymentById(paymentId) ?: return
        val newStatus = if (approve) PaymentStatus.APPROVED else PaymentStatus.REJECTED
        paymentDao.updatePaymentStatus(paymentId, newStatus, System.currentTimeMillis(), adminNote)

        if (approve) {
            // Activate Vendor membership!
            userDao.updateMembershipStatus(payment.vendorId, true, System.currentTimeMillis())
        }
    }
}
