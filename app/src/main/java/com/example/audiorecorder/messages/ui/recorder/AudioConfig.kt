package com.example.audiorecorder.messages.ui.recorder

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder

data class AudioConfig(
    val source: Int = MediaRecorder.AudioSource.MIC,
    val sampleRateHz: Int = 16000,
    val channel: Int = AudioFormat.CHANNEL_IN_MONO,
    val encoding: Int = AudioFormat.ENCODING_PCM_16BIT,
    val fileName: String = "audio.pcm"
){
    val bufferSizeBytes = AudioRecord.getMinBufferSize(sampleRateHz, channel, encoding)
}
