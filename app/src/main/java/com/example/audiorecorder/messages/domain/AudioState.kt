package com.example.audiorecorder.messages.domain

sealed class AudioState{
    object Idle: AudioState()
    object RecordStart: AudioState()
    object RecordStop: AudioState()
    object PlaybackStart: AudioState()
    object PlaybackPause: AudioState()
    //object PlaybackCompleted: AudioState()
}
