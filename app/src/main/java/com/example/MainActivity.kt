package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppSummaryScreen
import com.example.ui.CoachScreen
import com.example.ui.ConsoleScreen
import com.example.ui.GfxViewModel
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ImmersiveBg
import com.example.ui.theme.ImmersiveCard
import com.example.ui.theme.ImmersiveCardBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SlateGray

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: GfxViewModel = viewModel()
                var currentNavigationScreen by remember { mutableStateOf("console") }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            containerColor = ImmersiveCard,
                            tonalElevation = 8.dp,
                            modifier = Modifier
                                .navigationBarsPadding()
                                .testTag("app_bottom_bar")
                        ) {
                            NavigationBarItem(
                                selected = currentNavigationScreen == "console",
                                onClick = { currentNavigationScreen = "console" },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.SportsEsports,
                                        contentDescription = "Console"
                                    )
                                },
                                label = { Text("CONSOLE", fontFamily = FontFamily.Monospace, modifier = Modifier.testTag("console_nav_label")) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NeonCyan,
                                    unselectedIconColor = SlateGray,
                                    selectedTextColor = NeonCyan,
                                    unselectedTextColor = SlateGray,
                                    indicatorColor = ImmersiveCardBorder
                                ),
                                modifier = Modifier.testTag("nav_item_console")
                            )

                            NavigationBarItem(
                                selected = currentNavigationScreen == "coach",
                                onClick = { currentNavigationScreen = "coach" },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "AI Coach"
                                    )
                                },
                                label = { Text("COACH", fontFamily = FontFamily.Monospace, modifier = Modifier.testTag("coach_nav_label")) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NeonCyan,
                                    unselectedIconColor = SlateGray,
                                    selectedTextColor = NeonCyan,
                                    unselectedTextColor = SlateGray,
                                    indicatorColor = ImmersiveCardBorder
                                ),
                                modifier = Modifier.testTag("nav_item_coach")
                            )

                            NavigationBarItem(
                                selected = currentNavigationScreen == "info",
                                onClick = { currentNavigationScreen = "info" },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "About"
                                    )
                                },
                                label = { Text("SAFETY", fontFamily = FontFamily.Monospace, modifier = Modifier.testTag("safety_nav_label")) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NeonCyan,
                                    unselectedIconColor = SlateGray,
                                    selectedTextColor = NeonCyan,
                                    unselectedTextColor = SlateGray,
                                    indicatorColor = ImmersiveCardBorder
                                ),
                                modifier = Modifier.testTag("nav_item_info")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ImmersiveBg)
                            .padding(innerPadding)
                            .statusBarsPadding()
                    ) {
                        when (currentNavigationScreen) {
                            "console" -> ConsoleScreen(viewModel = viewModel)
                            "coach" -> CoachScreen(viewModel = viewModel)
                            "info" -> AppSummaryScreen()
                        }
                    }
                }
            }
        }
    }
}
