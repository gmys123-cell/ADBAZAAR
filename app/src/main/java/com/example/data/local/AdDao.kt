package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AdEntity
import com.example.data.model.AdStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface AdDao {
    @Query("SELECT * FROM ads WHERE status = 'APPROVED' ORDER BY createdAt DESC")
    fun getApprovedAds(): Flow<List<AdEntity>>

    @Query("SELECT * FROM ads WHERE vendorId = :vendorId ORDER BY createdAt DESC")
    fun getAdsByVendorId(vendorId: Long): Flow<List<AdEntity>>

    @Query("SELECT * FROM ads WHERE id = :id LIMIT 1")
    suspend fun getAdById(id: Long): AdEntity?

    @Query("SELECT * FROM ads WHERE status = 'PENDING' ORDER BY createdAt DESC")
    fun getPendingAds(): Flow<List<AdEntity>>

    @Query("SELECT * FROM ads ORDER BY createdAt DESC")
    fun getAllAds(): Flow<List<AdEntity>>

    @Query("SELECT COUNT(*) FROM ads")
    fun getTotalAdsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM ads WHERE status = 'APPROVED'")
    fun getApprovedAdsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM ads WHERE status = 'PENDING'")
    fun getPendingAdsCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAd(ad: AdEntity): Long

    @Update
    suspend fun updateAd(ad: AdEntity)

    @Query("UPDATE ads SET status = :status WHERE id = :adId")
    suspend fun updateAdStatus(adId: Long, status: AdStatus)

    @Query("UPDATE ads SET viewsCount = viewsCount + 1 WHERE id = :adId")
    suspend fun incrementAdViews(adId: Long)

    @Query("DELETE FROM ads WHERE id = :adId")
    suspend fun deleteAd(adId: Long)
}
