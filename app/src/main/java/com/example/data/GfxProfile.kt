package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "gfx_profiles")
data class GfxProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val profileName: String,
    val gameVersion: String, // BGMI, PUBG Global, PUBG KR, etc.
    val frameRate: String,   // 30, 40, 60, 90, 120 FPS
    val resolution: String,  // Smooth, HD, FHD, Native
    val graphicsPreset: String, // Smooth, Balanced, HD, HDR, Ultra HD
    val antiAliasing: String,   // Disabled, 2x, 4x
    val shadows: String,     // Disabled, Low, Medium, High
    val renderingApi: String, // OpenGL, Vulkan, Default
    val colorStyle: String,  // Classic, Colorful, Realistic, Soft, Movie
    val isApplied: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Dao
interface GfxProfileDao {
    @Query("SELECT * FROM gfx_profiles ORDER BY createdAt DESC")
    fun getAllProfilesFlow(): Flow<List<GfxProfile>>

    @Query("SELECT * FROM gfx_profiles WHERE id = :id LIMIT 1")
    suspend fun getProfileById(id: Int): GfxProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: GfxProfile): Long

    @Query("UPDATE gfx_profiles SET isApplied = 0")
    suspend fun clearAppliedFlags()

    @Query("UPDATE gfx_profiles SET isApplied = 1 WHERE id = :id")
    suspend fun setProfileApplied(id: Int)

    @Delete
    suspend fun deleteProfile(profile: GfxProfile)

    @Query("SELECT COUNT(*) FROM gfx_profiles")
    suspend fun getCount(): Int
}

@Database(entities = [GfxProfile::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gfxProfileDao(): GfxProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gfx_tool_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
