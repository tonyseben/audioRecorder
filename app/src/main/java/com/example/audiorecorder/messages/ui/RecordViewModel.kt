package com.example.audiorecorder.messages.ui

import androidx.lifecycle.viewModelScope
import com.example.audiorecorder.base.BaseViewModel
import com.example.audiorecorder.messages.domain.AudioState
import com.example.audiorecorder.messages.domain.GetNextAudioStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val getNextAudioState: GetNextAudioStateUseCase
) : BaseViewModel<
        RecordContract.State,
        RecordContract.Event,
        RecordContract.SideEffect>(RecordContract.State()) {

    override fun handleEvents(event: RecordContract.Event) {
        when (event) {
            is RecordContract.Event.OnAudioActionClicked -> handleAudioAction()
            is RecordContract.Event.OnCancelClicked -> resetAudioAction()
            is RecordContract.Event.OnDoneClicked -> resetAudioAction()
        }
    }

    private fun handleAudioAction() = viewModelScope.launch(Dispatchers.Default) {
        val nextState = getNextAudioState(state.value.audioState)
        setState { copy(audioState = nextState) }
    }

    private fun resetAudioAction() {
        setState { copy(audioState = AudioState.Idle) }
    }
}