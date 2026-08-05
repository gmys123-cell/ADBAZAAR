package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrightOrange
import com.example.ui.theme.DeepBlue

@Composable
fun AdBazaarIcon(
    size: Dp = 40.dp,
    showBorder: Boolean = true
) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(DeepBlue, BrightOrange)
    )

    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.28f))
            .background(gradientBrush)
            .then(
                if (showBorder) Modifier.border(
                    width = 1.5.dp,
                    color = Color.White.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(size * 0.28f)
                ) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        // Overlay combining Shopping Bag + Megaphone
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(size * 0.48f)
            )
            Icon(
                imageVector = Icons.Default.Campaign,
                contentDescription = null,
                tint = BrightOrange,
                modifier = Modifier
                    .size(size * 0.42f)
                    .background(Color.White, CircleShape)
                    .padding(2.dp)
            )
        }
    }
}

@Composable
fun AdBazaarLogo(
    modifier: Modifier = Modifier,
    iconSize: Dp = 38.dp,
    textSizeSp: Int = 22,
    showTagline: Boolean = false,
    textColorPrimary: Color = DeepBlue,
    textColorSecondary: Color = BrightOrange
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AdBazaarIcon(size = iconSize)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Ad",
                    fontSize = textSizeSp.sp,
                    fontWeight = FontWeight.Black,
                    color = textColorPrimary,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Bazaar",
                    fontSize = textSizeSp.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColorSecondary,
                    letterSpacing = (-0.5).sp
                )
            }
            if (showTagline) {
                Text(
                    text = "BUSINESS & PRODUCT ADS",
                    fontSize = (textSizeSp * 0.38f).sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
