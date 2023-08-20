package com.example.audiorecorder.record.domain

sealed class AudioState{
    object Idle: AudioState()
    object RecordStarted: AudioState()
    object RecordCompleted: AudioState()
    object PlaybackStarted: AudioState()
    object PlaybackPaused: AudioState()
    //object PlaybackCompleted: AudioState()
}
