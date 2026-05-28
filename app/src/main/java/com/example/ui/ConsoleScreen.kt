package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GfxProfile
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsoleScreen(
    viewModel: GfxViewModel,
    modifier: Modifier = Modifier
) {
    val liveFps by viewModel.liveFps.collectAsState()
    val livePing by viewModel.livePingHz.collectAsState()
    val liveTemp by viewModel.liveTempCc.collectAsState()
    val isHudOpen by viewModel.isHudOpen.collectAsState()
    val specs by viewModel.deviceSpec.collectAsState()
    val compReport by viewModel.compatibilityReport.collectAsState()
    val dbProfiles by viewModel.dbProfiles.collectAsState()

    val selectedGame by viewModel.selectedGameVersion.collectAsState()
    val selectedFps by viewModel.selectedFrameRate.collectAsState()
    val selectedRes by viewModel.selectedResolution.collectAsState()
    val selectedPreset by viewModel.selectedGraphicPreset.collectAsState()
    val selectedAa by viewModel.selectedAntiAliasing.collectAsState()
    val selectedApi by viewModel.selectedRenderingApi.collectAsState()

    val isBoosting by viewModel.isBoosting.collectAsState()
    val boostMessage by viewModel.boostMessage.collectAsState()
    val activeBoostType by viewModel.activeBoostType.collectAsState()
    val unlockState by viewModel.unlockState.collectAsState()

    val context = LocalContext.current
    var saveProfileDialogShow by remember { mutableStateOf(false) }
    var inputProfileName by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ImmersiveBg)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Immersive Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // System Pulse Green Dot
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .drawBehind {
                                drawCircle(
                                    color = NeonCyan,
                                    radius = size.minDimension / 2,
                                )
                                drawCircle(
                                    color = NeonCyan.copy(alpha = 0.4f),
                                    radius = (size.minDimension) * 1.25f,
                                    style = Stroke(width = 2.dp.toPx())
                                )
                            }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "SYSTEM LIVE",
                        fontSize = 10.sp,
                        color = NeonCyan,
                        letterSpacing = 1.5.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.testTag("system_live_indicator")
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "PING: ${livePing}MS",
                        color = if (livePing < 35) NeonCyan else SlateGray,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "TEMP: ${String.format("%.1f", liveTemp)}°C",
                        color = if (liveTemp > 38) CyberOrange else SlateGray,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // G-BOOST Headline Title
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "G-BOOST PRO",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp,
                    color = Color.White,
                    modifier = Modifier.testTag("app_headline_title")
                )
                Text(
                    text = "Advanced Frame Rate Unlocker • Active Setup: $selectedGame ($selectedFps FPS)",
                    fontSize = 11.sp,
                    color = SlateGray,
                    modifier = Modifier.testTag("app_headline_subtitle")
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Scrollable Workspace Layout
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Circular FPS Telemetry Circle
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Neon Ring Structure matching design
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .drawBehind {
                                    // Outer dashes path effect style
                                    drawCircle(
                                        color = NeonCyan.copy(alpha = 0.15f),
                                        radius = size.minDimension / 2,
                                        style = Stroke(
                                            width = 2.dp.toPx(),
                                            pathEffect = PathEffect.dashPathEffect(
                                                floatArrayOf(12f, 16f),
                                                0f
                                            )
                                        )
                                    )
                                    // Glow Ring
                                    drawCircle(
                                        color = NeonCyan.copy(alpha = 0.4f),
                                        radius = (size.minDimension / 2) - 15.dp.toPx(),
                                        style = Stroke(width = 1.5.dp.toPx())
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "$liveFps",
                                    fontSize = 62.sp,
                                    fontWeight = FontWeight.Black,
                                    color = NeonCyan,
                                    letterSpacing = (-2).sp,
                                    modifier = Modifier.testTag("fps_realtime_output")
                                )
                                Text(
                                    text = "FPS ACTIVE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan.copy(alpha = 0.8f),
                                    letterSpacing = 2.sp,
                                    modifier = Modifier.testTag("fps_caption_status")
                                )
                            }
                        }

                        // Circular HUD Stats Badges
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = 320.dp)
                                .align(Alignment.BottomCenter)
                                .padding(top = 175.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            HUDMiniStat(label = "THERM", value = "${String.format("%.1f", liveTemp)}°C", color = if (liveTemp > 38) CyberOrange else NeonCyan)
                            HUDMiniStat(label = "DELAY", value = "${livePing}ms", color = if (livePing < 35) NeonCyan else SlateGray)
                            HUDMiniStat(label = "VRAM", value = "${specs?.currentAvailableRamGb ?: 3.2}GB", color = NeonCyan)
                        }
                    }
                }

                // Compatibility Report Dashboard (Device compatibility checker)
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(ImmersiveCard)
                            .border(1.dp, ImmersiveCardBorder, RoundedCornerShape(24.dp))
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Assessment,
                                    contentDescription = "Stats Check",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "HARDWARE COMPATIBILITY REPORT",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            // Hardware Tier badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonCyanBg)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = compReport?.hardwareTier?.take(8) ?: "PRO-TIER",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = NeonCyan,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        compReport?.let { report ->
                            // Detailed items grid
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                CompReportItem(
                                    title = "Display Refresh",
                                    value = "${report.displayRefreshHz} Hz",
                                    isOk = report.isDisplayCompliant,
                                    subNote = if (report.isDisplayCompliant) "Supports 90 FPS" else "Locked to 60Hz Limit"
                                )
                                CompReportItem(
                                    title = "Memory Space",
                                    value = "${String.format("%.1f", report.totalRamGb)} GB",
                                    isOk = report.isRamCompliant,
                                    subNote = if (report.isRamCompliant) "Optimal Frame Buffer" else "Risk of Micro-Stutters"
                                )
                                CompReportItem(
                                    title = "Symmetric CPU",
                                    value = "${report.cpuCores} Cores",
                                    isOk = report.isCpuCompliant,
                                    subNote = "Core Sync Enabled"
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            HorizontalDivider(color = ImmersiveCardBorder, thickness = 1.dp)

                            Spacer(modifier = Modifier.height(10.dp))

                            // Custom Advice Message
                            Text(
                                text = report.adviceMessage,
                                color = LightSlate,
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                modifier = Modifier.testTag("compatibility_advice_text")
                            )

                            // Stutter Risk Progress bar
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Estimated Stutter / FPS Drop Risk:",
                                    fontSize = 11.sp,
                                    color = SlateGray
                                )
                                Text(
                                    text = "${report.stutterRiskScore}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (report.stutterRiskScore > 40) CyberOrange else NeonCyan,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = report.stutterRiskScore / 100f,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(CircleShape),
                                color = if (report.stutterRiskScore > 40) CyberOrange else NeonCyan,
                                trackColor = Color.White.copy(alpha = 0.05f)
                            )
                        } ?: run {
                            CircularProgressIndicator(color = NeonCyan, modifier = Modifier.size(20.dp))
                        }
                    }
                }

                // Live Double Interactive Core Booster and Network Internet Booster
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(ImmersiveCard)
                            .border(1.dp, ImmersiveCardBorder, RoundedCornerShape(24.dp))
                            .padding(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Launch,
                                contentDescription = "Optimizer",
                                tint = TacticalYellow,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CORE HARDWARE BOOST TUNERS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Execute surgical physical register corrections to purge background loads and accelerate TCP routers.",
                            fontSize = 11.sp,
                            color = SlateGray
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Double Booster Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.triggerBackgroundMemoryOptimization() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("ram_purger_booster"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White.copy(alpha = 0.04f),
                                    contentColor = NeonCyan
                                ),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Memory, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text("RAM Purger", fontSize = 11.sp, fontWeight = FontWeight.Black)
                                        Text("Clear Background", fontSize = 9.sp, color = SlateGray)
                                    }
                                }
                            }

                            Button(
                                onClick = { viewModel.triggerNetworkInternetBoost() },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("dns_router_booster"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White.copy(alpha = 0.04f),
                                    contentColor = TacticalYellow
                                ),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(1.dp, TacticalYellow.copy(alpha = 0.5f))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.NetworkCheck, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text("Ping Booster", fontSize = 11.sp, fontWeight = FontWeight.Black)
                                        Text("IntelliDNS Link", fontSize = 9.sp, color = SlateGray)
                                    }
                                }
                            }
                        }

                        // Animated live boosting console terminal logs
                        AnimatedVisibility(
                            visible = isBoosting,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 14.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.Black)
                                    .border(1.dp, ImmersiveCardBorder, RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(
                                        color = if (activeBoostType == BoostType.RAM) NeonCyan else TacticalYellow,
                                        modifier = Modifier.size(14.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (activeBoostType == BoostType.RAM) "VRAM CORE OPTIMIZER LIVE" else "INTELLIDNS BOOSTER ENGAGED",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (activeBoostType == BoostType.RAM) NeonCyan else TacticalYellow,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "> $boostMessage",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Interactive HUD Overlay Toggle Widget
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(ImmersiveCard)
                            .border(1.dp, ImmersiveCardBorder, RoundedCornerShape(24.dp))
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Monitor,
                                    contentDescription = "Overlay",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "NON-INTRUSIVE OVERLAY HUD",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = "Float live framerate & telemetry while playing",
                                        fontSize = 10.sp,
                                        color = SlateGray
                                    )
                                }
                            }
                            Switch(
                                checked = isHudOpen,
                                onCheckedChange = { viewModel.toggleHudOverlay(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.Black,
                                    checkedTrackColor = NeonCyan,
                                    uncheckedThumbColor = SlateGray,
                                    uncheckedTrackColor = Color.White.copy(alpha = 0.05f)
                                )
                            )
                        }
                    }
                }

                // Game selection Grid
                item {
                    Text(
                        text = "SELECT ACTIVE INSTALLED GAME",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateGray,
                        letterSpacing = 1.5.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // BGMI card
                        GameTunerCard(
                            gameName = "Battlegrounds",
                            versionName = "BGMI Ver 2.9",
                            badgeText = "BGMI",
                            badgeBgColor = TacticalYellow,
                            badgeTextColor = Color.Black,
                            isSelected = selectedGame == "BGMI",
                            isInstalled = specs?.isBgmiInstalled ?: false,
                            onClick = { viewModel.selectedGameVersion.value = "BGMI" },
                            modifier = Modifier.weight(1f)
                        )

                        // PUBG GLOBAL card
                        GameTunerCard(
                            gameName = "PUBG Mobile",
                            versionName = "Global Ver 3.1",
                            badgeText = "PUBG",
                            badgeBgColor = CyberOrange,
                            badgeTextColor = Color.White,
                            isSelected = selectedGame == "PUBG Global",
                            isInstalled = specs?.isPubgGlobalInstalled ?: false,
                            onClick = { viewModel.selectedGameVersion.value = "PUBG Global" },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Manual parameter tuning sliders & dropdowns
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(ImmersiveCard)
                            .border(1.dp, ImmersiveCardBorder, RoundedCornerShape(24.dp))
                            .padding(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Manual setting",
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ADVANCED GFX TUNER MODULE",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Target Frame rate
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("FPS Target", fontSize = 10.sp, color = SlateGray)
                                Spacer(modifier = Modifier.height(4.dp))
                                CustomValueSelector(
                                    selected = selectedFps,
                                    options = listOf("60", "90", "120"),
                                    onChanged = { viewModel.selectedFrameRate.value = it }
                                )
                            }
                            Column(modifier = Modifier.weight(1.5f)) {
                                Text("Anti-Aliasing", fontSize = 10.sp, color = SlateGray)
                                Spacer(modifier = Modifier.height(4.dp))
                                CustomValueSelector(
                                    selected = selectedAa,
                                    options = listOf("Disabled", "2x", "4x"),
                                    onChanged = { viewModel.selectedAntiAliasing.value = it }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Graphic preset & API
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Column(modifier = Modifier.weight(1.2f)) {
                                Text("Graphics Preset", fontSize = 10.sp, color = SlateGray)
                                Spacer(modifier = Modifier.height(4.dp))
                                CustomValueSelector(
                                    selected = selectedPreset,
                                    options = listOf("Smooth", "Balanced", "HD", "HDR"),
                                    onChanged = { viewModel.selectedGraphicPreset.value = it }
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Rendering API", fontSize = 10.sp, color = SlateGray)
                                Spacer(modifier = Modifier.height(4.dp))
                                CustomValueSelector(
                                    selected = selectedApi,
                                    options = listOf("Vulkan", "OpenGL ES"),
                                    onChanged = { viewModel.selectedRenderingApi.value = it }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Resolution
                        Column {
                            Text("Resolution Level", fontSize = 10.sp, color = SlateGray)
                            Spacer(modifier = Modifier.height(4.dp))
                            CustomValueSelector(
                                selected = selectedRes,
                                options = listOf("Smooth (540p)", "Standard (960x540)", "HD (1280x720)", "Ultra HD (1920x1080)"),
                                onChanged = { viewModel.selectedResolution.value = it }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Quick profile save dial trigger
                        Button(
                            onClick = {
                                inputProfileName = "${selectedGame} ${selectedFps}FPS Custom Profile"
                                saveProfileDialogShow = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.05f),
                                contentColor = NeonCyan
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Config as Custom Profile", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Profile Persistent Database Area
                if (dbProfiles.isNotEmpty()) {
                    item {
                        Text(
                            text = "SAVED ESPORTS PRESETS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SlateGray,
                            letterSpacing = 1.5.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    items(dbProfiles) { profile ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(ImmersiveCard)
                                .border(
                                    width = 1.dp,
                                    color = if (profile.isApplied) NeonCyan.copy(alpha = 0.5f) else ImmersiveCardBorder,
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (profile.isApplied) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Applied",
                                            tint = NeonCyan,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    Text(
                                        text = profile.profileName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${profile.gameVersion} • ${profile.frameRate} FPS • ${profile.graphicsPreset} • ${profile.renderingApi}",
                                    fontSize = 10.sp,
                                    color = SlateGray
                                )
                            }

                            Row(
                                modifier = Modifier.wrapContentSize(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.applyPresetProfile(profile) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (profile.isApplied) NeonCyanBg else Color.White.copy(alpha = 0.04f),
                                        contentColor = if (profile.isApplied) NeonCyan else Color.White
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(if (profile.isApplied) "Applied" else "Load", fontSize = 10.sp)
                                }

                                IconButton(
                                    onClick = { viewModel.deleteProfile(profile) },
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Delete",
                                        tint = ErrorRed.copy(alpha = 0.7f),
                                        modifier = Modifier.size(17.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Extra cushioning space for bottom bars
                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }

            // G-BOOST TRIGGER BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, ImmersiveBg.copy(alpha = 0.95f), ImmersiveBg)
                        )
                    )
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { viewModel.triggerGFXUnlockSequence() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonCyan,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("apply_and_optimize_button"),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Text(
                        text = "APPLY CONFIG & OPTIMIZE",
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // Real-time HUD overlay simulator dialog rendering elegantly on top of screen
        AnimatedVisibility(
            visible = isHudOpen,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xE60D121B))
                    .border(1.dp, NeonCyan.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                    .clickable { viewModel.toggleHudOverlay(false) }
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color.Green, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "G-BOOST HUD",
                        fontSize = 9.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FPS: $liveFps",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "PING: ${livePing}ms",
                        color = if (livePing < 30) Color.Green else TacticalYellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "TEMP: ${String.format("%.1f", liveTemp)}°C",
                        color = if (liveTemp > 38) CyberOrange else Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Bottom dialog trigger popup when unlocking registers
        if (unlockState is UnlockState.Progress || unlockState is UnlockState.Success) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(32.dp))
                        .background(ImmersiveCard)
                        .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(32.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (val state = unlockState) {
                        is UnlockState.Progress -> {
                            CircularProgressIndicator(color = NeonCyan, strokeWidth = 3.dp, modifier = Modifier.size(50.dp))
                            Spacer(modifier = Modifier.height(18.dp))
                            Text(
                                text = "MODIFYING GFX REGISTER PIPELINE",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = state.step,
                                fontSize = 11.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            LinearProgressIndicator(
                                progress = state.percent / 100f,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape),
                                color = NeonCyan,
                                trackColor = Color.White.copy(alpha = 0.05f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${state.percent}%",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = SlateGray
                            )
                        }

                        is UnlockState.Success -> {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Active",
                                tint = NeonCyan,
                                modifier = Modifier.size(55.dp)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "90 FPS HIGH RESOLUTION INJECTED",
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Black,
                                color = NeonCyan
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = state.manualSteps,
                                fontSize = 11.sp,
                                color = LightSlate,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.resetUnlockState() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White.copy(alpha = 0.05f),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Discard", fontSize = 12.sp)
                                }

                                Button(
                                    onClick = {
                                        viewModel.resetUnlockState()
                                        val targetIntent = state.launchIntent
                                        if (targetIntent != null) {
                                            viewModel.launchGame(targetIntent)
                                        } else {
                                            viewModel.launchGame("com.tencent.ig")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = NeonCyan,
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.weight(1.5f)
                                ) {
                                    Text(
                                        if (state.isGameInstalled) "LAUNCH SELECTED GAME" else "GET GAME VIA GOOGLE PLAY",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }
        }

        // Persistent save profile pop-up
        if (saveProfileDialogShow) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(ImmersiveCard)
                        .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                        .padding(20.dp)
                ) {
                    Text(
                        text = "SAVE CURRENT PROFILE CONFIG",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = inputProfileName,
                        onValueChange = { inputProfileName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_name_input_field"),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                        placeholder = { Text("Profile Name", color = SlateGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = ImmersiveCardBorder,
                            focusedTextColor = Color.White,
                            focusedContainerColor = Color.Black.copy(alpha = 0.2f),
                            unfocusedContainerColor = Color.Black.copy(alpha = 0.2f)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { saveProfileDialogShow = false }) {
                            Text("Cancel", color = SlateGray)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                if (inputProfileName.isNotBlank()) {
                                    viewModel.saveCurrentTuningProfile(inputProfileName)
                                    saveProfileDialogShow = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black)
                        ) {
                            Text("SAVE CONFIG", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CompReportItem(
    title: String,
    value: String,
    isOk: Boolean,
    subNote: String
) {
    Column(
        modifier = Modifier
            .width(95.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 9.sp,
            color = SlateGray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            color = if (isOk) NeonCyan else CyberOrange,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = subNote,
            fontSize = 8.sp,
            color = if (isOk) SlateGray else CyberOrange.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 10.sp,
            maxLines = 2
        )
    }
}

@Composable
fun HUDMiniStat(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.02f))
            .border(1.dp, ImmersiveCardBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(text = label, fontSize = 9.sp, color = SlateGray, letterSpacing = 1.sp)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color, fontFamily = FontFamily.Monospace)
    }
}

@Composable
fun GameTunerCard(
    gameName: String,
    versionName: String,
    badgeText: String,
    badgeBgColor: Color,
    badgeTextColor: Color,
    isSelected: Boolean,
    isInstalled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(ImmersiveCard)
            .border(
                width = 2.dp,
                color = if (isSelected) NeonCyan else ImmersiveCardBorder,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBgColor)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = badgeText, fontSize = 8.sp, fontWeight = FontWeight.Black, color = badgeTextColor)
                }

                if (isInstalled) {
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(5.dp).background(Color.Green, CircleShape))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Active", fontSize = 8.sp, color = Color.Green, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = gameName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = versionName, fontSize = 9.sp, color = SlateGray)
        }
    }
}

@Composable
fun CustomValueSelector(
    selected: String,
    options: List<String>,
    onChanged: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.03f))
                .border(1.dp, ImmersiveCardBorder, RoundedCornerShape(12.dp))
                .clickable { expanded = true }
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selected,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = SlateGray,
                modifier = Modifier.size(16.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(ImmersiveCard)
                .border(1.dp, ImmersiveCardBorder, RoundedCornerShape(12.dp))
        ) {
            options.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt, fontSize = 12.sp, color = Color.White) },
                    onClick = {
                        onChanged(opt)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = Color.White,
                        trailingIconColor = SlateGray
                    )
                )
            }
        }
    }
}
