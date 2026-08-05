package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdEntity
import com.example.ui.components.AiAdBannerPreview
import com.example.ui.components.AiBannerData
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.DeepBlue
import com.example.ui.viewmodel.MainViewModel
import com.example.util.L10n
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AdDetailScreen(
    ad: AdEntity,
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val currentLang by viewModel.currentLanguage.collectAsState()
    val indianCurrencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    val aiBanner = remember(ad.imageUrisJson) { AiBannerData.parse(ad.imageUrisJson) }

    Scaffold(
        bottomBar = {
            // Sticky Contact Bar
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Call Button
                    Button(
                        onClick = {
                            val callIntent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${ad.contactNumber}")
                            }
                            context.startActivity(callIntent)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepBlue)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = L10n.getString("call_vendor", currentLang),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // WhatsApp Button
                    Button(
                        onClick = {
                            val cleanNumber = ad.whatsappNumber.replace(Regex("[^0-9]"), "")
                            val formattedNumber = if (cleanNumber.length == 10) "91$cleanNumber" else cleanNumber
                            val waUri = Uri.parse("https://wa.me/$formattedNumber?text=Hello%20${Uri.encode(ad.vendorName)},%20I%20am%20interested%20in%20your%20ad%20'${Uri.encode(ad.title)}'%20on%20AdBazaar.")
                            val waIntent = Intent(Intent.ACTION_VIEW, waUri)
                            context.startActivity(waIntent)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "WhatsApp",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = L10n.getString("whatsapp_contact", currentLang),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .testTag("ad_detail_screen")
        ) {
            // Top Actions Bar (Back & Share)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.1f), RoundedCornerShape(50))
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DeepBlue)
                }

                IconButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, ad.title)
                            putExtra(Intent.EXTRA_TEXT, "Check out this ad on AdBazaar: ${ad.title} - Price: ${indianCurrencyFormat.format(ad.price)} in ${ad.location}. Contact: ${ad.contactNumber}")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Ad"))
                    },
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.1f), RoundedCornerShape(50))
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = DeepBlue)
                }
            }

            // Header Hero Banner
            if (aiBanner != null) {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    AiAdBannerPreview(bannerData = aiBanner)
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(DeepBlue, DeepBlue.copy(alpha = 0.8f))
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        // Category & Views Badges
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = BrightOrange
                            ) {
                                Text(
                                    text = ad.category,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.Black.copy(alpha = 0.4f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.RemoveRedEye, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${ad.viewsCount} total views",
                                        fontSize = 11.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Main Content Area
            Column(modifier = Modifier.padding(20.dp)) {
                // Price Tag
                Text(
                    text = indianCurrencyFormat.format(ad.price),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = BrightOrange
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Ad Title
                Text(
                    text = ad.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBlue,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Location Chip
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = DeepBlue, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = ad.location,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Description Box
                Text(
                    text = "Description / વિગત",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBlue
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = ad.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Vendor Business Card
                Text(
                    text = "Vendor & Business Information",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBlue
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = DeepBlue.copy(alpha = 0.1f),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Store, contentDescription = null, tint = DeepBlue)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = ad.vendorBusinessName ?: ad.vendorName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepBlue
                                )
                                Text(
                                    text = "Posted by ${ad.vendorName}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        androidx.compose.material3.HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Phone Number", fontSize = 11.sp, color = Color.Gray)
                                Text(text = ad.contactNumber, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }

                            Column {
                                Text(text = "WhatsApp Number", fontSize = 11.sp, color = Color.Gray)
                                Text(text = ad.whatsappNumber, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF25D366))
                            }
                        }
                    }
                }
            }
        }
    }
}
