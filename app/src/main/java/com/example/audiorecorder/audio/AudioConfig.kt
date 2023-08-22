package com.example.audiorecorder.audio

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder

data class AudioConfig(
    val source: Int = MediaRecorder.AudioSource.MIC,
    val sampleRateHz: Int = 16000,
    val channel: Int = AudioFormat.CHANNEL_IN_MONO,
    val encoding: Int = AudioFormat.ENCODING_PCM_16BIT
){
    val recordBufferSizeBytes = AudioRecord.getMinBufferSize(sampleRateHz, channel, encoding)
    val playbackBufferSizeBytes = AudioTrack.getMinBufferSize(sampleRateHz, channel, encoding)
}
