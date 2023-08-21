package com.example.audiorecorder.messages.ui.audio

import android.content.Context
import android.media.AudioManager
import android.media.AudioTrack
import java.io.DataInputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject


interface AudioPlayer {
    fun play(context: Context, config: AudioConfig)
    fun pause()
}

class AudioPlayerImpl @Inject constructor()  : AudioPlayer {
    override fun play(context: Context, config: AudioConfig) {

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
            var i = 0

            track.play()

            while (dataInputStream.read(buffer, 0, config.playbackBufferSizeBytes).also { i = it } > -1) {
                track.write(buffer, 0, i)
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

    }

}