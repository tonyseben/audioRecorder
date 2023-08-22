package com.example.audiorecorder.main.domain

import javax.inject.Inject

interface GetNextAudioUiStateUseCase {
    operator fun invoke(currentState: AudioUiState): AudioUiState
}

class GetNextAudioUiStateUseCaseImpl @Inject constructor() : GetNextAudioUiStateUseCase {
    override fun invoke(currentState: AudioUiState): AudioUiState {
        return when (currentState) {
            is AudioUiState.Idle -> AudioUiState.RecordStarted
            is AudioUiState.RecordStarted -> AudioUiState.RecordCompleted
            is AudioUiState.RecordCompleted -> AudioUiState.PlaybackStarted
            is AudioUiState.PlaybackStarted -> AudioUiState.PlaybackPaused
            is AudioUiState.PlaybackPaused -> AudioUiState.PlaybackStarted
        }
    }
}

