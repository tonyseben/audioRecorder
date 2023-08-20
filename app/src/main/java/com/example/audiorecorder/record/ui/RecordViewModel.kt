package com.example.audiorecorder.record.ui

import com.example.audiorecorder.base.BaseViewModel
import com.example.audiorecorder.record.domain.AudioState
import com.example.audiorecorder.record.domain.GetNextAudioStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val getNextAudioStateUse: GetNextAudioStateUseCase
) : BaseViewModel<
        RecordContract.State,
        RecordContract.Event,
        RecordContract.SideEffect>(RecordContract.State()) {

    override fun handleEvents(event: RecordContract.Event) {
        when(event){
            is RecordContract.Event.OnAudioActionClicked -> handleAudioAction()
            is RecordContract.Event.OnCancelClicked -> resetAudioAction()
            is RecordContract.Event.OnDoneClicked -> resetAudioAction()
        }
    }

    private fun handleAudioAction(){
        val state = getNextAudioStateUse(state.value.audioState)
        setState { copy(audioState = state) }
    }

    private fun resetAudioAction(){
        setState { copy(audioState = AudioState.Idle) }
    }
}