package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.GeminiApiClient
import com.example.ui.components.AiAdBannerPreview
import com.example.ui.components.AiBannerData
import com.example.ui.components.BannerTheme
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.DeepBlue
import com.example.ui.viewmodel.MainViewModel
import com.example.util.L10n
import kotlinx.coroutines.launch

@Composable
fun AiToolsScreen(
    viewModel: MainViewModel,
    onNavigateToPostAdWithAi: (title: String, description: String, bannerJson: String) -> Unit,
    onNavigateToMembership: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: Banner Studio, 1: Copywriter Assistant

    // Banner Studio States
    var selectedTheme by remember { mutableStateOf(BannerTheme.ORANGE_FLASH) }
    var bannerBadge by remember { mutableStateOf("FESTIVAL SPECIAL 50% OFF") }
    var bannerTitle by remember { mutableStateOf("Surat Pure Cotton Sarees") }
    var bannerTagline by remember { mutableStateOf("Direct Factory Wholesale Rate • All India Delivery") }
    var bannerPrice by remember { mutableStateOf("₹499 Onwards") }
    var bannerContact by remember { mutableStateOf("Call / WhatsApp Now") }

    // Gemini Copywriter States
    var productName by remember { mutableStateOf("") }
    var categoryName by remember { mutableStateOf("Textiles & Clothing") }
    var offerDetails by remember { mutableStateOf("Flat 40% Discount on Bulk Orders") }
    var targetLanguage by remember { mutableStateOf("Gujarati") }

    var isGeneratingCopy by remember { mutableStateOf(false) }
    var generatedCopyResult by remember { mutableStateOf("") }

    val currentBannerData = remember(selectedTheme, bannerBadge, bannerTitle, bannerTagline, bannerPrice, bannerContact) {
        AiBannerData(
            theme = selectedTheme,
            badgeText = bannerBadge,
            titleText = bannerTitle,
            taglineText = bannerTagline,
            priceText = bannerPrice,
            contactText = bannerContact
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("ai_tools_screen")
    ) {
        // Top Hero Banner for AI Studio
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DeepBlue)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = BrightOrange,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Gemini AI Ad Studio (જેમિનાઈ AI)",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Create professional advertisement banners and high-converting marketing content in Gujarati, Hindi & English instantly!",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tab Navigation
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = DeepBlue
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Brush, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("1. AI Banner Studio", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("2. AI Copywriter", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TAB 1: AI Banner Creator Studio
        if (selectedTabIndex == 0) {
            Text(
                text = "Live AI Banner Preview (બેનર પ્રિવ્યૂ):",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBlue
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Interactive Banner Canvas
            AiAdBannerPreview(bannerData = currentBannerData)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Customize Banner Design (ડિઝાઇન બદલો):",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBlue
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Color Themes Picker
            Text("Select Theme Color:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BannerTheme.values().forEach { theme ->
                    val isSelected = theme == selectedTheme
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(theme.gradient.first())
                            .border(
                                width = if (isSelected) 3.dp else 0.dp,
                                color = if (isSelected) BrightOrange else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedTheme = theme },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = theme.textColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Text Customizations
            OutlinedTextField(
                value = bannerBadge,
                onValueChange = { bannerBadge = it },
                label = { Text("Top Badge Text (e.g. 50% OFF, WHOLESALE SALE)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = bannerTitle,
                onValueChange = { bannerTitle = it },
                label = { Text("Main Product Headline") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = bannerTagline,
                onValueChange = { bannerTagline = it },
                label = { Text("Subtitle / Highlight Tagline") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = bannerPrice,
                    onValueChange = { bannerPrice = it },
                    label = { Text("Price Display") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = bannerContact,
                    onValueChange = { bannerContact = it },
                    label = { Text("Contact Label") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Button: Apply Banner to Post Ad
            Button(
                onClick = {
                    val serializedBanner = currentBannerData.toSerializedString()
                    onNavigateToPostAdWithAi(
                        bannerTitle,
                        "$bannerTitle - $bannerTagline ($bannerBadge)",
                        serializedBanner
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("use_ai_banner_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrightOrange)
            ) {
                Icon(Icons.Default.Publish, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Attach AI Banner & Create Ad Post",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        // TAB 2: Gemini AI Marketing Copywriter
        if (selectedTabIndex == 1) {
            Text(
                text = "Generate High-Converting Marketing Copy:",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBlue
            )
            Text(
                text = "Gemini AI will write compelling ad descriptions in your language.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Product / Business Name (પ્રોડક્ટ નામ)") },
                placeholder = { Text("e.g. Designer Saree or Mobile Repairing") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Category (કેટેગરી)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = offerDetails,
                onValueChange = { offerDetails = it },
                label = { Text("Offer / Discount Details (ઓફર વિગત)") },
                placeholder = { Text("e.g. Buy 2 Get 1 Free, Free Shipping") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Generate Button
            Button(
                onClick = {
                    if (productName.isBlank()) {
                        Toast.makeText(context, "કૃપા કરીને પ્રોડક્ટ નામ દાખલ કરો! (Enter product name)", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    scope.launch {
                        isGeneratingCopy = true
                        val copy = GeminiApiClient.generateAdCopy(
                            productName = productName,
                            category = categoryName,
                            offerDetails = offerDetails,
                            targetLanguage = currentLang.displayName
                        )
                        generatedCopyResult = copy
                        isGeneratingCopy = false
                    }
                },
                enabled = !isGeneratingCopy,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("generate_ai_copy_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepBlue)
            ) {
                if (isGeneratingCopy) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Gemini AI Processing...", fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = BrightOrange)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate AI Content with Gemini", fontWeight = FontWeight.Bold)
                }
            }

            // Output Display Card
            if (generatedCopyResult.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F9)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DeepBlue.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.FormatQuote, contentDescription = null, tint = BrightOrange)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Generated AI Marketing Copy", fontWeight = FontWeight.Bold, color = DeepBlue)
                            }

                            OutlinedButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(generatedCopyResult))
                                    Toast.makeText(context, "કોપી થઈ ગયું! (Copied to Clipboard)", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copy", fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = generatedCopyResult,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                onNavigateToPostAdWithAi(
                                    productName,
                                    generatedCopyResult,
                                    currentBannerData.toSerializedString()
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrightOrange)
                        ) {
                            Text("Use This Description to Post Ad", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
