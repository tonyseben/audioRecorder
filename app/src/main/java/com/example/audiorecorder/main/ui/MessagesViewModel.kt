package com.example.audiorecorder.main.ui

import android.media.AudioFormat
import androidx.lifecycle.viewModelScope
import com.example.audiorecorder.audio.AudioConfig
import com.example.audiorecorder.audio.AudioPlayer
import com.example.audiorecorder.audio.AudioRecorder
import com.example.audiorecorder.audio.PlayState
import com.example.audiorecorder.audio.RecordState
import com.example.audiorecorder.base.BaseViewModel
import com.example.audiorecorder.main.domain.AudioUiState
import com.example.audiorecorder.main.domain.GetNextAudioUiStateUseCase
import com.example.audiorecorder.main.domain.HandleFilesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val getNextAudioUiState: GetNextAudioUiStateUseCase,
    private val handleFiles: HandleFilesUseCase,
    private val audioRecorder: AudioRecorder,
    private val audioPlayer: AudioPlayer
) : BaseViewModel<
        MessagesContract.State,
        MessagesContract.Event,
        MessagesContract.SideEffect>(MessagesContract.State()) {

    override fun handleEvents(event: MessagesContract.Event) {
        when (event) {
            is MessagesContract.Event.OnAudioActionClicked -> handleAudioStates()
            is MessagesContract.Event.OnCancelClicked -> resetAudioAction()
            is MessagesContract.Event.OnDoneClicked -> resetAudioAction()
        }
    }

    private fun handleAudioStates() = viewModelScope.launch {
        when (getNextAudioUiState(state.value.audioState)) {
            is AudioUiState.Idle -> {}
            is AudioUiState.RecordStarted -> {
                val opStream = handleFiles.getFileOutputStream()
                audioRecorder.start(opStream, AudioConfig()).collect { status ->
                    val nxtState = when (status) {
                        RecordState.RECORD_STARTED -> AudioUiState.RecordStarted
                        RecordState.RECORD_COMPLETED -> AudioUiState.RecordCompleted
                    }
                    setState { copy(audioState = nxtState) }
                }
            }

            is AudioUiState.RecordCompleted -> {
                audioRecorder.stop()
            }

            is AudioUiState.PlaybackStarted -> {
                val ipStream = handleFiles.getFileInputStream()
                audioPlayer.play(ipStream, AudioConfig(channel = AudioFormat.CHANNEL_OUT_MONO))
                    .collect { status ->
                        val nxtState = when (status) {
                            PlayState.PLAY_STARTED -> AudioUiState.PlaybackStarted
                            PlayState.PLAY_PAUSED -> AudioUiState.PlaybackPaused
                            PlayState.PLAY_COMPLETED -> AudioUiState.RecordCompleted
                        }
                        setState { copy(audioState = nxtState) }
                    }
            }

            is AudioUiState.PlaybackPaused -> {
                audioPlayer.pause()
            }
        }
    }

    private fun resetAudioAction() {
        audioRecorder.reset()
        audioPlayer.reset()
        setState { copy(audioState = AudioUiState.Idle) }
    }
}