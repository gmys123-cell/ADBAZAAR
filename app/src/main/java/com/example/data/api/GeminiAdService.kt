package com.example.data.api

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiInlineData(
    val mimeType: String? = null,
    val data: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    val text: String? = null,
    val inlineData: GeminiInlineData? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiImageConfig(
    val aspectRatio: String = "1:1",
    val imageSize: String = "1K"
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    val responseModalities: List<String>? = null,
    val imageConfig: GeminiImageConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GeminiGenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    val content: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<GeminiCandidate>? = null
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse

    @POST("v1beta/models/gemini-2.5-flash-image:generateContent")
    suspend fun generateImageContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiApiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)
    }

    suspend fun generateAdCopy(
        productName: String,
        category: String,
        offerDetails: String,
        targetLanguage: String = "Gujarati"
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        val prompt = """
            You are an expert Indian local business marketing copywriter for Gujarati and Hindi markets.
            Generate a super high-converting advertisement for:
            - Product / Business Name: $productName
            - Category: $category
            - Special Offer/Discount/Details: $offerDetails
            - Target Language: $targetLanguage

            Provide the response formatted nicely with:
            1. Catchy Ad Title / Slogan (Gujarati & English)
            2. Detailed Product Description (incorporating key features, wholesale prices, quality guarantee)
            3. Call-To-Action for WhatsApp & Phone Call
            4. 3-5 Relevant Hashtags (#SuratTextiles #WholesaleOffer etc.)
        """.trimIndent()

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Intelligent Fallback Copywriter for demo when API key is unconfigured
            return@withContext """
                🔥 **$productName - ધમાકા ઓફર (Special Deal)** 🔥
                
                ✨ **કેટેગરી:** $category
                🎁 **ઓફર વિગત:** $offerDetails
                
                🛍️ **પ્રોડક્ટ વિશેષતાઓ:**
                - 100% પ્રીમિયમ ક્વોલિટીની ગેરંટી
                - હોલસેલ અને રિટેલ બંને ભાવે ઉપલબ્ધ
                - સુરત અને સમગ્ર ભારતમાં ઝડપી ડિલિવરી
                
                📞 **સંપર્ક:** ઓર્ડર અને વધુ વિગત માટે અત્યારે જ કોલ અથવા વોટ્સએપ મેસેજ કરો!
                
                #AdBazaar #LocalBusiness #${category.replace(" ", "")} #SuratDeals
            """.trimIndent()
        }

        try {
            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = prompt))
                    )
                )
            )
            val response = service.generateContent(apiKey, request)
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            text ?: "ઓટોમેટેડ AI એડ લખાણ તૈયાર થઈ ગયું છે."
        } catch (e: Exception) {
            """
                🔥 **$productName - ધમાકા ઓફર (Special Deal)** 🔥
                
                ✨ **કેટેગરી:** $category
                🎁 **ઓફર વિગત:** $offerDetails
                
                🛍️ **પ્રોડક્ટ વિશેષતાઓ:**
                - 100% પ્રીમિયમ ક્વોલિટીની ગેરંટી
                - હોલસેલ અને રિટેલ બંને ભાવે ઉપલબ્ધ
                
                📞 ઓર્ડર માટે અત્યારે જ સંપર્ક કરો!
            """.trimIndent()
        }
    }

    suspend fun generatePromotionalAdImage(
        promptText: String,
        adTitle: String = "",
        category: String = ""
    ): String? = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        val formattedPrompt = """
            Create a vibrant, professional Indian market advertisement promotional poster graphic.
            Product/Title: $adTitle
            Category: $category
            Prompt Description: $promptText
            Style: High resolution, professional product showcase banner, bright colors, attractive festival deal framing.
        """.trimIndent()

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext null
        }

        try {
            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = formattedPrompt))
                    )
                ),
                generationConfig = GeminiGenerationConfig(
                    responseModalities = listOf("TEXT", "IMAGE"),
                    imageConfig = GeminiImageConfig(aspectRatio = "1:1", imageSize = "1K")
                )
            )

            val response = service.generateImageContent(apiKey, request)
            val inlineData = response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull { it.inlineData != null }
                ?.inlineData

            if (inlineData?.data != null) {
                val mime = inlineData.mimeType ?: "image/jpeg"
                "data:$mime;base64,${inlineData.data}"
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

