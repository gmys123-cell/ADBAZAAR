package com.example.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    GUJARATI("gu", "Gujarati", "ગુજરાતી"),
    HINDI("hi", "Hindi", "हिंदी"),
    MARATHI("mr", "Marathi", "मराठी"),
    BENGALI("bn", "Bengali", "বাংলা"),
    TAMIL("ta", "Tamil", "தமிழ்"),
    TELUGU("te", "Telugu", "తెలుగు"),
    KANNADA("kn", "Kannada", "ಕನ್ನಡ"),
    MALAYALAM("ml", "Malayalam", "മലയാളം"),
    PUNJABI("pa", "Punjabi", "ਪੰਜਾਬੀ"),
    ODIA("or", "Odia", "ଓଡ଼ିଆ"),
    URDU("ur", "Urdu", "اردو"),
    ASSAMESE("as", "Assamese", "અસમિયા")
}

class LanguageManager {
    var currentLanguage by mutableStateOf(AppLanguage.GUJARATI)

    fun setLanguage(language: AppLanguage) {
        currentLanguage = language
    }
}

val LocalLanguageManager = compositionLocalOf { LanguageManager() }

object L10n {
    fun getString(key: String, lang: AppLanguage): String {
        val map = dictionary[key] ?: return key
        return map[lang] ?: map[AppLanguage.ENGLISH] ?: key
    }

