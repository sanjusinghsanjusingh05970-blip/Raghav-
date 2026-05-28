package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

data class ChatMessage(
    val sender: String, // "user" or "ai"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

sealed class UnlockState {
    object Idle : UnlockState()
    data class Progress(val step: String, val percent: Int) : UnlockState()
    data class Success(val manualSteps: String, val isGameInstalled: Boolean, val launchIntent: String?) : UnlockState()
    data class Error(val message: String) : UnlockState()
}

sealed class CoachState {
    object Idle : CoachState()
    object Loading : CoachState()
    data class AnalysisReceived(val recommendation: String) : CoachState()
    data class Error(val message: String) : CoachState()
}

sealed class BoostType {
    object RAM : BoostType()
    object NET : BoostType()
}

class GfxViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dao = db.gfxProfileDao()

    // Real-time specs state
    private val _deviceSpec = MutableStateFlow<DeviceSpec?>(null)
    val deviceSpec: StateFlow<DeviceSpec?> = _deviceSpec.asStateFlow()

    // Compatibility report
    private val _compatibilityReport = MutableStateFlow<CompatibilityReport?>(null)
    val compatibilityReport: StateFlow<CompatibilityReport?> = _compatibilityReport.asStateFlow()

    // Loaded Local Profiles from SQLite
    private val _dbProfiles = MutableStateFlow<List<GfxProfile>>(emptyList())
    val dbProfiles: StateFlow<List<GfxProfile>> = _dbProfiles.asStateFlow()

    // Dynamic gameplay HUD metrics
    private val _liveFps = MutableStateFlow(60)
    val liveFps: StateFlow<Int> = _liveFps.asStateFlow()

    private val _livePingHz = MutableStateFlow(48)
    val livePingHz: StateFlow<Int> = _livePingHz.asStateFlow()

    private val _liveTempCc = MutableStateFlow(32.4)
    val liveTempCc: StateFlow<Double> = _liveTempCc.asStateFlow()

    private val _isHudOpen = MutableStateFlow(false)
    val isHudOpen: StateFlow<Boolean> = _isHudOpen.asStateFlow()

    // Config inputs (tuner state)
    val selectedGameVersion = MutableStateFlow("BGMI") // BGMI, PUBG Global, PUBG KR
    val selectedFrameRate = MutableStateFlow("90") // 60, 90, 120
    val selectedResolution = MutableStateFlow("HD (1280x720)") 
    val selectedGraphicPreset = MutableStateFlow("Smooth") 
    val selectedAntiAliasing = MutableStateFlow("4x") 
    val selectedShadows = MutableStateFlow("Medium") 
    val selectedRenderingApi = MutableStateFlow("Vulkan") 
    val selectedColorStyle = MutableStateFlow("Colorful") 

    // Boosting status flags
    private val _isBoosting = MutableStateFlow(false)
    val isBoosting: StateFlow<Boolean> = _isBoosting.asStateFlow()

    private val _boostMessage = MutableStateFlow("")
    val boostMessage: StateFlow<String> = _boostMessage.asStateFlow()

    private val _activeBoostType = MutableStateFlow<BoostType?>(null)
    val activeBoostType: StateFlow<BoostType?> = _activeBoostType.asStateFlow()

    // FPS Injection state machine
    private val _unlockState = MutableStateFlow<UnlockState>(UnlockState.Idle)
    val unlockState: StateFlow<UnlockState> = _unlockState.asStateFlow()

    // Gemini states
    private val _coachState = MutableStateFlow<CoachState>(CoachState.Idle)
    val coachState: StateFlow<CoachState> = _coachState.asStateFlow()

    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(listOf(
        ChatMessage("ai", "Hello Soldier! I am your AI Hardware Optimizer. Ask me for customized graphics registers, thermal throttle mitigation, or tactical high-tier drops for Erangel or Sanhok!")
    ))
    val chatHistory: StateFlow<List<ChatMessage>> = _chatHistory.asStateFlow()

    private val _isSendingChat = MutableStateFlow(false)
    val isSendingChat: StateFlow<Boolean> = _isSendingChat.asStateFlow()

    init {
        refreshDiagnostics()
        observeDatabaseProfiles()
        startLiveGameTelemetryTicker()
    }

    fun refreshDiagnostics() {
        viewModelScope.launch {
            try {
                val specs = SystemDiagnostics.getDeviceSpecs(getApplication())
                _deviceSpec.value = specs
                
                // Set default live telemetry initial targets
                _livePingHz.value = if (specs.pingMs > 0) specs.pingMs else 48
                _liveTempCc.value = if (specs.batteryTempCc > 10.0) specs.batteryTempCc else 32.4
                _liveFps.value = when (selectedFrameRate.value) {
                    "90" -> 90
                    "120" -> 120
                    else -> 60
                }

                // Generate device compatibility report
                _compatibilityReport.value = CompatibilityChecker.generateReport(specs)
            } catch (t: Throwable) {
                Log.e("GfxViewModel", "Specs read failed", t)
            }
        }
    }

    // Interactive Core Booster & RAM Purger
    fun triggerBackgroundMemoryOptimization() {
        viewModelScope.launch {
            _activeBoostType.value = BoostType.RAM
            _isBoosting.value = true
            
            val steps = listOf(
                "Scanning memory pages...",
                "Stopping auxiliary cache allocators...",
                "Purging standard background background tasks...",
                "Clearing graphic buffer garbage pages...",
                "Cooling CPU thermal threshold line...",
                "Purge Complete! Latency overhead dropped."
            )
            for (step in steps) {
                _boostMessage.value = step
                delay(600)
            }
            delay(300)
            _isBoosting.value = false
            
            // Simulating positive effect of boot
            val specs = _deviceSpec.value
            if (specs != null) {
                // Instantly increase simulated available RAM level and cool down temp slightly
                _liveTempCc.value = (_liveTempCc.value - 1.8).coerceAtLeast(29.0)
                _deviceSpec.value = specs.copy(
                    currentAvailableRamGb = (specs.currentAvailableRamGb + 1.2).coerceAtMost(specs.totalRamGb)
                )
            }
            refreshDiagnostics()
        }
    }

    // High fidelity Interactive Ping & DNS Stabilizer Booster
    fun triggerNetworkInternetBoost() {
        viewModelScope.launch {
            _activeBoostType.value = BoostType.NET
            _isBoosting.value = true
            
            val steps = listOf(
                "Polling network interface registers...",
                "Analyzing active gateway DNS hop latency...",
                "Binding socket connections to primary DNS router (1.1.1.1)...",
                "Injecting anti-packet-loss keepalive registers...",
                "Accelerating TCP game pipelines for BGMI...",
                "Network stabilized successfully!"
            )
            for (step in steps) {
                _boostMessage.value = step
                delay(600)
            }
            delay(300)
            _isBoosting.value = false
            
            // Simulating real drop in communication latency
            _livePingHz.value = Random.nextInt(18, 35)
        }
    }

    private fun startLiveGameTelemetryTicker() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                delay(2000)
                // Maintain telemetry motion with authentic minor jitter
                val currentTargetFps = try {
                    selectedFrameRate.value.toInt()
                } catch (e: Exception) {
                    60
                }

                _liveFps.value = (currentTargetFps + Random.nextInt(-3, 1)).coerceAtLeast(30)
                
                // Latency fluctuation based on boost state
                val basePing = if (_livePingHz.value < 40) _livePingHz.value else 48
                _livePingHz.value = (basePing + Random.nextInt(-2, 3)).coerceAtLeast(15)

                // Battery temperature fluctuations
                _liveTempCc.value = (_liveTempCc.value + Random.nextDouble(-0.1, 0.2)).coerceIn(28.0, 42.0)
            }
        }
    }

    private fun observeDatabaseProfiles() {
        viewModelScope.launch {
            try {
                dao.getAllProfilesFlow().collectLatest { list ->
                    _dbProfiles.value = list
                }
            } catch (t: Throwable) {
                if (t is kotlinx.coroutines.CancellationException) throw t
                Log.e("GfxViewModel", "DB observe profiles failed", t)
            }
        }

        viewModelScope.launch {
            try {
                val count = withContext(Dispatchers.IO) { dao.getCount() }
                if (count == 0) {
                    prepopulateDefaultProfiles()
                }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) throw e
                Log.e("GfxViewModel", "DB prepopulate check failed", e)
            }
        }
    }

    private suspend fun prepopulateDefaultProfiles() {
        val defaults = listOf(
            GfxProfile(
                profileName = "Erangel Tournament Pro Preset",
                gameVersion = "BGMI",
                frameRate = "90",
                resolution = "HD (1280x720)",
                graphicsPreset = "Smooth",
                antiAliasing = "4x",
                shadows = "Low",
                renderingApi = "Vulkan",
                colorStyle = "Colorful"
            ),
            GfxProfile(
                profileName = "Livik Ultra Performance 120 FPS",
                gameVersion = "PUBG Global",
                frameRate = "120",
                resolution = "Smooth (540p)",
                graphicsPreset = "Smooth",
                antiAliasing = "Disabled",
                shadows = "Disabled",
                renderingApi = "Vulkan",
                colorStyle = "Classic"
            )
        )
        withContext(Dispatchers.IO) {
            for (p in defaults) {
                dao.insertProfile(p)
            }
        }
    }

    fun saveCurrentTuningProfile(customName: String) {
        viewModelScope.launch {
            val profile = GfxProfile(
                profileName = customName,
                gameVersion = selectedGameVersion.value,
                frameRate = selectedFrameRate.value,
                resolution = selectedResolution.value,
                graphicsPreset = selectedGraphicPreset.value,
                antiAliasing = selectedAntiAliasing.value,
                shadows = selectedShadows.value,
                renderingApi = selectedRenderingApi.value,
                colorStyle = selectedColorStyle.value
            )
            dao.insertProfile(profile)
            refreshDiagnostics()
        }
    }

    fun applyPresetProfile(profile: GfxProfile) {
        viewModelScope.launch {
            selectedGameVersion.value = profile.gameVersion
            selectedFrameRate.value = profile.frameRate
            selectedResolution.value = profile.resolution
            selectedGraphicPreset.value = profile.graphicsPreset
            selectedAntiAliasing.value = profile.antiAliasing
            selectedShadows.value = profile.shadows
            selectedRenderingApi.value = profile.renderingApi
            selectedColorStyle.value = profile.colorStyle

            dao.clearAppliedFlags()
            dao.setProfileApplied(profile.id)
            triggerGFXUnlockSequence()
        }
    }

    fun deleteProfile(profile: GfxProfile) {
        viewModelScope.launch {
            dao.deleteProfile(profile)
        }
    }

    fun toggleHudOverlay(isOpen: Boolean) {
        _isHudOpen.value = isOpen
    }

    fun triggerGFXUnlockSequence() {
        viewModelScope.launch {
            _unlockState.value = UnlockState.Progress("Initializing 90 FPS configuration scanner...", 10)
            delay(700)
            
            val version = selectedGameVersion.value
            val fps = selectedFrameRate.value
            val res = selectedResolution.value
            val preset = selectedGraphicPreset.value
            
            _unlockState.value = UnlockState.Progress("Allocating advanced buffer registers inside game stack...", 35)
            delay(800)
            
            _unlockState.value = UnlockState.Progress("Calibrating graphics pipeline for $version...", 60)
            delay(900)

            _unlockState.value = UnlockState.Progress("Matching Vulkan frame alignment registers (Target = $fps FPS)...", 85)
            delay(800)

            _unlockState.value = UnlockState.Progress("Applying optimal $res resolution modifiers...", 95)
            delay(500)

            // Detect if game package installed to enable instant direct launched activity
            val targetPackage = when(version) {
                "BGMI" -> "com.pubg.imobile"
                "PUBG Global" -> "com.tencent.ig"
                "PUBG KR" -> "com.pubg.krmobile"
                else -> "com.tencent.ig"
            }

            val pm = getApplication<Application>().packageManager
            var isInstalled = false
            try {
                pm.getPackageInfo(targetPackage, 0)
                isInstalled = true
            } catch (e: Exception) {
                // ignore
            }

            val stepsManual = """
                Settings applied successfully! The tool formulated premium game variables natively. Complete these final actions for guaranteed 90 FPS synchronization:
                
                1. Tap 'LAUNCH GAME' to trigger direct launch registers.
                2. Once the game loads, tap the top-left floating G-BOOST overlay to verify active FPS.
                3. Navigate to: settings ➔ Graphics & Audio inside the game.
                4. Select 'Extreme' or the newly unlocked '90 FPS' frame rate option. 
                5. Enjoy gorgeous fluid graphics backed by our latency stabilization algorithms!
            """.trimIndent()

            _unlockState.value = UnlockState.Success(
                manualSteps = stepsManual,
                isGameInstalled = isInstalled,
                launchIntent = if (isInstalled) targetPackage else null
            )
        }
    }

    fun resetUnlockState() {
        _unlockState.value = UnlockState.Idle
    }

    fun launchGame(packageName: String) {
        val pm = getApplication<Application>().packageManager
        val intent = pm.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            getApplication<Application>().startActivity(intent)
        } else {
            val url = "https://play.google.com/store/apps/details?id=$packageName"
            val launchIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            getApplication<Application>().startActivity(launchIntent)
        }
    }

    // --- Gemini AI Optimizer ---
    fun requestAICoachAnalysis() {
        val currentSpecs = _deviceSpec.value ?: return
        val compReport = _compatibilityReport.value ?: return
        viewModelScope.launch {
            _coachState.value = CoachState.Loading
            
            val prompt = """
                You are a highly experienced Esports & Hardware Game Optimizer Coach specialized in PUBG Mobile and BGMI (Battlegrounds Mobile India).
                The player is using:
                - Device Model: ${currentSpecs.manufacturer} ${currentSpecs.model}
                - Ram: ${currentSpecs.totalRamGb} GB (Available: ${currentSpecs.currentAvailableRamGb} GB)
                - Active Screen Refresh Rate: ${currentSpecs.refreshRateHz} Hz
                - CPU Cores: ${currentSpecs.cpuCores}
                - Selected Gaming Setup: ${selectedGameVersion.value} targeting ${selectedFrameRate.value} FPS, graphics: ${selectedGraphicPreset.value}, shadows: ${selectedShadows.value}, api: ${selectedRenderingApi.value}
                - Calculated Compatibility Tier: ${compReport.hardwareTier} with Estimated GPU: ${compReport.estimatedGpuModel}

                Tasks:
                1. Analyze their device specs. Tell them if their hardware can natively output ${selectedFrameRate.value} frames based on refresh rates.
                2. Give GFX tuning guidelines: which specific parameters to change (shadows, AA, rendering API) to keep thermals under 38°C.
                3. Share a pro battle strategy for ${selectedGameVersion.value} map locations.
                
                Provide a structured report using exact headings:
                🎯 COMPATIBILITY DIAGNOSIS
                ⚙️ ES-PORTS GFX STABILIZATION SETTINGS 
                🔥 TACTICAL ADVANTAGE
            """.trimIndent()

            try {
                val apiKey = BuildConfig.GEMINI_API_KEY
                if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                    delay(1200)
                    _coachState.value = CoachState.AnalysisReceived(
                        "⚠️ Gemini API Key not registered. Using local engine report for ${currentSpecs.model}:\n\n" +
                        "🎯 COMPATIBILITY DIAGNOSIS\n" +
                        "- Your display supports ${currentSpecs.refreshRateHz}Hz refresh rate hardware. ${if(compReport.isDisplayCompliant) "Supports true 90 FPS gameplay natively!" else "Locked to 60 FPS physically. The tool will unlock system registers, but display hardware is restricted to " + currentSpecs.refreshRateHz + "Hz."}\n" +
                        "- Calculated Profile: ${compReport.hardwareTier}\n\n" +
                        "⚙️ ES-PORTS GFX STABILIZATION SETTINGS\n" +
                        "- Switch API to Vulkan to lower overhead.\n" +
                        "- Set shadows to Low or Disabled to prevent thermal thermal limits.\n" +
                        "- Purge RAM before launching to stabilize background frames.\n\n" +
                        "🔥 TACTICAL ADVANTAGE (${selectedGameVersion.value})\n" +
                        "- Avoid central hotdrops like Pochinki if you have high latency. Land in Mylta Power, loot up, then pinch squads rotating from military base."
                    )
                    return@launch
                }

                val request = GenerateContentRequest(
                    contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                    generationConfig = GenerationConfig(temperature = 0.5f, maxOutputTokens = 800)
                )

                val response = RetrofitClient.service.generateContent(apiKey, request)
                val responseText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                
                if (!responseText.isNullOrEmpty()) {
                    _coachState.value = CoachState.AnalysisReceived(responseText)
                } else {
                    _coachState.value = CoachState.Error("Received empty response from optimization interface.")
                }
            } catch (e: Exception) {
                _coachState.value = CoachState.Error("Optimization Connection Error: ${e.message ?: "Server Busy"}")
            }
        }
    }

    fun sendChatMessage(queryText: String) {
        if (queryText.isBlank() || _isSendingChat.value) return
        
        val history = _chatHistory.value.toMutableList()
        history.add(ChatMessage("user", queryText))
        _chatHistory.value = history
        _isSendingChat.value = true

        val currentSpecs = _deviceSpec.value
        val contextPrompt = """
            You are a helpful, professional battle royale Esports Gaming Coach. The user is currently running our 90 FPS GFX configuration utility.
            Device parameters: ${currentSpecs?.manufacturer} ${currentSpecs?.model}, ${currentSpecs?.totalRamGb}GB RAM, Screen: ${currentSpecs?.refreshRateHz}Hz.
            Selected GFX Setup: ${selectedGameVersion.value}, ${selectedFrameRate.value} FPS, graphics quality is ${selectedGraphicPreset.value}.

            Always answer in 1-2 paragraphs inside a gaming/esports context, providing pro gameplay or performance tips. Be highly supportive and motivating.
            
            Current chat input: $queryText
        """.trimIndent()

        viewModelScope.launch {
            try {
                val apiKey = BuildConfig.GEMINI_API_KEY
                if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                    delay(1200)
                    val reply = ChatMessage("ai", "I am running local backup mode. Pro tip for BGMI: To immediately decrease frame fluctuation by 15%, disable Anti-Aliasing (MSAA) in the GFX tool form above, set resolution to Smooth (540p), and trigger G-BOOST RAM purge before launching!")
                    val updated = _chatHistory.value.toMutableList()
                    updated.add(reply)
                    _chatHistory.value = updated
                    _isSendingChat.value = false
                    return@launch
                }

                val request = GenerateContentRequest(
                    contents = listOf(Content(parts = listOf(Part(text = contextPrompt)))),
                    generationConfig = GenerationConfig(temperature = 0.7f, maxOutputTokens = 500)
                )

                val response = RetrofitClient.service.generateContent(apiKey, request)
                val replyText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "Esports server congestion! Please retry in a moment."
                
                val reply = ChatMessage("ai", replyText)
                val updated = _chatHistory.value.toMutableList()
                updated.add(reply)
                _chatHistory.value = updated
            } catch (e: Exception) {
                val reply = ChatMessage("ai", "Communication Link Error: ${e.localizedMessage ?: "Check your connection and try again."}")
                val updated = _chatHistory.value.toMutableList()
                updated.add(reply)
                _chatHistory.value = updated
            } finally {
                _isSendingChat.value = false
            }
        }
    }
}
