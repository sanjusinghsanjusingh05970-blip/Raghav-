package com.example.data

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import android.view.WindowManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.InetAddress
import kotlin.system.measureTimeMillis

data class DeviceSpec(
    val model: String,
    val manufacturer: String,
    val androidVersion: String,
    val cpuCores: Int,
    val totalRamGb: Double,
    val currentAvailableRamGb: Double,
    val refreshRateHz: Int,
    val batteryLevel: Int,
    val batteryTempCc: Double,
    val pingMs: Int,
    val isBgmiInstalled: Boolean,
    val isPubgGlobalInstalled: Boolean,
    val isPubgKrInstalled: Boolean
)

object SystemDiagnostics {

    suspend fun getDeviceSpecs(context: Context): DeviceSpec = withContext(Dispatchers.IO) {
        try {
            val pm = context.packageManager

            // Install checks
            val isBgmi = isPackageInstalled(pm, "com.pubg.imobile")
            val isPubgGlobal = isPackageInstalled(pm, "com.tencent.ig")
            val isPubgKr = isPackageInstalled(pm, "com.pubg.krmobile")

            // Memory usage
            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            var totalRam = 8.0
            var availRam = 4.0
            if (actManager != null) {
                try {
                    actManager.getMemoryInfo(memInfo)
                    totalRam = memInfo.totalMem.toDouble() / (1024 * 1024 * 1024)
                    availRam = memInfo.availMem.toDouble() / (1024 * 1024 * 1024)
                } catch (t: Throwable) {
                    // ignore
                }
            }

            // Battery specs
            var level = 85
            var tempCc = 32.4
            try {
                val batteryStatus: Intent? = context.registerReceiver(
                    null,
                    IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                )
                val rawLevel = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: 100
                level = if (rawLevel >= 0 && scale > 0) {
                    (rawLevel * 100 / scale.toFloat()).toInt()
                } else {
                    85
                }
                val rawTemp = batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
                tempCc = rawTemp.toDouble() / 10.0
                if (tempCc <= 0.0) tempCc = 32.4
            } catch (t: Throwable) {
                // ignore
            }

            // Refresh rate
            var refreshRate = 90
            try {
                val wm = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
                refreshRate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    context.display?.refreshRate?.toInt() ?: 90
                } else {
                    @Suppress("DEPRECATION")
                    wm?.defaultDisplay?.refreshRate?.toInt() ?: 90
                }
            } catch (t: Throwable) {
                // ignore
            }

            // Active network ping
            val ping = performPing()

            DeviceSpec(
                model = if (Build.MODEL.isNullOrEmpty()) "Global Esports Platform" else Build.MODEL,
                manufacturer = if (Build.MANUFACTURER.isNullOrEmpty()) "Esports Pro" else Build.MANUFACTURER.replaceFirstChar { it.uppercase() },
                androidVersion = if (Build.VERSION.RELEASE.isNullOrEmpty()) "14" else Build.VERSION.RELEASE,
                cpuCores = Runtime.getRuntime().availableProcessors().coerceAtLeast(4),
                totalRamGb = Math.round(totalRam * 10.0) / 10.0,
                currentAvailableRamGb = Math.round(availRam * 10.0) / 10.0,
                refreshRateHz = refreshRate,
                batteryLevel = level,
                batteryTempCc = tempCc,
                pingMs = ping,
                isBgmiInstalled = isBgmi,
                isPubgGlobalInstalled = isPubgGlobal,
                isPubgKrInstalled = isPubgKr
            )
        } catch (t: Throwable) {
            DeviceSpec(
                model = if (Build.MODEL.isNullOrEmpty()) "Global Esports Platform" else Build.MODEL,
                manufacturer = if (Build.MANUFACTURER.isNullOrEmpty()) "Esports Pro" else Build.MANUFACTURER.replaceFirstChar { it.uppercase() },
                androidVersion = if (Build.VERSION.RELEASE.isNullOrEmpty()) "14" else Build.VERSION.RELEASE,
                cpuCores = 8,
                totalRamGb = 8.0,
                currentAvailableRamGb = 4.5,
                refreshRateHz = 90,
                batteryLevel = 85,
                batteryTempCc = 32.4,
                pingMs = 28,
                isBgmiInstalled = false,
                isPubgGlobalInstalled = false,
                isPubgKrInstalled = false
            )
        }
    }

    private fun isPackageInstalled(pm: PackageManager, packageName: String): Boolean {
        return try {
            pm.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun performPing(): Int {
        return try {
            val startTime = System.currentTimeMillis()
            val ipAddress = InetAddress.getByName("8.8.8.8")
            if (ipAddress.isReachable(1000)) {
                val duration = System.currentTimeMillis() - startTime
                duration.toInt()
            } else {
                -1
            }
        } catch (e: Exception) {
            -1
        }
    }
}