    private val dictionary: Map<String, Map<AppLanguage, String>> = mapOf(
        "app_title" to mapOf(
            AppLanguage.ENGLISH to "AdBazaar",
            AppLanguage.GUJARATI to "એડબજાર",
            AppLanguage.HINDI to "एडबाज़ार",
            AppLanguage.MARATHI to "ॲडबाजार",
            AppLanguage.BENGALI to "এডবাজার",
            AppLanguage.TAMIL to "அட்பஜார்",
            AppLanguage.TELUGU to "యాడ్ బజార్",
            AppLanguage.KANNADA to "ಆಡ್ ಬಜಾರ್",
            AppLanguage.MALAYALAM to "ആഡ് ബസാർ",
            AppLanguage.PUNJABI to "ਐਡਬਜ਼ਾਰ",
            AppLanguage.ODIA to "ଆଡ୍ ବଜାର",
            AppLanguage.URDU to "ایڈ بازار",
            AppLanguage.ASSAMESE to "এডবজাৰ"
        ),
        "app_tagline" to mapOf(
            AppLanguage.ENGLISH to "India's Premier Business & Advertisement Hub",
            AppLanguage.GUJARATI to "ભારતનું અગ્રણી વ્યાવસાયિક જાહેરાત પ્લેટફોર્મ",
            AppLanguage.HINDI to "भारत का अग्रणी व्यावसायिक विज्ञापन मंच",
            AppLanguage.MARATHI to "भारतातील प्रमुख व्यावसायिक जाहिरात मंच",
            AppLanguage.BENGALI to "ভারতের প্রধান ব্যবসায়ী বিজ্ঞাপন প্ল্যাটফর্ম"
        ),
        "role_member" to mapOf(
            AppLanguage.ENGLISH to "Member",
            AppLanguage.GUJARATI to "સામાન્ય સભ્ય",
            AppLanguage.HINDI to "सामान्य सदस्य",
            AppLanguage.MARATHI to "सामान्य सदस्य"
        ),
        "role_vendor" to mapOf(
            AppLanguage.ENGLISH to "Vendor",
            AppLanguage.GUJARATI to "વેન્ડર / વિક્રેતા",
            AppLanguage.HINDI to "वेंडर / विक्रेता",
            AppLanguage.MARATHI to "विक्रेता"
        ),
        "role_admin" to mapOf(
            AppLanguage.ENGLISH to "Admin",
            AppLanguage.GUJARATI to "એડમિન",
            AppLanguage.HINDI to "एडमिन",
            AppLanguage.MARATHI to "अॅडमिन"
        ),
        "home_tab" to mapOf(
            AppLanguage.ENGLISH to "Explore Ads",
            AppLanguage.GUJARATI to "એડ્સ જુઓ",
            AppLanguage.HINDI to "विज्ञापन देखें",
            AppLanguage.MARATHI to "जाहिराती पहा"
        ),
        "post_ad_tab" to mapOf(
            AppLanguage.ENGLISH to "Post AD",
            AppLanguage.GUJARATI to "નવી એડ મૂકો",
            AppLanguage.HINDI to "नया विज्ञापन डालें",
            AppLanguage.MARATHI to "जाहिरात टाका"
        ),
        "my_ads_tab" to mapOf(
            AppLanguage.ENGLISH to "My Ads",
            AppLanguage.GUJARATI to "મારી એડ્સ",
            AppLanguage.HINDI to "मेरे विज्ञापन",
            AppLanguage.MARATHI to "माझ्या जाहिराती"
        ),
        "membership_tab" to mapOf(
            AppLanguage.ENGLISH to "Membership ₹201",
            AppLanguage.GUJARATI to "મેમ્બરશિપ ₹201",
            AppLanguage.HINDI to "सदस्यता ₹201",
            AppLanguage.MARATHI to "सदस्यत्व ₹201"
        ),
        "admin_panel" to mapOf(
            AppLanguage.ENGLISH to "Admin Portal",
            AppLanguage.GUJARATI to "એડમિન પોર્ટલ",
            AppLanguage.HINDI to "एडमिन पोर्टल",
            AppLanguage.MARATHI to "अॅडमिन पोर्टल"
        ),
        "search_hint" to mapOf(
            AppLanguage.ENGLISH to "Search business, shop, product...",
            AppLanguage.GUJARATI to "શોપ, બિઝનેસ કે પ્રોડક્ટ શોધો...",
            AppLanguage.HINDI to "दुकान, व्यवसाय या उत्पाद खोजें...",
            AppLanguage.MARATHI to "दुकान, व्यवसाय किंवा उत्पादन शोधा..."
        ),
        "category_all" to mapOf(
            AppLanguage.ENGLISH to "All Categories",
            AppLanguage.GUJARATI to "તમામ કેટેગરી",
            AppLanguage.HINDI to "सभी श्रेणियां",
            AppLanguage.MARATHI to "सर्व श्रेणी"
        ),
        "location_all" to mapOf(
            AppLanguage.ENGLISH to "All India",
            AppLanguage.GUJARATI to "સમગ્ર ભારત",
            AppLanguage.HINDI to "संपूर्ण भारत",
            AppLanguage.MARATHI to "संपूर्ण भारत"
        ),
        "vendor_only_notice" to mapOf(
            AppLanguage.ENGLISH to "Only Vendors can post advertisements. Normal members can browse and contact.",
            AppLanguage.GUJARATI to "ફક્ત Vendor જ એડ મૂકી શકે છે. સામાન્ય સભ્ય ફક્ત એડ જોઈ અને સંપર્ક કરી શકે છે.",
            AppLanguage.HINDI to "केवल वेंडर ही विज्ञापन डाल सकते हैं। सामान्य सदस्य केवल देख और संपर्क कर सकते हैं।",
            AppLanguage.MARATHI to "फक्त विक्रेतेच जाहिरात देऊ शकतात. सामान्य सदस्य फक्त पाहू शकतात."
        ),
        "call_vendor" to mapOf(
            AppLanguage.ENGLISH to "Call Vendor",
            AppLanguage.GUJARATI to "કોલ કરો",
            AppLanguage.HINDI to "कॉल करें",
            AppLanguage.MARATHI to "कॉल करा"
        ),
        "whatsapp_contact" to mapOf(
            AppLanguage.ENGLISH to "WhatsApp",
            AppLanguage.GUJARATI to "વોટ્સએપ સંદેશ",
            AppLanguage.HINDI to "व्हाट्सएप संदेश",
            AppLanguage.MARATHI to "व्हॉट्सॲप"
        ),
        "pay_membership_title" to mapOf(
            AppLanguage.ENGLISH to "Vendor Membership Fee: ₹201",
            AppLanguage.GUJARATI to "વેન્ડર મેમ્બરશિપ ફી: ₹201",
            AppLanguage.HINDI to "वेंडर सदस्यता शुल्क: ₹201",
            AppLanguage.MARATHI to "विक्रेता सदस्यत्व शुल्क: ₹201"
        ),
        "pay_membership_desc" to mapOf(
            AppLanguage.ENGLISH to "Pay ₹201 once for lifetime unlimited ad postings across India.",
            AppLanguage.GUJARATI to "જીવનભર અનલિમિટેડ એડ મૂકવા માટે ફક્ત એકવાર ₹201 ચૂકવો.",
            AppLanguage.HINDI to "जीवनभर असीमित विज्ञापन पोस्ट करने के लिए केवल एक बार ₹201 का भुगतान करें।",
            AppLanguage.MARATHI to "आयुष्यभर अमर्याद जाहिराती देण्यासाठी एकदाच ₹201 भरा."
        ),
        "bank_details" to mapOf(
            AppLanguage.ENGLISH to "Bank Account Details",
            AppLanguage.GUJARATI to "બેંક એકાઉન્ટ વિગતો",
            AppLanguage.HINDI to "बैंक खाता विवरण",
            AppLanguage.MARATHI to "बँक खाते तपशील"
        ),
        "utr_number" to mapOf(
            AppLanguage.ENGLISH to "UTR / UPI Transaction ID",
            AppLanguage.GUJARATI to "UTR નંબર / UPI ટ્રાન્ઝેક્શન આઈડી",
            AppLanguage.HINDI to "UTR नंबर / UPI ट्रांजेक्शन आईडी",
            AppLanguage.MARATHI to "UTR क्रमांक / UPI आयडी"
        ),
        "submit_payment" to mapOf(
            AppLanguage.ENGLISH to "Submit Payment Proof",
            AppLanguage.GUJARATI to "ચૂકવણીની ખાતરી સબમિટ કરો",
            AppLanguage.HINDI to "भुगतान प्रमाण जमा करें",
            AppLanguage.MARATHI to "पेमेंट पुरावा सबमिट करा"
        ),
        "pending_approval" to mapOf(
            AppLanguage.ENGLISH to "Pending Admin Verification",
            AppLanguage.GUJARATI to "એડમિન ચકાસણી પેન્ડિંગ છે",
            AppLanguage.HINDI to "एडमिन सत्यापन लंबित है",
            AppLanguage.MARATHI to "अॅडमिन पडताळणी प्रलंबित"
        ),
        "approved_status" to mapOf(
            AppLanguage.ENGLISH to "Approved / Active",
            AppLanguage.GUJARATI to "મંજૂર / સક્રિય",
            AppLanguage.HINDI to "स्वीकृत / सक्रिय",
            AppLanguage.MARATHI to "मंजूर / सक्रिय"
        ),
        "rejected_status" to mapOf(
            AppLanguage.ENGLISH to "Rejected",
            AppLanguage.GUJARATI to "નામંજૂર",
            AppLanguage.HINDI to "अस्वीकृत",
            AppLanguage.MARATHI to "नाकारले"
        ),
        "price_label" to mapOf(
            AppLanguage.ENGLISH to "Price",
            AppLanguage.GUJARATI to "કિંમત",
            AppLanguage.HINDI to "मूल्य",
            AppLanguage.MARATHI to "किंमत"
        ),
        "location_label" to mapOf(
            AppLanguage.ENGLISH to "City / Location",
            AppLanguage.GUJARATI to "શહેર / સ્થળ",
            AppLanguage.HINDI to "शहर / स्थान",
            AppLanguage.MARATHI to "शहर / ठिकाण"
        ),
        "login_button" to mapOf(
            AppLanguage.ENGLISH to "Login",
            AppLanguage.GUJARATI to "લોગિન કરો",
            AppLanguage.HINDI to "लॉगिन करें",
            AppLanguage.MARATHI to "लॉगिन करा"
        ),
        "register_button" to mapOf(
            AppLanguage.ENGLISH to "Register",
            AppLanguage.GUJARATI to "રજિસ્ટર કરો",
            AppLanguage.HINDI to "पंजीकरण करें",
            AppLanguage.MARATHI to "नोंदणी करा"
        ),
        "logout" to mapOf(
            AppLanguage.ENGLISH to "Logout",
            AppLanguage.GUJARATI to "લોગઆઉટ",
            AppLanguage.HINDI to "लॉगआउट",
            AppLanguage.MARATHI to "लॉगआउट"
        )
    )
}
