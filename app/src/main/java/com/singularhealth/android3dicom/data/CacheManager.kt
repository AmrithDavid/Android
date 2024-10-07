package com.singularhealth.android3dicom.data

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface CacheManager {
    suspend fun clearCache()
}

@Singleton
class CacheManagerImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : CacheManager {
        override suspend fun clearCache() {
            withContext(Dispatchers.IO) {
                try {
                    val initialInternalCacheSize = context.cacheDir.calculateSize()
                    val initialExternalCacheSize = context.externalCacheDir?.calculateSize() ?: 0L

                    Log.d(TAG, "Initial internal cache size: ${initialInternalCacheSize.formatSize()}")
                    Log.d(TAG, "Initial external cache size: ${initialExternalCacheSize.formatSize()}")

                    // Clear internal cache
                    context.cacheDir.deleteRecursively()
                    Log.d(TAG, "Internal cache cleared")

                    // clear external cache if available
                    context.externalCacheDir?.deleteRecursively()
                    Log.d(TAG, "External cache cleared")

                    // clear app-specific external storage directories
                    context.getExternalFilesDirs(null).forEach {
                        it?.deleteRecursively()
                        Log.d(TAG, "Cleared external file dir: ${it?.path}")
                    }

                    val finalInternalCacheSize = context.cacheDir.calculateSize()
                    val finalExternalCacheSize = context.externalCacheDir?.calculateSize() ?: 0L

                    Log.d(TAG, "Final internal cache size: ${finalInternalCacheSize.formatSize()}")
                    Log.d(TAG, "Final external cache size: ${finalExternalCacheSize.formatSize()}")

                    val totalCleared =
                        (initialInternalCacheSize - finalInternalCacheSize) +
                            (initialExternalCacheSize - finalExternalCacheSize)
                    Log.d(TAG, "Total cache cleared: ${totalCleared.formatSize()}")
                } catch (e: Exception) {
                    Log.e(TAG, "Error clearing cache", e)
                    e.printStackTrace()
                }
            }
        }

        private fun File.calculateSize(): Long = walkTopDown().sumOf { it.length() }

        private fun Long.formatSize(): String {
            if (this < 1024) return "$this B"
            val z = (63 - java.lang.Long.numberOfLeadingZeros(this)) / 10
            return String.format("%.1f %sB", this.toDouble() / (1L shl (z * 10)), " KMGTPE"[z])
        }

        private fun File.deleteRecursively() {
            if (isDirectory) {
                listFiles()?.forEach { it.deleteRecursively() }
            }
            delete()
        }

        companion object {
            private const val TAG = "CacheManagerImpl"
        }
    }
