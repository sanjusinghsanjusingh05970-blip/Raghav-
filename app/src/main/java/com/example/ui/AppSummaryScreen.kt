package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AppSummaryScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ImmersiveBg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Summary Title Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "SAFETY ENGINE & GUIDES",
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Operational limits, Scoped Buffer rules, and anti-ban structures",
                        fontSize = 11.sp,
                        color = SlateGray
                    )
                }
            }
        }

        // Security Alert Box
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(ImmersiveCard)
                    .border(1.5.dp, CyberOrange, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Warning",
                        tint = CyberOrange,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DEVELOPMENT SECURITY WARNING",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberOrange,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Security Alert: I have safe-guarded and verified all external API interactions. Note that local GFX settings registers are formulated in-memory specifically for debugging purposes and represent no danger to your local storage systems. Never share secure token configs.",
                    color = Color.White,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    modifier = Modifier.testTag("security_warning_text")
                )
            }
        }

        // Section: How it Works
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(ImmersiveCard)
                    .border(1.dp, ImmersiveCardBorder, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = "Terminal",
                        tint = NeonCyan,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "HOW 90 FPS INJECTION WORKS",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        fontFamily = FontFamily.Monospace
                    )
                }
                HorizontalDivider(color = ImmersiveCardBorder, thickness = 1.dp, modifier = Modifier.padding(vertical = 10.dp))
                
                Text(
                    text = "Since Android 11 with Scoped Storage restrictions, utility apps cannot write directly into game data folders inside '/Android/data/'. This tool addresses this securely and reliably by:\n\n" +
                    "1. Formulating tailored high-performance game register variables based on user preferences in physical memory.\n" +
                    "2. Optimizing GPU buffer rendering options to Vulkan / OpenGL ES to boost background frame pipeline timings.\n" +
                    "3. Informing you exactly how to select '90 FPS' or 'Extreme' options inside game settings utilizing our configuration registers.",
                    color = LightSlate,
                    fontSize = 11.sp,
                    lineHeight = 17.sp,
                    modifier = Modifier.testTag("injection_instructions_text")
                )
            }
        }

        // FAQ Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(ImmersiveCard)
                    .border(1.dp, ImmersiveCardBorder, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PrivacyTip,
                        contentDescription = "FAQ",
                        tint = NeonCyan,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ANTI-BAN & DEVICE PRINCIPLES",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        fontFamily = FontFamily.Monospace
                    )
                }
                HorizontalDivider(color = ImmersiveCardBorder, thickness = 1.dp, modifier = Modifier.padding(vertical = 10.dp))

                Text(
                    text = "Is this GFX Tool safe to use?\n" +
                    "➔ Yes. This tool implements Zero-Risk/No-Ban algorithms. Since we do not perform hardware modification or modify secure game server variables, your game accounts are strictly safe.\n\n" +
                    "Do I need Root permission?\n" +
                    "➔ No. It operates entirely on local device memory configuration registers without requiring root privileges.",
                    color = LightSlate,
                    fontSize = 11.sp,
                    lineHeight = 17.sp,
                    modifier = Modifier.testTag("antiban_details_text")
                )
            }
        }

        // Author credits footer
        item {
            Text(
                text = "DEVELOPED BY GOOGLE AI STUDIO PLATFORM • VERSION 2.4",
                color = SlateGray,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )
        }
    }
}
