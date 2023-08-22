package com.example.audiorecorder.main.domain

sealed class AudioUiState{
    object Idle: AudioUiState()
    object RecordStarted: AudioUiState()
    object RecordCompleted: AudioUiState()
    object PlaybackStarted: AudioUiState()
    object PlaybackPaused: AudioUiState()
    //object PlaybackCompleted: AudioState()
}
