package com.example.audiorecorder.messages.ui.recorder

import android.content.Context
import android.media.AudioRecord
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

interface AudioRecorder {
    suspend fun start(activity: Context?, config: AudioConfig)
    fun stop()
}

class AudioRecorderImpl @Inject constructor() : AudioRecorder{

    private var isRecording = false

    @RequiresPermission(android.Manifest.permission.RECORD_AUDIO)
    override suspend fun start(context: Context?, config: AudioConfig) = withContext(Dispatchers.IO){
        if(isRecording) return@withContext

        val record = AudioRecord(
            config.source,
            config.sampleRateHz,
            config.channel,
            config.encoding,
            config.bufferSizeBytes
        )

        if (record.state != AudioRecord.STATE_INITIALIZED) {
            throw IllegalStateException("Error initializing AudioRecord");
        }

        try{
            val outputStream = context?.openFileOutput(config.fileName, Context.MODE_PRIVATE)
            val buffer = ByteArray(config.bufferSizeBytes / 2)

            record.startRecording()
            isRecording = true

            while (isRecording){
                val bytes = record.read(buffer, 0, buffer.size)
                if (bytes > 0) {
                    outputStream?.write(buffer, 0, bytes)
                }
            }

            outputStream?.flush()
            outputStream?.close()

        } catch (e: IOException){

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

}