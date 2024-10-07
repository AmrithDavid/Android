package com.singularhealth.android3dicom.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface CacheManager {
    suspend fun clearCache(): Long

    suspend fun getCacheSize(): Long
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

                clearDirectory(context.cacheDir)
                context.externalCacheDir?.let { clearDirectory(it) }
                context.getExternalFilesDirs(null).forEach { it?.let { clearDirectory(it) } }

                val finalSize = getCacheSize()
                return@withContext initialSize - finalSize
            }

        override suspend fun getCacheSize(): Long =
            withContext(Dispatchers.IO) {
                var totalSize = context.cacheDir.calculateSize()
                context.externalCacheDir?.let { totalSize += it.calculateSize() }
                totalSize
            }

        private fun clearDirectory(dir: File) {
            if (dir.exists() && dir.isDirectory) {
                dir.listFiles()?.forEach { file ->
                    if (file.isDirectory) {
                        clearDirectory(file)
                    } else {
                        file.delete()
                    }
                }
            }
        }

        private fun File.calculateSize(): Long = walkTopDown().sumOf { it.length() }
    }
