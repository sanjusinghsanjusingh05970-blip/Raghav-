package com.example.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun CoachScreen(
    viewModel: GfxViewModel,
    modifier: Modifier = Modifier
) {
    val coachState by viewModel.coachState.collectAsState()
    val chatHistory by viewModel.chatHistory.collectAsState()
    val isSendingChat by viewModel.isSendingChat.collectAsState()
    val specs by viewModel.deviceSpec.collectAsState()

    var userMessageText by remember { mutableStateOf("") }
    val chatListState = rememberLazyListState()

    // Auto-scroll chat to end when history modifies
    LaunchedEffect(chatHistory.size) {
        if (chatHistory.isNotEmpty()) {
            chatListState.animateScrollToItem(chatHistory.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ImmersiveBg)
    ) {
        // AI Header bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "AI ESPORTS COACH ENGINE",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Personalized graphics diagnostics & game maneuvers",
                    fontSize = 10.sp,
                    color = SlateGray
                )
            }
        }

        LazyColumn(
            state = chatListState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Automated Compatibility Optimizer Audit trigger card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(ImmersiveCard)
                        .border(1.dp, ImmersiveCardBorder, RoundedCornerShape(24.dp))
                        .padding(18.dp)
                ) {
                    Text(
                        text = "HARDWARE TUNING ASSISTANCE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Trigger a deep analysis on your ${specs?.manufacturer ?: "device"} CPU cores, RAM limits, and thermal configs, then provide optimal gameplay setting recipes.",
                        fontSize = 11.sp,
                        color = SlateGray,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    when (val state = coachState) {
                        is CoachState.Loading -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Gemini formulating optimization recipe...", fontSize = 11.sp, color = NeonCyan)
                            }
                        }

                        is CoachState.AnalysisReceived -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color.Black.copy(alpha = 0.3f))
                                    .padding(12.dp)
                                    .verticalScroll(rememberScrollState())
                                    .heightIn(max = 240.dp)
                            ) {
                                Text(
                                    text = state.recommendation,
                                    color = LightSlate,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { viewModel.requestAICoachAnalysis() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.05f), contentColor = NeonCyan),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Re-Analyze Device Pipeline", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        is CoachState.Error -> {
                            Text(text = state.message, color = ErrorRed, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { viewModel.requestAICoachAnalysis() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.05f), contentColor = NeonCyan),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Retry Hardware Diagnostic Analysis", fontSize = 11.sp)
                            }
                        }

                        is CoachState.Idle -> {
                            Button(
                                onClick = { viewModel.requestAICoachAnalysis() },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = Color.Black),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("GENERATE ES-PORTS HEURISTICS REPORT", fontSize = 11.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }

            // Divider labeled "LOGS & COMMUNICATIONS"
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = ImmersiveCardBorder)
                    Text(
                        text = "TACTICAL CHAT LOGS",
                        fontSize = 9.sp,
                        color = SlateGray,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = ImmersiveCardBorder)
                }
            }

            // Chat loop
            items(chatHistory) { msg ->
                ChatBubble(msg = msg)
            }

            if (isSendingChat) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(bottomEnd = 16.dp, topStart = 16.dp, topEnd = 16.dp))
                                .background(ImmersiveCard)
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(color = NeonCyan, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Coach is analyzing strategy...", color = SlateGray, fontSize = 10.sp)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Bottom text message box sender
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ImmersiveCard)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = userMessageText,
                    onValueChange = { userMessageText = it },
                    placeholder = { Text("Ask about anti-aliasing, layout, or triggers...", color = SlateGray, fontSize = 12.sp) },
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 12.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = ImmersiveCardBorder,
                        focusedContainerColor = Color.Black.copy(alpha = 0.2f),
                        unfocusedContainerColor = Color.Black.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("coach_chat_text_box"),
                    maxLines = 3
                )

                IconButton(
                    onClick = {
                        if (userMessageText.isNotBlank()) {
                            viewModel.sendChatMessage(userMessageText)
                            userMessageText = ""
                        }
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(NeonCyan)
                        .testTag("coach_send_message_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(msg: ChatMessage) {
    val isUser = msg.sender == "user"
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier.widthIn(max = 280.dp),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        if (isUser) RoundedCornerShape(bottomStart = 16.dp, topStart = 16.dp, topEnd = 16.dp)
                        else RoundedCornerShape(bottomEnd = 16.dp, topStart = 16.dp, topEnd = 16.dp)
                    )
                    .background(if (isUser) NeonCyanBg else ImmersiveCard)
                    .border(
                        width = 1.dp,
                        color = if (isUser) NeonCyan.copy(alpha = 0.4f) else ImmersiveCardBorder,
                        shape = if (isUser) RoundedCornerShape(bottomStart = 16.dp, topStart = 16.dp, topEnd = 16.dp)
                        else RoundedCornerShape(bottomEnd = 16.dp, topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = msg.text,
                    color = if (isUser) NeonCyan else Color.White,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    modifier = Modifier.testTag(if (isUser) "user_chat_bubble" else "ai_chat_bubble")
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (isUser) "You" else "Optimizer Coach",
                fontSize = 8.sp,
                color = SlateGray,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
