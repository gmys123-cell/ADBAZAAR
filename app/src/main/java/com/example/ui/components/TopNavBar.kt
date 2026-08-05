package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Badge
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.ChakraBlue
import com.example.ui.theme.DeepBlue
import com.example.ui.theme.IndiaGreen
import com.example.ui.theme.IndiaSaffron
import com.example.util.AppLanguage
import com.example.util.L10n

@Composable
fun TopNavBar(
    currentUser: UserEntity?,
    currentLang: AppLanguage,
    onLanguageClick: () -> Unit,
    onRoleSwitch: (UserRole) -> Unit,
    onLogoutClick: () -> Unit,
    onOpenAuth: () -> Unit
) {
    var showRoleMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("top_nav_bar"),
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Indian Flag Tiranga Top Strip (Kesari, White, Green)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(IndiaSaffron)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.White)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(IndiaGreen)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            // AdBazaar Brand Logo
            AdBazaarLogo(
                iconSize = 34.dp,
                textSizeSp = 20,
                showTagline = true
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Language Switcher Button
                Surface(
                    onClick = onLanguageClick,
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = DeepBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentLang.nativeName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepBlue
                        )
                    }
                }

                // Current Role & Demo Switcher Menu
                Box {
                    val roleBadgeColor = when (currentUser?.role) {
                        UserRole.ADMIN -> Color(0xFFDC2626)
                        UserRole.VENDOR -> BrightOrange
                        else -> DeepBlue
                    }

                    val roleTitle = when (currentUser?.role) {
                        UserRole.ADMIN -> L10n.getString("role_admin", currentLang)
                        UserRole.VENDOR -> L10n.getString("role_vendor", currentLang)
                        UserRole.MEMBER -> L10n.getString("role_member", currentLang)
                        null -> "Guest"
                    }

                    Surface(
                        onClick = {
                            if (currentUser != null) showRoleMenu = true else onOpenAuth()
                        },
                        shape = RoundedCornerShape(20.dp),
                        color = roleBadgeColor.copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, roleBadgeColor.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (currentUser?.role) {
                                    UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                                    UserRole.VENDOR -> Icons.Default.Store
                                    else -> Icons.Default.Person
                                },
                                contentDescription = null,
                                tint = roleBadgeColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = roleTitle,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = roleBadgeColor
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Switch Role Demo",
                                tint = roleBadgeColor,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showRoleMenu,
                        onDismissRequest = { showRoleMenu = false }
                    ) {
                        Text(
                            text = "DEMO ROLE SWITCHER",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                        DropdownMenuItem(
                            text = { Text("Member Mode (સામાન્ય સભ્ય)") },
                            onClick = {
                                onRoleSwitch(UserRole.MEMBER)
                                showRoleMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = DeepBlue)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Vendor Mode (₹201 Active Vendor)") },
                            onClick = {
                                onRoleSwitch(UserRole.VENDOR)
                                showRoleMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Store, contentDescription = null, tint = BrightOrange)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Admin Mode (/admin Portal)") },
                            onClick = {
                                onRoleSwitch(UserRole.ADMIN)
                                showRoleMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color(0xFFDC2626))
                            }
                        )
                        androidx.compose.material3.HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Log Out (${currentUser?.name ?: ""})", color = Color.Red) },
                            onClick = {
                                onLogoutClick()
                                showRoleMenu = false
                            }
                        )
                    }
                }
            }
        }
    }
}
}
