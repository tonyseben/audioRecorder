package com.example.audiorecorder.record.ui

import com.example.audiorecorder.base.UiEvent
import com.example.audiorecorder.base.UiSideEffect
import com.example.audiorecorder.base.UiState
import com.example.audiorecorder.record.domain.AudioState

class RecordContract {

    sealed class Event: UiEvent{
        object OnAudioActionClicked: Event()
        object OnCancelClicked: Event()
        object OnDoneClicked: Event()
    }

    data class State(
        val audioState: AudioState = AudioState.Idle
    ): UiState

    sealed class SideEffect: UiSideEffect
}