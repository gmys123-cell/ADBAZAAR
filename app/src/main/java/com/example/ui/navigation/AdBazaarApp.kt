package com.example.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.data.model.AdEntity
import com.example.data.model.UserRole
import com.example.ui.components.LanguageSelectorDialog
import com.example.ui.components.TopNavBar
import com.example.ui.screens.AdDetailScreen
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MembershipPaymentScreen
import com.example.ui.screens.MyAdsScreen
import com.example.ui.screens.PostAdScreen
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.DeepBlue
import com.example.ui.viewmodel.MainViewModel
import com.example.util.L10n

import androidx.compose.material.icons.filled.AutoAwesome
import com.example.ui.screens.AiToolsScreen

@Composable
fun AdBazaarApp(
    viewModel: MainViewModel
) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val pendingPaymentsCount by viewModel.pendingPaymentsCount.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Home, 1: Post Ad, 2: My Ads, 3: Membership, 4: Admin, 5: Auth, 6: AI Studio
    var selectedAdDetail by remember { mutableStateOf<AdEntity?>(null) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    // State for pre-filled data passed from AI Studio to Post Ad screen
    var aiPreFilledTitle by remember { mutableStateOf("") }
    var aiPreFilledDescription by remember { mutableStateOf("") }
    var aiPreFilledBannerJson by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopNavBar(
                currentUser = currentUser,
                currentLang = currentLang,
                onLanguageClick = { showLanguageDialog = true },
                onRoleSwitch = { role ->
                    viewModel.switchUserRole(role)
                },
                onLogoutClick = {
                    viewModel.logout()
                    selectedTab = 0
                },
                onOpenAuth = {
                    selectedTab = 5
                }
            )
        },
        bottomBar = {
            if (selectedAdDetail == null && selectedTab != 5) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = DeepBlue
                ) {
                    // Tab 0: Home Explore
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Explore, contentDescription = "Explore") },
                        label = {
                            Text(
                                text = L10n.getString("home_tab", currentLang),
                                fontSize = 11.sp,
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrightOrange,
                            selectedTextColor = BrightOrange,
                            indicatorColor = DeepBlue.copy(alpha = 0.1f)
                        ),
                        modifier = Modifier.testTag("nav_explore")
                    )

                    // Tab 1: Post AD (Vendor only feature)
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.AddCircle, contentDescription = "Post AD") },
                        label = {
                            Text(
                                text = L10n.getString("post_ad_tab", currentLang),
                                fontSize = 11.sp,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrightOrange,
                            selectedTextColor = BrightOrange,
                            indicatorColor = DeepBlue.copy(alpha = 0.1f)
                        ),
                        modifier = Modifier.testTag("nav_post_ad")
                    )

                    // Tab 6: Gemini AI Studio (Vendor special tool)
                    NavigationBarItem(
                        selected = selectedTab == 6,
                        onClick = { selectedTab = 6 },
                        icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI Studio") },
                        label = {
                            Text(
                                text = "AI Studio",
                                fontSize = 11.sp,
                                fontWeight = if (selectedTab == 6) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrightOrange,
                            selectedTextColor = BrightOrange,
                            indicatorColor = DeepBlue.copy(alpha = 0.1f)
                        ),
                        modifier = Modifier.testTag("nav_ai_studio")
                    )

                    // Tab 2: My Ads (Vendor only)
                    if (currentUser?.role == UserRole.VENDOR || currentUser?.role == UserRole.ADMIN) {
                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            icon = { Icon(Icons.Default.Store, contentDescription = "My Ads") },
                            label = {
                                Text(
                                    text = L10n.getString("my_ads_tab", currentLang),
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BrightOrange,
                                selectedTextColor = BrightOrange,
                                indicatorColor = DeepBlue.copy(alpha = 0.1f)
                            ),
                            modifier = Modifier.testTag("nav_my_ads")
                        )
                    }

                    // Tab 3: Membership ₹201
                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = { Icon(Icons.Default.Payment, contentDescription = "Membership") },
                        label = {
                            Text(
                                text = L10n.getString("membership_tab", currentLang),
                                fontSize = 11.sp,
                                fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrightOrange,
                            selectedTextColor = BrightOrange,
                            indicatorColor = DeepBlue.copy(alpha = 0.1f)
                        ),
                        modifier = Modifier.testTag("nav_membership")
                    )

                    // Tab 4: Admin Portal
                    if (currentUser?.role == UserRole.ADMIN) {
                        NavigationBarItem(
                            selected = selectedTab == 4,
                            onClick = { selectedTab = 4 },
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (pendingPaymentsCount > 0) {
                                            Badge(containerColor = BrightOrange) {
                                                Text("$pendingPaymentsCount")
                                            }
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin")
                                }
                            },
                            label = {
                                Text(
                                    text = L10n.getString("admin_panel", currentLang),
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedTab == 4) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Red,
                                selectedTextColor = Color.Red,
                                indicatorColor = DeepBlue.copy(alpha = 0.1f)
                            ),
                            modifier = Modifier.testTag("nav_admin")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (selectedAdDetail != null) {
                AdDetailScreen(
                    ad = selectedAdDetail!!,
                    viewModel = viewModel,
                    onBackClick = { selectedAdDetail = null }
                )
            } else {
                when (selectedTab) {
                    0 -> HomeScreen(
                        viewModel = viewModel,
                        onAdClick = { ad -> selectedAdDetail = ad },
                        onNavigateToMembership = { selectedTab = 3 }
                    )
                    1 -> PostAdScreen(
                        viewModel = viewModel,
                        initialTitle = aiPreFilledTitle,
                        initialDescription = aiPreFilledDescription,
                        initialBannerJson = aiPreFilledBannerJson,
                        onNavigateToMembership = { selectedTab = 3 },
                        onNavigateToAuth = { selectedTab = 5 },
                        onNavigateToAiStudio = { selectedTab = 6 },
                        onAdPostedSuccess = {
                            // Reset prefilled AI data
                            aiPreFilledTitle = ""
                            aiPreFilledDescription = ""
                            aiPreFilledBannerJson = ""
                            selectedTab = 0
                        }
                    )
                    2 -> MyAdsScreen(
                        viewModel = viewModel,
                        onNavigateToPostAd = { selectedTab = 1 },
                        onAdClick = { ad -> selectedAdDetail = ad }
                    )
                    3 -> MembershipPaymentScreen(
                        viewModel = viewModel,
                        onPaymentSubmitted = { selectedTab = 3 }
                    )
                    4 -> AdminScreen(
                        viewModel = viewModel
                    )
                    5 -> AuthScreen(
                        viewModel = viewModel,
                        onAuthSuccess = { selectedTab = 0 }
                    )
                    6 -> AiToolsScreen(
                        viewModel = viewModel,
                        onNavigateToPostAdWithAi = { title, desc, bannerJson ->
                            aiPreFilledTitle = title
                            aiPreFilledDescription = desc
                            aiPreFilledBannerJson = bannerJson
                            selectedTab = 1
                        },
                        onNavigateToMembership = { selectedTab = 3 }
                    )
                }
            }
        }

        // Multi-Language Selector Modal
        if (showLanguageDialog) {
            LanguageSelectorDialog(
                currentLanguage = currentLang,
                onSelectLanguage = { lang ->
                    viewModel.switchLanguage(lang)
                },
                onDismiss = { showLanguageDialog = false }
            )
        }
    }
}
