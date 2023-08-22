package com.example.audiorecorder.audio

import android.content.Context
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.DataInputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

enum class PlayState {
    PLAY_STARTED,
    PLAY_PAUSED,
    PLAY_COMPLETED
}

interface AudioPlayer {
    suspend fun play(context: Context, config: AudioConfig): Flow<PlayState>
    fun pause()
    fun reset()
}

class AudioPlayerImpl @Inject constructor() : AudioPlayer {

    private var isPlaying = false
    private var readBytes = 0

    override suspend fun play(context: Context, config: AudioConfig): Flow<PlayState> = flow {

        val track = AudioTrack(
            AudioManager.STREAM_MUSIC,
            config.sampleRateHz,
            config.channel,
            config.encoding,
            config.playbackBufferSizeBytes,
            AudioTrack.MODE_STREAM
        )

        try {
            val inputStream =
                FileInputStream(context.filesDir.absolutePath + "/" + config.fileName)
            val dataInputStream = DataInputStream(inputStream)
            val buffer = ByteArray(config.playbackBufferSizeBytes)

            track.play()
            dataInputStream.skipBytes(readBytes)
            isPlaying = true
            emit(PlayState.PLAY_STARTED)

            var status: PlayState? = null
            while (isPlaying) {
                Log.d("TEST", "isPlaying:$isPlaying")
                val bytes = dataInputStream.read(buffer, 0, config.playbackBufferSizeBytes)
                if (bytes > -1) {
                    track.write(buffer, 0, bytes)
                    readBytes += bytes
                } else {
                    isPlaying = false
                    readBytes = 0
                    status = PlayState.PLAY_COMPLETED
                }
            }
            dataInputStream.close()
            inputStream.close()

            if (status == null) {
                status = PlayState.PLAY_PAUSED
            }
            emit(status)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            track.stop()
            track.release()
        }
    }

    override fun pause() {
        isPlaying = false
    }

    override fun reset() {
        pause()
        readBytes = 0
    }

}