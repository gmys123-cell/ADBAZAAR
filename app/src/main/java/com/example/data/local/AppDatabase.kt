package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.AdEntity
import com.example.data.model.AdStatus
import com.example.data.model.CategoryEntity
import com.example.data.model.PaymentEntity
import com.example.data.model.PaymentStatus
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        AdEntity::class,
        CategoryEntity::class,
        PaymentEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun adDao(): AdDao
    abstract fun categoryDao(): CategoryDao
    abstract fun paymentDao(): PaymentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "adbazaar_database"
                )
                    .addCallback(DatabaseCallback(context.applicationContext))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateInitialData(database)
                }
            }
        }

        private suspend fun populateInitialData(db: AppDatabase) {
            // Seed Users
            val adminUser = UserEntity(
                id = 1,
                name = "AdBazaar Central Admin",
                email = "admin@adbazaar.com",
                phone = "9876543210",
                passwordHash = "admin123",
                role = UserRole.ADMIN,
                isMember = true,
                preferredLanguage = "gu"
            )

            val vendorApproved = UserEntity(
                id = 2,
                name = "Ramesh Bhai Patel",
                email = "vendor@adbazaar.com",
                phone = "9825012345",
                passwordHash = "vendor123",
                role = UserRole.VENDOR,
                isMember = true,
                membershipPaidAt = System.currentTimeMillis() - 86400000L * 5,
                businessName = "Shree Hari Textile & Sarees",
                city = "Surat, Gujarat",
                preferredLanguage = "gu"
            )

            val vendorPending = UserEntity(
                id = 3,
                name = "Shivam Shah",
                email = "shivam@techbazaar.in",
                phone = "9988776655",
                passwordHash = "vendor123",
                role = UserRole.VENDOR,
                isMember = false,
                businessName = "Shivam Mobile & Electronics",
                city = "Ahmedabad, Gujarat",
                preferredLanguage = "gu"
            )

            val memberUser = UserEntity(
                id = 4,
                name = "Vijay Kumar",
                email = "member@adbazaar.com",
                phone = "9123456789",
                passwordHash = "member123",
                role = UserRole.MEMBER,
                isMember = false,
                city = "Rajkot, Gujarat",
                preferredLanguage = "gu"
            )

            db.userDao().insertUser(adminUser)
            db.userDao().insertUser(vendorApproved)
            db.userDao().insertUser(vendorPending)
            db.userDao().insertUser(memberUser)

            // Seed Categories
            val categories = listOf(
                CategoryEntity(1, "Textiles & Fashion", "કાપડ અને ફેશન", "कपड़ों और फैशन", "textiles", "checkroom"),
                CategoryEntity(2, "Electronics & Mobiles", "ઇલેક્ટ્રોનિક્સ અને મોબાઇલ", "इलेक्ट्रॉनिक्स और मोबाइल", "electronics", "smartphone"),
                CategoryEntity(3, "Agriculture & Spices", "ખેતી ઉત્પાદનો અને મસાલા", "कृषि और मसाले", "agriculture", "grass"),
                CategoryEntity(4, "Automobiles & Tractors", "વાહનો અને ટ્રેક્ટર", "ऑटोमोबाइल और ट्रैक्टर", "automobiles", "directions_car"),
                CategoryEntity(5, "Real Estate & Shops", "રિયલ એસ્ટેટ અને શોપ", "रियल एस्टेट और दुकान", "realestate", "storefront"),
                CategoryEntity(6, "Industrial Machinery", "ઔદ્યોગિક મશીનરી", "औद्योगिक मशीनरी", "machinery", "precision_manufacturing"),
                CategoryEntity(7, "Food & Sweets", "ફૂડ, મીઠાઈ અને વાનગીઓ", "खाद्य पदार्थ और मिठाइयां", "food", "restaurant"),
                CategoryEntity(8, "Handicrafts & Gifts", "હસ્તકળા અને ગિફ્ટ", "हस्तशिल्प और उपहार", "handicrafts", "card_giftcard"),
                CategoryEntity(9, "Professional Services", "વ્યાવસાયિક સેવાઓ", "व्यावसायिक सेवाएं", "services", "work")
            )
            db.categoryDao().insertCategories(categories)

            // Seed Ads
            val ads = listOf(
                AdEntity(
                    id = 1,
                    title = "શુદ્ધ સુરતી પટોળા અને બાંધણી સાડી હોલસેલ દરો (Surti Silk Sarees)",
                    description = "અમારી પાસે પ્રીમિયમ સુરતી સિલ્ક સાડી, પટોળા, લહેંગા ચોલી અને બાંધણીનો વિશાળ સંગ્રહ છે. ભારતભરમાં કુરિયર ડિલિવરી ઉપલબ્ધ છે. હોલસેલરો અને રિટેલરો માટે વિશેષ ડિસ્કાઉન્ટ.",
                    price = 1450.0,
                    category = "Textiles & Fashion",
                    imageUrisJson = "sample_saree_1,sample_saree_2",
                    location = "Surat, Gujarat",
                    contactNumber = "9825012345",
                    whatsappNumber = "9825012345",
                    vendorId = 2,
                    vendorName = "Ramesh Bhai Patel",
                    vendorBusinessName = "Shree Hari Textile & Sarees",
                    status = AdStatus.APPROVED,
                    viewsCount = 142
                ),
                AdEntity(
                    id = 2,
                    title = "ઓર્ગેનિક કટોલ અને શુદ્ધ ગીર ગાયનું ઘી (Pure Organic Gir Cow Ghee)",
                    description = "100% શુદ્ધ વૈદિક A2 ગીર ગાયનું ઘી. ઓર્ગેનિક મસાલા અને ઘઉં સીધા ખેતરમાંથી. ફ્રી હોમ ડિલિવરી તમામ મુખ્ય શહેરોમાં.",
                    price = 1200.0,
                    category = "Agriculture & Spices",
                    imageUrisJson = "sample_ghee_1",
                    location = "Junagadh, Gujarat",
                    contactNumber = "9825012345",
                    whatsappNumber = "9825012345",
                    vendorId = 2,
                    vendorName = "Ramesh Bhai Patel",
                    vendorBusinessName = "Gir Organic Farm",
                    status = AdStatus.APPROVED,
                    viewsCount = 98
                ),
                AdEntity(
                    id = 3,
                    title = "મહિન્દ્રા 575 DI ટ્રેક્ટર 2022 મોડેલ એકદમ નવી જેવું (Mahindra Tractor)",
                    description = "સારી કન્ડિશનમાં મહિન્દ્રા ટ્રેક્ટર. ફક્ત 450 કલાક ચાલેલું. તમામ કાગળો કમ્પ્લીટ. તાત્કાલિક વેચવાનું છે.",
                    price = 485000.0,
                    category = "Automobiles & Tractors",
                    imageUrisJson = "sample_tractor_1",
                    location = "Rajkot, Gujarat",
                    contactNumber = "9825012345",
                    whatsappNumber = "9825012345",
                    vendorId = 2,
                    vendorName = "Ramesh Bhai Patel",
                    vendorBusinessName = "Patel Auto Traders",
                    status = AdStatus.APPROVED,
                    viewsCount = 210
                ),
                AdEntity(
                    id = 4,
                    title = "અમદાવાદ સી.જી. રોડ પર પ્રાઇમ લોકેશન કોમર્શિયલ શોપ (Prime Commercial Shop)",
                    description = "મેઇન રોડ પર 450 ચોરસ ફૂટ કોમર્શિયલ દુકાન ભાડે અથવા વેચાણ માટે. રેડી વિથ ઓલ ફર્નિચર.",
                    price = 3500000.0,
                    category = "Real Estate & Shops",
                    imageUrisJson = "sample_shop_1",
                    location = "Ahmedabad, Gujarat",
                    contactNumber = "9825012345",
                    whatsappNumber = "9825012345",
                    vendorId = 2,
                    vendorName = "Ramesh Bhai Patel",
                    vendorBusinessName = "Prime Realty",
                    status = AdStatus.APPROVED,
                    viewsCount = 315
                ),
                AdEntity(
                    id = 5,
                    title = "સૂર્યમુંખી કાપડ કટીંગ CNC મશીન ઔદ્યોગિક (Automatic Textile CNC Machine)",
                    description = "હાઈ સ્પીડ ફુલ્લી ઓટોમેટિક કાપડ કટીંગ મશીન. 1 વર્ષની ઓન-સાઇટ વોરંટી અને ફ્રી ઇન્સ્ટોલેશન સાથે.",
                    price = 280000.0,
                    category = "Industrial Machinery",
                    imageUrisJson = "sample_machine_1",
                    location = "Surat, Gujarat",
                    contactNumber = "9825012345",
                    whatsappNumber = "9825012345",
                    vendorId = 2,
                    vendorName = "Ramesh Bhai Patel",
                    vendorBusinessName = "Patel Industrial Machines",
                    status = AdStatus.APPROVED,
                    viewsCount = 76
                ),
                AdEntity(
                    id = 6,
                    title = "પ્રીમિયમ 5G સ્માર્ટફોન અને લેપટોપ જથ્થાબંધ ભાવોમાં (Refurbished 5G Laptops)",
                    description = "રિફર્બિશ્ડ બ્રાન્ડેડ લેપટોપ અને સ્માર્ટફોન 6 મહિનાની વોરંટી સાથે. વિદ્યાર્થીઓ અને શોપ ઓનર્સ માટે સ્પેશિયલ ઓફર.",
                    price = 18500.0,
                    category = "Electronics & Mobiles",
                    imageUrisJson = "sample_laptop_1",
                    location = "Ahmedabad, Gujarat",
                    contactNumber = "9988776655",
                    whatsappNumber = "9988776655",
                    vendorId = 3,
                    vendorName = "Shivam Shah",
                    vendorBusinessName = "Shivam Mobile & Electronics",
                    status = AdStatus.PENDING,
                    viewsCount = 12
                )
            )

            for (ad in ads) {
                db.adDao().insertAd(ad)
            }

            // Seed Initial Payment Request (Pending for Shivam Shah)
            val pendingPayment = PaymentEntity(
                id = 1,
                vendorId = 3,
                vendorName = "Shivam Shah",
                vendorEmail = "shivam@techbazaar.in",
                vendorPhone = "9988776655",
                amount = 201.0,
                utrNumber = "UTR982347102398",
                paymentNote = "Paid ₹201 via Google Pay UPI for Vendor Membership activation.",
                status = PaymentStatus.PENDING,
                submittedAt = System.currentTimeMillis() - 3600000L * 3
            )
            val approvedPayment = PaymentEntity(
                id = 2,
                vendorId = 2,
                vendorName = "Ramesh Bhai Patel",
                vendorEmail = "vendor@adbazaar.com",
                vendorPhone = "9825012345",
                amount = 201.0,
                utrNumber = "UTR441908234190",
                paymentNote = "Paid ₹201 via PhonePe UPI.",
                status = PaymentStatus.APPROVED,
                submittedAt = System.currentTimeMillis() - 86400000L * 5,
                verifiedAt = System.currentTimeMillis() - 86400000L * 5 + 1800000L,
                adminNote = "Payment verified in Axis Bank account."
            )

            db.paymentDao().insertPayment(pendingPayment)
            db.paymentDao().insertPayment(approvedPayment)
        }
    }
}
