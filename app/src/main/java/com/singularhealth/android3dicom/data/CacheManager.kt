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
    suspend fun clearCache(): Long

    suspend fun getCacheSize(): Long

    fun addTestCacheFiles()
}

@Singleton
class CacheManagerImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : CacheManager {
        override suspend fun clearCache(): Long =
            withContext(Dispatchers.IO) {
                val initialSize = getCacheSize()

                Log.d(TAG, "Logging directory contents before clearing cache")
                logDirectoryContents(context.cacheDir)
                logDirectoryContents(context.externalCacheDir ?: return@withContext 0)
                context.getExternalFilesDirs(null).forEach { logDirectoryContents(it) }

                try {
                    clearDirectory(context.cacheDir)
                    context.externalCacheDir?.let { clearDirectory(it) }
                    context.getExternalFilesDirs(null).forEach { it?.let { clearDirectory(it) } }
                } catch (e: Exception) {
                    Log.e(TAG, "Error clearing cache", e)
                }

                Log.d(TAG, "Logging directory contents after clearing cache")
                logDirectoryContents(context.cacheDir)
                logDirectoryContents(context.externalCacheDir ?: return@withContext 0)
                context.getExternalFilesDirs(null).forEach { logDirectoryContents(it) }

                val finalSize = getCacheSize()
                val clearedSize = initialSize - finalSize

                Log.d(TAG, "Initial cache size: ${initialSize.formatSize()}")
                Log.d(TAG, "Final cache size: ${finalSize.formatSize()}")
                Log.d(TAG, "Cleared cache size: ${clearedSize.formatSize()}")

                return@withContext clearedSize
            }

        override suspend fun getCacheSize(): Long =
            withContext(Dispatchers.IO) {
                var totalSize = context.cacheDir.calculateSize()
                context.externalCacheDir?.let { totalSize += it.calculateSize() }
                totalSize
            }

        override fun addTestCacheFiles() {
            val internalCacheFile = File(context.cacheDir, "test_internal_cache.txt")
            internalCacheFile.writeText("This is a test file in internal cache")

            context.externalCacheDir?.let { externalCacheDir ->
                val externalCacheFile = File(externalCacheDir, "test_external_cache.txt")
                externalCacheFile.writeText("This is a test file in external cache")
            }

            Log.d(TAG, "Test cache files added")
        }

        private fun clearDirectory(dir: File) {
            if (dir.exists() && dir.isDirectory) {
                Log.d(TAG, "Clearing directory: ${dir.path}")
                dir.listFiles()?.forEach { file ->
                    try {
                        if (file.isDirectory) {
                            clearDirectory(file)
                        } else {
                            val fileSize = file.length()
                            val deleted = file.delete()
                            Log.d(TAG, "Deleting ${file.path} (${fileSize.formatSize()}): ${if (deleted) "success" else "failed"}")
                        }
                    } catch (e: SecurityException) {
                        Log.e(TAG, "Security exception when trying to delete ${file.path}", e)
                    }
                }
            } else {
                Log.d(TAG, "Directory does not exist or is not a directory: ${dir.path}")
            }
        }

        private fun logDirectoryContents(dir: File) {
            if (dir.exists() && dir.isDirectory) {
                Log.d(TAG, "Contents of directory: ${dir.path}")
                dir.listFiles()?.forEach { file ->
                    Log.d(TAG, "  ${file.name}: ${file.length().formatSize()}")
                }
                if (dir.listFiles().isNullOrEmpty()) {
                    Log.d(TAG, "  Directory is empty")
                }
            } else {
                Log.d(TAG, "Directory does not exist or is not a directory: ${dir.path}")
            }
        }

        private fun File.calculateSize(): Long = walkTopDown().sumOf { it.length() }

        private fun Long.formatSize(): String {
            if (this < 1024) return "$this B"
            val units = listOf("B", "KB", "MB", "GB", "TB")
            val digitGroups = (Math.log10(this.toDouble()) / Math.log10(1024.0)).toInt()
            return String.format("%.1f %s", this / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
        }

        companion object {
            private const val TAG = "CacheManagerImpl"
        }
    }
