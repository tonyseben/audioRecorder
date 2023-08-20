package com.example.audiorecorder.record.ui

import com.example.audiorecorder.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(

) : BaseViewModel<
        RecordContract.State,
        RecordContract.Event,
        RecordContract.SideEffect>(RecordContract.State()) {

    override fun handleEvents(event: RecordContract.Event) {

    }
}