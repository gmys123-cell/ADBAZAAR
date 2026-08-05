package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.DeepBlue

enum class BannerTheme(
    val id: String,
    val nameGu: String,
    val nameEn: String,
    val gradient: List<Color>,
    val textColor: Color,
    val accentColor: Color
) {
    FESTIVE_RED(
        "red",
        "તહેવાર રેડ (Festival Red)",
        "Festival Red",
        listOf(Color(0xFFB71C1C), Color(0xFFE53935), Color(0xFF880E4F)),
        Color.White,
        Color(0xFFFFD54F)
    ),
    ORANGE_FLASH(
        "orange",
        "ધમાકા ઓરેન્જ (Flash Orange)",
        "Flash Orange",
        listOf(Color(0xFFFF6D00), Color(0xFFFF9100), Color(0xFFDD2C00)),
        Color.White,
        Color(0xFFFFF59D)
    ),
    ROYAL_BLUE(
        "blue",
        "રોયલ બ્લુ (Royal Blue)",
        "Royal Blue",
        listOf(Color(0xFF0D47A1), Color(0xFF1565C0), Color(0xFF002171)),
        Color.White,
        Color(0xFF40C4FF)
    ),
    DARK_LUXURY(
        "dark",
        "ગોલ્ડન બ્લેક (Golden Luxury)",
        "Golden Luxury",
        listOf(Color(0xFF1A1A1A), Color(0xFF2D2D2D), Color(0xFF000000)),
        Color(0xFFFFD700),
        Color(0xFFFFAB00)
    ),
    EMERALD_GREEN(
        "green",
        "એમરાલ્ડ ગ્રીન (Fresh Green)",
        "Fresh Green",
        listOf(Color(0xFF1B5E20), Color(0xFF2E7D32), Color(0xFF003300)),
        Color.White,
        Color(0xFFA7FFEB)
    )
}

data class AiBannerData(
    val theme: BannerTheme = BannerTheme.ORANGE_FLASH,
    val badgeText: String = "SPECIAL DISCOUNT",
    val titleText: String = "Premium Wholesale Collection",
    val taglineText: String = "Direct Factory Price • Limited Stock",
    val priceText: String = "₹999 Only",
    val contactText: String = "Call / WhatsApp Now"
) {
    fun toSerializedString(): String {
        return "ai_banner:${theme.id}:$badgeText:$titleText:$taglineText:$priceText:$contactText"
    }

    companion object {
        fun parse(serialized: String): AiBannerData? {
            if (!serialized.startsWith("ai_banner:")) return null
            val parts = serialized.split(":")
            if (parts.size < 7) return null
            val theme = BannerTheme.values().find { it.id == parts[1] } ?: BannerTheme.ORANGE_FLASH
            return AiBannerData(
                theme = theme,
                badgeText = parts[2],
                titleText = parts[3],
                taglineText = parts[4],
                priceText = parts[5],
                contactText = parts[6]
            )
        }
    }
}

@Composable
fun AiAdBannerPreview(
    bannerData: AiBannerData,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(bannerData.theme.gradient))
                .border(2.dp, bannerData.theme.accentColor.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Badge Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = bannerData.theme.accentColor
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = bannerData.badgeText.ifBlank { "SPECIAL OFFER" },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = bannerData.theme.accentColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "AI Verified Ad",
                                fontSize = 9.sp,
                                color = bannerData.theme.textColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Product Title
                Text(
                    text = bannerData.titleText.ifBlank { "Product Title" },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = bannerData.theme.textColor,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Tagline / Subtitle
                Text(
                    text = bannerData.taglineText.ifBlank { "Direct Wholesale Deal" },
                    fontSize = 12.sp,
                    color = bannerData.theme.textColor.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Price Tag & CTA Footer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.35f))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "OFFER PRICE",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = bannerData.theme.accentColor
                        )
                        Text(
                            text = bannerData.priceText.ifBlank { "Best Deal" },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(bannerData.theme.accentColor)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = bannerData.contactText.ifBlank { "Contact Now" },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
