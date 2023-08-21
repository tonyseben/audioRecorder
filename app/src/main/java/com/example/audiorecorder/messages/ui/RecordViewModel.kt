package com.example.audiorecorder.messages.ui

import android.content.Context
import android.media.AudioFormat
import androidx.lifecycle.viewModelScope
import com.example.audiorecorder.base.BaseViewModel
import com.example.audiorecorder.messages.domain.AudioState
import com.example.audiorecorder.messages.domain.GetNextAudioStateUseCase
import com.example.audiorecorder.messages.ui.audio.AudioConfig
import com.example.audiorecorder.messages.ui.audio.AudioPlayer
import com.example.audiorecorder.messages.ui.audio.AudioRecorder
import com.example.audiorecorder.messages.ui.audio.PlayState
import com.example.audiorecorder.messages.ui.audio.RecordState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getNextAudioState: GetNextAudioStateUseCase,
    private val audioRecorder: AudioRecorder,
    private val audioPlayer: AudioPlayer
) : BaseViewModel<
        RecordContract.State,
        RecordContract.Event,
        RecordContract.SideEffect>(RecordContract.State()) {

    override fun handleEvents(event: RecordContract.Event) {
        when (event) {
            is RecordContract.Event.OnAudioActionClicked -> handleAudioStates()
            is RecordContract.Event.OnCancelClicked -> resetAudioAction()
            is RecordContract.Event.OnDoneClicked -> resetAudioAction()
        }
    }

    private fun handleAudioStates() = viewModelScope.launch(Dispatchers.IO) {
        when (getNextAudioState(state.value.audioState)) {
            is AudioState.Idle -> {}
            is AudioState.RecordStarted -> {
                audioRecorder.start(context, AudioConfig()).collect { status ->
                    val nxtState = when (status) {
                        RecordState.RECORD_STARTED -> AudioState.RecordStarted
                        RecordState.RECORD_COMPLETED -> AudioState.RecordCompleted
                    }
                    setState { copy(audioState = nxtState) }
                }
            }

            is AudioState.RecordCompleted -> {
                audioRecorder.stop()
            }

            is AudioState.PlaybackStarted -> {
                audioPlayer.play(
                    context,
                    AudioConfig(channel = AudioFormat.CHANNEL_OUT_MONO)
                ).collect { status ->
                    val nxtState = when (status) {
                        PlayState.PLAY_STARTED -> AudioState.PlaybackStarted
                        PlayState.PLAY_PAUSED -> AudioState.PlaybackPaused
                        PlayState.PLAY_COMPLETED -> AudioState.RecordCompleted
                    }
                    setState { copy(audioState = nxtState) }
                }
            }

            is AudioState.PlaybackPaused -> {
                audioPlayer.pause()
            }
        }
    }

    private fun resetAudioAction() {
        audioRecorder.reset()
        audioPlayer.reset()
        setState { copy(audioState = AudioState.Idle) }
    }
}