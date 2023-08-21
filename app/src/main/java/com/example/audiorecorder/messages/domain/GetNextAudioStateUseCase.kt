package com.example.audiorecorder.messages.domain

import javax.inject.Inject

interface GetNextAudioStateUseCase {
    operator fun invoke(currentState: AudioState): AudioState
}

class GetNextAudioStateUseCaseImpl @Inject constructor() : GetNextAudioStateUseCase{
    override fun invoke(currentState: AudioState): AudioState {
        return when (currentState) {
            is AudioState.Idle -> AudioState.RecordStart
            is AudioState.RecordStart -> AudioState.RecordStop
            is AudioState.RecordStop -> AudioState.PlaybackStart
            is AudioState.PlaybackStart -> AudioState.PlaybackPause
            is AudioState.PlaybackPause -> AudioState.PlaybackStart
        }
    }
}

