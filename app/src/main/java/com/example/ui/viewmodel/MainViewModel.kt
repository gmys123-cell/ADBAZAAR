package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AdEntity
import com.example.data.model.AdStatus
import com.example.data.model.CategoryEntity
import com.example.data.model.PaymentEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.data.repository.AdBazaarRepository
import com.example.util.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = AdBazaarRepository(
        userDao = db.userDao(),
        adDao = db.adDao(),
        categoryDao = db.categoryDao(),
        paymentDao = db.paymentDao()
    )

    // Current App Language (Supports 13 Indian languages)
    val currentLanguage = MutableStateFlow(AppLanguage.GUJARATI)

    // Current Logged-in User
    val currentUserId = MutableStateFlow<Long?>(4L) // Default to Member user (id=4) for quick demo

    val currentUser: StateFlow<UserEntity?> = currentUserId
        .flatMapLatest { id ->
            if (id == null) flowOf(null) else repository.getUserByIdFlow(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Search and Filters
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow<String?>(null)
    val selectedLocation = MutableStateFlow<String?>(null)

    // Categories
    val categories: StateFlow<List<CategoryEntity>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Raw Approved Ads
    private val rawApprovedAds = repository.approvedAds

    // Filtered Approved Ads
    val approvedAds: StateFlow<List<AdEntity>> = combine(
        rawApprovedAds,
        searchQuery,
        selectedCategory,
        selectedLocation
    ) { ads, query, category, location ->
        ads.filter { ad ->
            val matchesQuery = query.isBlank() ||
                    ad.title.contains(query, ignoreCase = true) ||
                    ad.description.contains(query, ignoreCase = true) ||
                    ad.vendorName.contains(query, ignoreCase = true) ||
                    ad.vendorBusinessName?.contains(query, ignoreCase = true) == true

            val matchesCategory = category == null || category == "All" || ad.category.equals(category, ignoreCase = true)
            val matchesLocation = location == null || location == "All" || ad.location.contains(location, ignoreCase = true)

            matchesQuery && matchesCategory && matchesLocation
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Vendor Specific Ads
    val myVendorAds: StateFlow<List<AdEntity>> = currentUserId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else repository.getAdsByVendorId(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Vendor Payments History
    val myPayments: StateFlow<List<PaymentEntity>> = currentUserId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList()) else repository.getPaymentsByVendorId(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin Flow Data
    val pendingAds: StateFlow<List<AdEntity>> = repository.getPendingAds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingPayments: StateFlow<List<PaymentEntity>> = repository.getPendingPayments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPayments: StateFlow<List<PaymentEntity>> = repository.getAllPayments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalVendorCount: StateFlow<Int> = repository.getTotalVendorCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val activeVendorCount: StateFlow<Int> = repository.getActiveVendorCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalAdsCount: StateFlow<Int> = repository.getTotalAdsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val approvedAdsCount: StateFlow<Int> = repository.getApprovedAdsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendingPaymentsCount: StateFlow<Int> = repository.getPendingPaymentsCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalRevenue: StateFlow<Double?> = repository.getTotalApprovedRevenue()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Actions
    fun switchLanguage(lang: AppLanguage) {
        currentLanguage.value = lang
    }

    fun switchUserRole(role: UserRole) {
        viewModelScope.launch {
            when (role) {
                UserRole.MEMBER -> currentUserId.value = 4L // Sample Member
                UserRole.VENDOR -> currentUserId.value = 2L // Sample Approved Vendor
                UserRole.ADMIN -> currentUserId.value = 1L // Sample Admin
            }
        }
    }

    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email.trim())
            if (user != null && user.passwordHash == pass) {
                currentUserId.value = user.id
                onSuccess()
            } else {
                onError("ઇમેઇલ અથવા પાસવર્ડ ખોટો છે! (Invalid credentials)")
            }
        }
    }

    fun register(
        name: String,
        email: String,
        phone: String,
        pass: String,
        role: UserRole,
        businessName: String?,
        city: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val existing = repository.getUserByEmail(email.trim())
            if (existing != null) {
                onError("આ ઇમેઇલ પહેલેથી રજિસ્ટર્ડ છે! (Email already registered)")
                return@launch
            }

            val newUser = UserEntity(
                name = name.trim(),
                email = email.trim(),
                phone = phone.trim(),
                passwordHash = pass,
                role = role,
                isMember = (role == UserRole.MEMBER || role == UserRole.ADMIN), // Members are free, Admin free
                businessName = businessName?.trim(),
                city = city?.trim(),
                preferredLanguage = currentLanguage.value.code
            )

            val newId = repository.registerUser(newUser)
            currentUserId.value = newId
            onSuccess()
        }
    }

    fun logout() {
        currentUserId.value = null
    }

    fun submitMembershipPayment(utr: String, note: String?, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val user = currentUser.value
        if (user == null || user.role != UserRole.VENDOR) {
            onError("ફક્ત વેન્ડર જ ચૂકવણી કરી શકે છે! (Only vendors can submit payment)")
            return
        }

        if (utr.isBlank() || utr.length < 6) {
            onError("કૃપા કરીને માન્ય UTR / UPI ટ્રાન્ઝેક્શન નંબર દાખલ કરો! (Enter valid UTR number)")
            return
        }

        viewModelScope.launch {
            val payment = PaymentEntity(
                vendorId = user.id,
                vendorName = user.name,
                vendorEmail = user.email,
                vendorPhone = user.phone,
                amount = 201.0,
                utrNumber = utr.trim(),
                paymentNote = note?.trim(),
                status = com.example.data.model.PaymentStatus.PENDING
            )
            repository.submitPayment(payment)
            onSuccess()
        }
    }

    fun postNewAd(
        title: String,
        description: String,
        price: Double,
        category: String,
        location: String,
        contactNumber: String,
        whatsappNumber: String,
        imageUrisJson: String = "sample_ad_banner",
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val user = currentUser.value
        if (user == null || user.role != UserRole.VENDOR) {
            onError("ફક્ત Vendor જ એડ મૂકી શકે છે! (Only Vendors can post ads)")
            return
        }

        if (!user.isMember) {
            onError("એડ મૂકવા માટે પહેલા ₹201 મેમ્બરશિપ ફી ભરીને એડમિન પાસે એક્ટિવ કરાવવું જરૂરી છે! (Active ₹201 membership required)")
            return
        }

        if (title.isBlank() || description.isBlank() || price <= 0 || contactNumber.isBlank()) {
            onError("કૃપા કરીને તમામ જરૂરી વિગતો યોગ્ય રીતે ભરો! (Fill all required fields)")
            return
        }

        viewModelScope.launch {
            val newAd = AdEntity(
                title = title.trim(),
                description = description.trim(),
                price = price,
                category = category,
                imageUrisJson = imageUrisJson.ifBlank { "sample_ad_banner" },
                location = location.trim(),
                contactNumber = contactNumber.trim(),
                whatsappNumber = whatsappNumber.trim().ifBlank { contactNumber.trim() },
                vendorId = user.id,
                vendorName = user.name,
                vendorBusinessName = user.businessName ?: user.name,
                status = AdStatus.APPROVED // Vendors with active ₹201 membership post live ads directly!
            )
            repository.postAd(newAd)
            onSuccess()
        }
    }

    fun verifyPayment(paymentId: Long, approve: Boolean, adminNote: String?) {
        viewModelScope.launch {
            repository.verifyPayment(paymentId, approve, adminNote)
        }
    }

    fun approveAd(adId: Long) {
        viewModelScope.launch {
            repository.updateAdStatus(adId, AdStatus.APPROVED)
        }
    }

    fun rejectAd(adId: Long) {
        viewModelScope.launch {
            repository.updateAdStatus(adId, AdStatus.REJECTED)
        }
    }

    fun deleteAd(adId: Long) {
        viewModelScope.launch {
            repository.deleteAd(adId)
        }
    }

    fun viewAdDetails(adId: Long) {
        viewModelScope.launch {
            repository.incrementAdViews(adId)
        }
    }
}
