package com.example.audiorecorder.main.ui

import com.example.audiorecorder.base.UiEvent
import com.example.audiorecorder.base.UiSideEffect
import com.example.audiorecorder.base.UiState
import com.example.audiorecorder.main.domain.AudioUiState

class MessagesContract {

    sealed class Event: UiEvent{
        object OnAudioActionClicked: Event()
        object OnCancelClicked: Event()
        object OnDoneClicked: Event()
    }

    data class State(
        val audioState: AudioUiState = AudioUiState.Idle,
        val audioSessionId: Int = 0,
        val playbackProgress: Int = 0
    ): UiState

    sealed class SideEffect: UiSideEffect
}