package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.DeepBlue
import com.example.ui.viewmodel.MainViewModel
import com.example.util.L10n

import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import com.example.ui.components.AiAdBannerPreview
import com.example.ui.components.AiBannerData

@Composable
fun PostAdScreen(
    viewModel: MainViewModel,
    initialTitle: String = "",
    initialDescription: String = "",
    initialBannerJson: String = "",
    onNavigateToMembership: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToAiStudio: () -> Unit = {},
    onAdPostedSuccess: () -> Unit
) {
    val context = LocalContext.current
    val currentLang by viewModel.currentLanguage.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val categories by viewModel.categories.collectAsState()

    // Form Fields State
    var title by remember(initialTitle) { mutableStateOf(initialTitle) }
    var selectedCategoryName by remember { mutableStateOf("") }
    var priceStr by remember { mutableStateOf("") }
    var location by remember { mutableStateOf(currentUser?.city ?: "Surat, Gujarat") }
    var contactNumber by remember { mutableStateOf(currentUser?.phone ?: "") }
    var whatsappNumber by remember { mutableStateOf(currentUser?.phone ?: "") }
    var description by remember(initialDescription) { mutableStateOf(initialDescription) }
    var attachedBannerJson by remember(initialBannerJson) { mutableStateOf(initialBannerJson) }

    var categoryExpanded by remember { mutableStateOf(false) }

    val isVendor = currentUser?.role == UserRole.VENDOR || currentUser?.role == UserRole.ADMIN
    val isMembershipActive = currentUser?.isMember == true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("post_ad_screen")
    ) {
        // CASE 1: User is MEMBER (Normal user cannot post ads!)
        if (!isVendor) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = DeepBlue,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "ફક્ત Vendor જ એડ મૂકી શકે છે\n(Only Vendors Can Post Ads)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepBlue,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = L10n.getString("vendor_only_notice", currentLang),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onNavigateToAuth,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrightOrange)
                    ) {
                        Icon(Icons.Default.Store, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Register / Switch to Vendor Account", fontWeight = FontWeight.Bold)
                    }
                }
            }
            return
        }

        // CASE 2: User is VENDOR but Membership ₹201 is PENDING / NOT PAID
        if (!isMembershipActive) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
                border = androidx.compose.foundation.BorderStroke(1.dp, BrightOrange.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = null,
                        tint = BrightOrange,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = L10n.getString("pay_membership_title", currentLang),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepBlue,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = L10n.getString("pay_membership_desc", currentLang),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onNavigateToMembership,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DeepBlue)
                    ) {
                        Text("Pay ₹201 Membership / Verify UTR", fontWeight = FontWeight.Bold)
                    }
                }
            }
            return
        }

        // CASE 3: Active Vendor - Full Ad Creation Form!
        Text(
            text = "Post New Advertisement (નવી એડ મૂકો)",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBlue
        )
        Text(
            text = "Fill details to post your business product across India.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // AI Studio Banner Shortcut Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
            border = androidx.compose.foundation.BorderStroke(1.dp, DeepBlue.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = BrightOrange, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Gemini AI Studio", fontWeight = FontWeight.Bold, color = DeepBlue, fontSize = 14.sp)
                    }
                    Text(
                        text = "Design AI Banners & generate Gujarati ad descriptions automatically!",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                }

                Button(
                    onClick = onNavigateToAiStudio,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepBlue)
                ) {
                    Icon(Icons.Default.Brush, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Open AI Studio", fontSize = 11.sp)
                }
            }
        }

        // Attached AI Banner Preview (if present)
        val aiBannerParsed = remember(attachedBannerJson) { AiBannerData.parse(attachedBannerJson) }
        if (aiBannerParsed != null) {
            Text(
                text = "Attached AI Banner (જોડાયેલ એડ બેનર):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBlue
            )
            Spacer(modifier = Modifier.height(6.dp))
            AiAdBannerPreview(bannerData = aiBannerParsed)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Ad Title Field
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Ad Title (એડનું શીર્ષક)") },
            placeholder = { Text("e.g. Pure Cotton Saree Wholesale or 5G Phone") },
            leadingIcon = { Icon(Icons.Default.Title, contentDescription = null, tint = DeepBlue) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("post_ad_title_input"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Selection
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedCategoryName.ifEmpty { "Select Category" },
                onValueChange = {},
                readOnly = true,
                label = { Text("Category (કેટેગરી)") },
                leadingIcon = { Icon(Icons.Default.Category, contentDescription = null, tint = DeepBlue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("post_ad_category_dropdown"),
                shape = RoundedCornerShape(12.dp)
            )

            Surface(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Transparent),
                color = Color.Transparent,
                onClick = { categoryExpanded = true }
            ) {}

            DropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                categories.forEach { cat ->
                    val displayName = when (currentLang.code) {
                        "gu" -> cat.nameGu
                        "hi" -> cat.nameHi
                        else -> cat.nameEn
                    }
                    DropdownMenuItem(
                        text = { Text(displayName) },
                        onClick = {
                            selectedCategoryName = cat.nameEn
                            categoryExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Price Field
        OutlinedTextField(
            value = priceStr,
            onValueChange = { priceStr = it },
            label = { Text("Price in ₹ (કિંમત)") },
            placeholder = { Text("1500") },
            leadingIcon = { Icon(Icons.Default.CurrencyRupee, contentDescription = null, tint = DeepBlue) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("post_ad_price_input"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // City / Location Field
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("City & State (સ્થળ)") },
            placeholder = { Text("Surat, Gujarat") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = DeepBlue) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("post_ad_location_input"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Contact Phone Number
        OutlinedTextField(
            value = contactNumber,
            onValueChange = { contactNumber = it },
            label = { Text("Call Contact Number (મોબાઇલ નંબર)") },
            leadingIcon = { Icon(Icons.Default.Call, contentDescription = null, tint = DeepBlue) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("post_ad_phone_input"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // WhatsApp Number
        OutlinedTextField(
            value = whatsappNumber,
            onValueChange = { whatsappNumber = it },
            label = { Text("WhatsApp Number") },
            leadingIcon = { Icon(Icons.Default.Chat, contentDescription = null, tint = Color(0xFF25D366)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("post_ad_whatsapp_input"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description Field
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Detailed Description (સંપૂર્ણ વિગત)") },
            placeholder = { Text("Describe product features, material quality, wholesale discount details...") },
            leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = DeepBlue) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .testTag("post_ad_description_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = {
                val priceVal = priceStr.toDoubleOrNull() ?: 0.0
                val categoryToPost = selectedCategoryName.ifEmpty { categories.firstOrNull()?.nameEn ?: "General" }

                viewModel.postNewAd(
                    title = title,
                    description = description,
                    price = priceVal,
                    category = categoryToPost,
                    location = location,
                    contactNumber = contactNumber,
                    whatsappNumber = whatsappNumber,
                    imageUrisJson = attachedBannerJson,
                    onSuccess = {
                        Toast.makeText(context, "એડ સફળતાપૂર્વક મૂકાઈ ગઈ છે! (Ad Posted Successfully)", Toast.LENGTH_LONG).show()
                        onAdPostedSuccess()
                    },
                    onError = { err ->
                        Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("submit_ad_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrightOrange)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Publish Advertisement Now",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
