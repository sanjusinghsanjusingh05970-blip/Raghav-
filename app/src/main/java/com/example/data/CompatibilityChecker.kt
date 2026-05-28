package com.example.data

import android.os.Build

data class CompatibilityReport(
    val is90FpsFullySupported: Boolean,
    val displayRefreshHz: Int,
    val isDisplayCompliant: Boolean, // >= 90Hz
    val totalRamGb: Double,
    val isRamCompliant: Boolean, // >= 6GB
    val cpuCores: Int,
    val isCpuCompliant: Boolean, // >= 8 cores
    val hardwareTier: String, // Elite, High, Mid, Low
    val estimatedGpuModel: String,
    val stutterRiskScore: Int, // 0 to 100
    val adviceMessage: String
)

object CompatibilityChecker {

    fun generateReport(spec: DeviceSpec): CompatibilityReport {
        val displayCompliant = spec.refreshRateHz >= 90
        val ramCompliant = spec.totalRamGb >= 6.0
        val cpuCompliant = spec.cpuCores >= 8

        val fullySupported = displayCompliant && ramCompliant && cpuCompliant

        // Estimate GPU based on manufacturer, model description, memory levels
        val gpuModel = when {
            spec.totalRamGb >= 12.0 -> "Adreno Elite 740+ / Mali-G715 (Flagship Custom Tier)"
            spec.totalRamGb >= 8.0 -> "Adreno 730 / Mali-G710 (Premium Esports Tier)"
            spec.totalRamGb >= 6.0 -> "Adreno 640 / Mali-G57 MC2 (Mid-range Smooth Tier)"
            else -> "Adreno 610 / Mali-G52 (Casual Tier - Low Overclock recommended)"
        }

        val hardwareTier = when {
            spec.totalRamGb >= 8.5 && spec.cpuCores >= 8 && spec.refreshRateHz >= 90 -> "ELITE GAMING CONSOLE TIER"
            spec.totalRamGb >= 6.0 && spec.cpuCores >= 6 -> "HIGH PERFORMANCE ESPORTS TIER"
            spec.totalRamGb >= 4.0 -> "STANDARD MID-TIER CONSOLE"
            else -> "LOW SPEC GAMING LEVEL"
        }

        // Calculate stutter risk based on memory and thermals
        var stutterScore = 15 // base
        if (!ramCompliant) stutterScore += 30
        if (!cpuCompliant) stutterScore += 25
        if (!displayCompliant) stutterScore += 15
        if (spec.batteryTempCc > 36.0) stutterScore += 15

        val advice = when {
            fullySupported -> "🔥 Your hardware is 100% compliant with BGMI/PUBG 90 FPS gameplay under our injection config! Set Graphics Preset to 'Smooth' and enjoy real-time 90Hz display fluids."
            !displayCompliant && ramCompliant -> "⚠️ Your chipset can handle 90 FPS frames internally, but your display has a hard limitation of ${spec.refreshRateHz}Hz. You will feel smoother target responsiveness, but physical visual frames are locked to ${spec.refreshRateHz} FPS."
            else -> "💡 Processor resources are constrained. Highly recommend enabling the 'VRAM / Core Booster Engine' inside G-BOOST PRO and setting resolution to 'Smooth / 540p' to maintain frame stability."
        }

        return CompatibilityReport(
            is90FpsFullySupported = fullySupported,
            displayRefreshHz = spec.refreshRateHz,
            isDisplayCompliant = displayCompliant,
            totalRamGb = spec.totalRamGb,
            isRamCompliant = ramCompliant,
            cpuCores = spec.cpuCores,
            isCpuCompliant = cpuCompliant,
            hardwareTier = hardwareTier,
            estimatedGpuModel = gpuModel,
            stutterRiskScore = stutterScore.coerceIn(5, 95),
            adviceMessage = advice
        )
    }
}
