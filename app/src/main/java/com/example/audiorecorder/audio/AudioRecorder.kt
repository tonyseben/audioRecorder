package com.example.audiorecorder.audio

import android.content.Context
import android.media.AudioRecord
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.IOException
import javax.inject.Inject


enum class RecordState {
    RECORD_STARTED,
    RECORD_COMPLETED
}

interface AudioRecorder {
    suspend fun start(context: Context, config: AudioConfig): Flow<RecordState>
    fun stop()
    fun reset()
}

class AudioRecorderImpl @Inject constructor() : AudioRecorder {

    private var isRecording = false

    @RequiresPermission(android.Manifest.permission.RECORD_AUDIO)
    override suspend fun start(context: Context, config: AudioConfig): Flow<RecordState> = flow {
        val record = AudioRecord(
            config.source,
            config.sampleRateHz,
            config.channel,
            config.encoding,
            config.recordBufferSizeBytes
        )

        if (record.state != AudioRecord.STATE_INITIALIZED) {
            throw IllegalStateException("Error initializing AudioRecord");
        }

        try {
            removeOldRecoding(context, config.fileName)
            val outputStream = context.openFileOutput(config.fileName, Context.MODE_PRIVATE)
            val buffer = ByteArray(config.recordBufferSizeBytes / 2)

            record.startRecording()
            isRecording = true
            emit(RecordState.RECORD_STARTED)

            while (isRecording) {
                Log.d("TEST", "isRecording:$isRecording")
                val bytes = record.read(buffer, 0, buffer.size)
                if (bytes > 0) {
                    outputStream?.write(buffer, 0, bytes)
                }
            }

            outputStream?.flush()
            outputStream?.close()
            emit(RecordState.RECORD_COMPLETED)

        } catch (e: IOException) {

        } finally {
            if (record.state != AudioRecord.STATE_UNINITIALIZED) {
                record.stop()
                record.release()
            }
        }
    }

    override fun stop() {
        isRecording = false
    }

    override fun reset() {
        stop()
    }

    private fun removeOldRecoding(context: Context, fileName: String) {
        val file = File(context.filesDir.absolutePath + "/" + fileName)
        if (file.exists()) file.delete()
    }

}