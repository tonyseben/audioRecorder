package com.example.audiorecorder.messages.ui.audio

import android.content.Context
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataInputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject


interface AudioPlayer {
    suspend fun play(context: Context, config: AudioConfig)
    fun pause()
}

class AudioPlayerImpl @Inject constructor()  : AudioPlayer {

    private var isPlaying = false
    private var readBytes = 0

    override suspend fun play(context: Context, config: AudioConfig) = withContext(Dispatchers.IO){

        val track = AudioTrack(
            AudioManager.STREAM_MUSIC,
            config.sampleRateHz,
            config.channel,
            config.encoding,
            config.playbackBufferSizeBytes,
            AudioTrack.MODE_STREAM
        )

        try {
            val inputStream = FileInputStream(context.filesDir.absolutePath + "/" + config.fileName)
            val dataInputStream = DataInputStream(inputStream)
            val buffer = ByteArray(config.playbackBufferSizeBytes)

            track.play()
            dataInputStream.skipBytes(readBytes)
            isPlaying = true

            while (isPlaying) {
                Log.d("TEST", "isPlaying:$isPlaying")
                val bytes = dataInputStream.read(buffer, 0, config.playbackBufferSizeBytes)
                if(bytes > -1) {
                    track.write(buffer, 0, bytes)
                    readBytes += bytes
                } else {
                    isPlaying = false
                }
            }
            dataInputStream.close()
            inputStream.close()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }finally {
            track.stop()
            track.release()
        }
    }

    override fun pause() {
        isPlaying = false
    }

}