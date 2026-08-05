package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdEntity
import com.example.data.model.UserRole
import com.example.ui.components.AdCard
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.DeepBlue
import com.example.ui.viewmodel.MainViewModel
import com.example.util.L10n

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onAdClick: (AdEntity) -> Unit,
    onNavigateToMembership: () -> Unit
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val ads by viewModel.approvedAds.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedLocation by viewModel.selectedLocation.collectAsState()

    val locationsList = listOf("All", "Surat, Gujarat", "Ahmedabad, Gujarat", "Rajkot, Gujarat", "Vadodara, Gujarat", "Mumbai, Maharashtra", "Delhi, NCR")
    var showLocationDropdown by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Branding Header Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DeepBlue)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = L10n.getString("app_title", currentLang),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = BrightOrange
                            )
                            Text(
                                text = L10n.getString("app_tagline", currentLang),
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                lineHeight = 16.sp
                            )
                        }

                        Surface(
                            onClick = onNavigateToMembership,
                            shape = RoundedCornerShape(12.dp),
                            color = BrightOrange
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "VENDOR AD",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "₹201 ONCE",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Search Field
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.searchQuery.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_ad_input"),
                        placeholder = {
                            Text(
                                text = L10n.getString("search_hint", currentLang),
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = DeepBlue)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray)
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = BrightOrange,
                            unfocusedBorderColor = Color.White
                        )
                    )
                }
            }
        }

        // Location & Category Filters Bar
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                // Location Selector Dropdown Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { showLocationDropdown = true }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = DeepBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (selectedLocation == null || selectedLocation == "All") L10n.getString("location_all", currentLang) else selectedLocation!!,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepBlue
                        )
                    }

                    DropdownMenu(
                        expanded = showLocationDropdown,
                        onDismissRequest = { showLocationDropdown = false }
                    ) {
                        locationsList.forEach { loc ->
                            DropdownMenuItem(
                                text = { Text(loc) },
                                onClick = {
                                    viewModel.selectedLocation.value = if (loc == "All") null else loc
                                    showLocationDropdown = false
                                }
                            )
                        }
                    }

                    // Reset Filters Button
                    if (selectedCategory != null || selectedLocation != null || searchQuery.isNotEmpty()) {
                        Surface(
                            onClick = {
                                viewModel.searchQuery.value = ""
                                viewModel.selectedCategory.value = null
                                viewModel.selectedLocation.value = null
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.errorContainer
                        ) {
                            Text(
                                text = "Reset Filters",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Category Chips Horizontal Scroll
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        val isAllSelected = selectedCategory == null || selectedCategory == "All"
                        Surface(
                            onClick = { viewModel.selectedCategory.value = null },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isAllSelected) DeepBlue else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = L10n.getString("category_all", currentLang),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isAllSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }

                    items(categories) { cat ->
                        val isSelected = selectedCategory == cat.nameEn
                        val categoryName = when (currentLang.code) {
                            "gu" -> cat.nameGu
                            "hi" -> cat.nameHi
                            else -> cat.nameEn
                        }

                        Surface(
                            onClick = {
                                viewModel.selectedCategory.value = if (isSelected) null else cat.nameEn
                            },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) BrightOrange else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = categoryName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Member Notice Bar (Strict rule reminder)
        if (currentUser?.role != UserRole.VENDOR && currentUser?.role != UserRole.ADMIN) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DeepBlue.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = DeepBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = L10n.getString("vendor_only_notice", currentLang),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = DeepBlue,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Ads Section Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Live Advertisements (${ads.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBlue
                )
            }
        }

        // Empty state or Ad cards
        if (ads.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No advertisements match your current search/filters.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            items(ads) { ad ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    AdCard(
                        ad = ad,
                        currentLang = currentLang,
                        onAdClick = {
                            viewModel.viewAdDetails(ad.id)
                            onAdClick(ad)
                        }
                    )
                }
            }
        }
    }
}
