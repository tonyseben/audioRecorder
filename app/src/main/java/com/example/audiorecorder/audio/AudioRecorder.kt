package com.example.audiorecorder.audio

import android.Manifest
import android.media.AudioRecord
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


sealed class RecordState {
    data class Recording(val sessionId: Int): RecordState()
    object Completed: RecordState()
}

interface AudioRecorder {
    suspend fun start(outputStream: FileOutputStream, config: AudioConfig): Flow<RecordState>
    fun stop()
    fun reset()
}

class AudioRecorderImpl @Inject constructor() : AudioRecorder {

    private var isRecording = false

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    override suspend fun start(
        outputStream: FileOutputStream,
        config: AudioConfig
    ): Flow<RecordState> = flow {
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
            val buffer = ByteArray(config.recordBufferSizeBytes / 2)

            record.startRecording()
            isRecording = true
            emit(RecordState.Recording(record.audioSessionId))

            while (isRecording) {
                Log.d("TEST", "isRecording:${record.audioSessionId}")
                val bytes = record.read(buffer, 0, buffer.size)
                if (bytes > 0) {
                    outputStream.write(buffer, 0, bytes)
                }
            }

            outputStream.flush()
            outputStream.close()
            emit(RecordState.Completed)

        } catch (e: IOException) {

        } finally {
            if (record.state != AudioRecord.STATE_UNINITIALIZED) {
                record.stop()
                record.release()
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun stop() {
        isRecording = false
    }

    override fun reset() {
        stop()
    }

}