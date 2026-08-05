package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentStatus
import com.example.data.model.UserRole
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.DeepBlue
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AdminScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsState()
    val pendingPayments by viewModel.pendingPayments.collectAsState()
    val allPayments by viewModel.allPayments.collectAsState()
    val pendingAds by viewModel.pendingAds.collectAsState()
    val totalVendors by viewModel.totalVendorCount.collectAsState()
    val activeVendors by viewModel.activeVendorCount.collectAsState()
    val totalAds by viewModel.totalAdsCount.collectAsState()
    val totalRevenue by viewModel.totalRevenue.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }

    // Admin Access Guard
    if (currentUser?.role != UserRole.ADMIN) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color.Red, modifier = Modifier.size(64.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Admin Access Restricted (/admin)", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Switch to Admin Mode from the top bar to access the control panel.", fontSize = 13.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.switchUserRole(UserRole.ADMIN) },
                colors = ButtonDefaults.buttonColors(containerColor = DeepBlue)
            ) {
                Text("Switch to Admin Mode Demo")
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_portal_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Executive Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DeepBlue)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = BrightOrange, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("AdBazaar Admin Control Center", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = BrightOrange
                        ) {
                            Text("ADMIN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Metrics Grid Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Pending Payments
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = BrightOrange)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Pending ₹201", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("${pendingPayments.size}", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.White)
                            }
                        }

                        // Active Vendors
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Active Vendors", fontSize = 10.sp, color = Color.Gray)
                                Text("$activeVendors / $totalVendors", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        // Revenue
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF065F46))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Total Revenue", fontSize = 10.sp, color = Color.LightGray)
                                Text("₹${(totalRevenue ?: 0.0).toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // Tabs Bar
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 0.dp,
                containerColor = Color.Transparent
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Verify Payments (${pendingPayments.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Approve Ads (${pendingAds.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("All Payments Audit", fontWeight = FontWeight.Bold) }
                )
            }
        }

        // Tab Content 0: Pending Payments Verification
        if (selectedTab == 0) {
            if (pendingPayments.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No pending ₹201 membership payments to verify!", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            } else {
                items(pendingPayments) { payment ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BrightOrange)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = payment.vendorName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
                                    Text(text = "${payment.vendorPhone} • ${payment.vendorEmail}", fontSize = 12.sp, color = Color.Gray)
                                }

                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = BrightOrange
                                ) {
                                    Text(
                                        text = "₹${payment.amount.toInt()}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // UTR Number Highlight Box
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(text = "UTR / UPI TRANSACTION ID", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                                    Text(text = payment.utrNumber, fontSize = 16.sp, fontWeight = FontWeight.Black, color = DeepBlue)
                                    if (!payment.paymentNote.isNullOrBlank()) {
                                        Text(text = "Note: ${payment.paymentNote}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Approval Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.verifyPayment(payment.id, approve = true, adminNote = "Approved in Axis Bank A/C")
                                        Toast.makeText(context, "Vendor Membership Approved & Activated!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Approve Vendor", fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        viewModel.verifyPayment(payment.id, approve = false, adminNote = "UTR Verification Failed")
                                        Toast.makeText(context, "Payment Rejected", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                                ) {
                                    Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Reject", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Tab Content 1: Pending Ads
        if (selectedTab == 1) {
            if (pendingAds.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.PostAdd, contentDescription = null, tint = DeepBlue, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No pending ads awaiting moderation.", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            } else {
                items(pendingAds) { ad ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = ad.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
                            Text(text = "Vendor: ${ad.vendorName} • Price: ₹${ad.price.toInt()} • City: ${ad.location}", fontSize = 12.sp, color = BrightOrange)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = ad.description, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { viewModel.approveAd(ad.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Approve Ad")
                                }
                                Button(
                                    onClick = { viewModel.rejectAd(ad.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Reject")
                                }
                            }
                        }
                    }
                }
            }
        }

        // Tab Content 2: All Payments Audit
        if (selectedTab == 2) {
            items(allPayments) { pay ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = pay.vendorName, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text(text = "UTR: ${pay.utrNumber}", fontSize = 12.sp, color = DeepBlue, fontWeight = FontWeight.SemiBold)
                            Text(text = "Amount: ₹${pay.amount.toInt()}", fontSize = 11.sp, color = Color.Gray)
                        }

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = when (pay.status) {
                                PaymentStatus.APPROVED -> Color(0xFF10B981)
                                PaymentStatus.PENDING -> BrightOrange
                                PaymentStatus.REJECTED -> Color(0xFFEF4444)
                            }
                        ) {
                            Text(
                                text = pay.status.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
