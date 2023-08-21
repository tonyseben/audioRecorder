package com.example.audiorecorder.messages.domain

import javax.inject.Inject

interface GetNextAudioStateUseCase {
    operator fun invoke(currentState: AudioState): AudioState
}

class GetNextAudioStateUseCaseImpl @Inject constructor() : GetNextAudioStateUseCase{
    override fun invoke(currentState: AudioState): AudioState {
        return when (currentState) {
            is AudioState.Idle -> AudioState.RecordStarted
            is AudioState.RecordStarted -> AudioState.RecordCompleted
            is AudioState.RecordCompleted -> AudioState.PlaybackStarted
            is AudioState.PlaybackStarted -> AudioState.PlaybackPaused
            is AudioState.PlaybackPaused -> AudioState.PlaybackStarted
        }
    }
}

