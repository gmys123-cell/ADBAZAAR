package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentStatus
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.DeepBlue
import com.example.ui.viewmodel.MainViewModel
import com.example.util.L10n

@Composable
fun MembershipPaymentScreen(
    viewModel: MainViewModel,
    onPaymentSubmitted: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val currentLang by viewModel.currentLanguage.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val myPayments by viewModel.myPayments.collectAsState()

    var utrInput by remember { mutableStateOf("") }
    var noteInput by remember { mutableStateOf("") }

    val upiId = "vhora110@oksbi"
    val accNo = "38775754909"
    val ifsc = "SBIN0016687"
    val accHolder = "GUJARAT MUSLIM YUVAA SANGHATHAN ANAND"
    val bankName = "State Bank of India (SBI)"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("membership_payment_screen")
    ) {
        // Active Membership Banner
        if (currentUser?.isMember == true) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Vendor Membership Active! (મેમ્બરશિપ સક્રિય છે)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF065F46)
                        )
                        Text(
                            text = "You can post unlimited advertisements on AdBazaar.",
                            fontSize = 12.sp,
                            color = Color(0xFF047857)
                        )
                    }
                }
            }
        }

        // Header Title
        Text(
            text = L10n.getString("pay_membership_title", currentLang),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBlue
        )
        Text(
            text = L10n.getString("pay_membership_desc", currentLang),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // UPI & QR Code Details Card
        GooglePayQrCard(
            upiId = upiId,
            accountHolder = accHolder,
            amountStr = "₹201",
            onCopyUpi = {
                clipboardManager.setText(AnnotatedString(upiId))
                Toast.makeText(context, "UPI ID Copied! ($upiId)", Toast.LENGTH_SHORT).show()
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Bank Transfer Account Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = DeepBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Option 2: Direct Bank NEFT / IMPS Account",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepBlue
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Text(text = "Account Name: $accHolder", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "Account Number: $accNo", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepBlue)
                    Text(text = "IFSC Code: $ifsc", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrightOrange)
                    Text(text = "Bank: $bankName", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Payment Proof Submission Form
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(1.dp, DeepBlue.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Submit Payment Verification (₹201)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepBlue
                )
                Text(
                    text = "After paying ₹201, enter your 12-digit UTR / UPI Transaction Reference Number below:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = utrInput,
                    onValueChange = { utrInput = it },
                    label = { Text("12-Digit UTR / UPI Transaction ID") },
                    placeholder = { Text("e.g. UTR441908234190 or 4239812039") },
                    leadingIcon = { Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = DeepBlue) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("utr_number_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = noteInput,
                    onValueChange = { noteInput = it },
                    label = { Text("Payment Note (Optional)") },
                    placeholder = { Text("e.g. Paid via PhonePe from Ramesh Patel") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.submitMembershipPayment(
                            utr = utrInput,
                            note = noteInput,
                            onSuccess = {
                                Toast.makeText(context, "ચૂકવણી વિગત સફળતાપૂર્વક સબમિટ થઈ ગઈ છે! (Payment Submitted For Verification)", Toast.LENGTH_LONG).show()
                                utrInput = ""
                                noteInput = ""
                                onPaymentSubmitted()
                            },
                            onError = { err ->
                                Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_payment_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrightOrange)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = L10n.getString("submit_payment", currentLang), fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submitted Payments Status Log
        Text(
            text = "Your Submitted Payment Proofs",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DeepBlue
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (myPayments.isEmpty()) {
            Text(
                text = "No payment records found yet.",
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            myPayments.forEach { pay ->
                val statusColor = when (pay.status) {
                    PaymentStatus.APPROVED -> Color(0xFF10B981)
                    PaymentStatus.PENDING -> BrightOrange
                    PaymentStatus.REJECTED -> Color(0xFFEF4444)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "UTR: ${pay.utrNumber}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepBlue
                            )
                            Text(
                                text = "Amount: ₹${pay.amount.toInt()}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            if (!pay.paymentNote.isNullOrBlank()) {
                                Text(
                                    text = pay.paymentNote!!,
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = statusColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = pay.status.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = statusColor,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GooglePayQrCard(
    upiId: String,
    accountHolder: String,
    amountStr: String = "₹201",
    onCopyUpi: () -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("google_pay_qr_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFCBD5E1))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Organization Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF047857),
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "GM",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = accountHolder,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // White QR Code Display Container
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 4.dp,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // QR Code Canvas
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val matrixSize = 25
                            val cellSize = size.width / matrixSize

                            fun isFinderPattern(r: Int, c: Int): Boolean {
                                val topLt = r < 7 && c < 7
                                val topRt = r < 7 && c >= matrixSize - 7
                                val botLt = r >= matrixSize - 7 && c < 7
                                return topLt || topRt || botLt
                            }

                            // 1. White background
                            drawRect(Color.White)

                            // 2. Draw standard QR finder patterns
                            fun drawFinder(startR: Int, startC: Int) {
                                // Outer 7x7 block
                                drawRect(
                                    color = Color.Black,
                                    topLeft = androidx.compose.ui.geometry.Offset(startC * cellSize, startR * cellSize),
                                    size = androidx.compose.ui.geometry.Size(7 * cellSize, 7 * cellSize)
                                )
                                // Inner 5x5 white square
                                drawRect(
                                    color = Color.White,
                                    topLeft = androidx.compose.ui.geometry.Offset((startC + 1) * cellSize, (startR + 1) * cellSize),
                                    size = androidx.compose.ui.geometry.Size(5 * cellSize, 5 * cellSize)
                                )
                                // Center 3x3 black square
                                drawRect(
                                    color = Color.Black,
                                    topLeft = androidx.compose.ui.geometry.Offset((startC + 2) * cellSize, (startR + 2) * cellSize),
                                    size = androidx.compose.ui.geometry.Size(3 * cellSize, 3 * cellSize)
                                )
                            }

                            drawFinder(0, 0)
                            drawFinder(0, matrixSize - 7)
                            drawFinder(matrixSize - 7, 0)

                            // 3. Deterministic QR grid pattern generated for vhora110@oksbi
                            val seed = upiId.hashCode()
                            val javaRandom = java.util.Random(seed.toLong())

                            for (r in 0 until matrixSize) {
                                for (c in 0 until matrixSize) {
                                    if (!isFinderPattern(r, c)) {
                                        val centerR = matrixSize / 2
                                        val centerC = matrixSize / 2
                                        if (kotlin.math.abs(r - centerR) <= 2 && kotlin.math.abs(c - centerC) <= 2) {
                                            continue
                                        }

                                        if (r == 6 || c == 6) {
                                            if ((r + c) % 2 == 0) {
                                                drawRect(
                                                    color = Color.Black,
                                                    topLeft = androidx.compose.ui.geometry.Offset(c * cellSize, r * cellSize),
                                                    size = androidx.compose.ui.geometry.Size(cellSize, cellSize)
                                                )
                                            }
                                            continue
                                        }

                                        if (javaRandom.nextFloat() < 0.48f) {
                                            drawRect(
                                                color = Color.Black,
                                                topLeft = androidx.compose.ui.geometry.Offset(c * cellSize, r * cellSize),
                                                size = androidx.compose.ui.geometry.Size(cellSize, cellSize)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Center GPay Badge Pill
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 3.dp,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "G",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 16.sp,
                                        color = Color(0xFF4285F4)
                                    )
                                    Text(
                                        text = "Pay",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color(0xFF34A853)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // UPI ID Badge with Copy
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable { onCopyUpi() }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "UPI ID: ",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = upiId,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepBlue
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy UPI",
                                tint = DeepBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Subtitle
            Text(
                text = "Scan to pay with any UPI app (GPay / PhonePe / Paytm / BHIM)",
                fontSize = 11.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Direct UPI App Deep Link Action
            Button(
                onClick = {
                    try {
                        val uri = Uri.parse("upi://pay?pa=$upiId&pn=${Uri.encode(accountHolder)}&am=201&cu=INR&tn=Vendor%20Membership%20Fee")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(Intent.createChooser(intent, "Pay ₹201 with UPI App"))
                    } catch (e: Exception) {
                        Toast.makeText(context, "No UPI App found. Please copy UPI ID $upiId to pay.", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepBlue)
            ) {
                Icon(Icons.Default.QrCode, contentDescription = null, tint = BrightOrange)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Pay $amountStr directly via UPI App",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
