package com.example.audiorecorder.main.domain

import android.content.Context
import com.example.audiorecorder.di.AudioFileName
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject

interface HandleFilesUseCase {
    fun getFileOutputStream(): FileOutputStream
    fun getFileInputStream(): FileInputStream
    fun getByteCount(): Long
}

class HandleFilesUseCaseImpl @Inject constructor(
    @ApplicationContext val context: Context,
    @AudioFileName val fileName: String
) : HandleFilesUseCase {
    override fun getFileOutputStream(): FileOutputStream {
        removeOldFile()
        return context.openFileOutput(fileName, Context.MODE_PRIVATE)
    }

    override fun getFileInputStream(): FileInputStream =
        FileInputStream(context.filesDir.absolutePath + "/" + fileName)

    override fun getByteCount(): Long {
        File(context.filesDir.absolutePath + "/" + fileName).apply {
            if(exists()) return length()
        }
        return 0L
    }

    private fun removeOldFile() {
        File(context.filesDir.absolutePath + "/" + fileName).apply {
            if(exists()) delete()
        }
    }
}