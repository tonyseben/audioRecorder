package com.example.audiorecorder.audio

import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.FileInputStream
import java.io.IOException
import javax.inject.Inject

enum class PlayState {
    PLAY_STARTED,
    PLAY_PAUSED,
    PLAY_COMPLETED
}

interface AudioPlayer {
    suspend fun play(inputStream: FileInputStream, config: AudioConfig): Flow<PlayState>
    fun pause()
    fun reset()
}

class AudioPlayerImpl @Inject constructor() : AudioPlayer {

    private var isPlaying = false
    private var readBytes = 0L

    override suspend fun play(inputStream: FileInputStream, config: AudioConfig): Flow<PlayState> = flow {

        val track = AudioTrack(
            AudioManager.STREAM_MUSIC,
            config.sampleRateHz,
            config.channel,
            config.encoding,
            config.playbackBufferSizeBytes,
            AudioTrack.MODE_STREAM
        )

        try {
            val buffer = ByteArray(config.playbackBufferSizeBytes)

            track.play()
            inputStream.skip(readBytes)
            isPlaying = true
            emit(PlayState.PLAY_STARTED)

            var status: PlayState? = null
            while (isPlaying) {
                Log.d("TEST", "isPlaying:$isPlaying")
                val bytes = inputStream.read(buffer, 0, config.playbackBufferSizeBytes)
                if (bytes > -1) {
                    track.write(buffer, 0, bytes)
                    readBytes += bytes
                } else {
                    isPlaying = false
                    readBytes = 0
                    status = PlayState.PLAY_COMPLETED
                }
            }
            inputStream.close()

            if (status == null) {
                status = PlayState.PLAY_PAUSED
            }
            emit(status)

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            track.stop()
            track.release()
        }
    }.flowOn(Dispatchers.IO)

    override fun pause() {
        isPlaying = false
    }

    override fun reset() {
        pause()
        readBytes = 0
    }

}