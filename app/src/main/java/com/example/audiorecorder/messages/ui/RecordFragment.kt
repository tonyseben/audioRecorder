package com.example.audiorecorder.messages.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.audiorecorder.R
import com.example.audiorecorder.databinding.FragmentRecordBinding
import com.example.audiorecorder.messages.domain.AudioState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordFragment : Fragment() {

    private lateinit var binding: FragmentRecordBinding
    private val viewModel by viewModels<RecordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordBinding.inflate(inflater, container, false)
        observeState()
        binding.initViews()
        return binding.root
    }

    private fun observeState() = lifecycleScope.launch {
        viewModel.state.collect { state ->
            when (state.audioState) {
                is AudioState.Idle -> binding.onIdle()
                is AudioState.RecordStarted -> binding.onRecordStarted()
                is AudioState.RecordCompleted -> binding.onRecordCompleted()
                is AudioState.PlaybackStarted -> binding.onPlaybackStarted()
                is AudioState.PlaybackPaused -> binding.onPlaybackPaused()
            }
        }
    }

    private fun FragmentRecordBinding.initViews(){
        audioActionButton.setOnClickListener {
            viewModel.setEvent(RecordContract.Event.OnAudioActionClicked)
        }
        cancelButton.setOnClickListener {
            viewModel.setEvent(RecordContract.Event.OnCancelClicked)
        }
        doneButton.setOnClickListener {
            viewModel.setEvent(RecordContract.Event.OnDoneClicked)
        }
    }

    private fun FragmentRecordBinding.onIdle() {
        root.setBackgroundResource(R.drawable.bg_greenish)
        timeTextView.text = ""
        audioActionButton.setImageResource(R.drawable.ic_mic)
        audioActionButton.setBackgroundResource(R.drawable.btn_bg_red)
        recordProgress.setProgress(0, false)
        context?.let {
            recordProgress.setIndicatorColor(ContextCompat.getColor(it, R.color.appRed))
        }
        hintTextView.setText(R.string.hintStartRecord)
        cancelButton.isVisible = false
        doneButton.isVisible = false
    }

    private fun FragmentRecordBinding.onRecordStarted() {
        root.setBackgroundResource(R.drawable.bg_reddish)
        timeTextView.text = "0:00"
        audioActionButton.setImageResource(R.drawable.ic_stop)
        audioActionButton.setBackgroundResource(R.drawable.btn_bg_red)
        recordProgress.setProgress(0, false)
        context?.let {
            recordProgress.setIndicatorColor(ContextCompat.getColor(it, R.color.appRed))
        }
        hintTextView.setText(R.string.hintStopRecord)
        cancelButton.isVisible = true
        doneButton.isVisible = false
    }

    private fun FragmentRecordBinding.onRecordCompleted() {
        root.setBackgroundResource(R.drawable.bg_greenish)
        timeTextView.text = "1:00"
        audioActionButton.setImageResource(R.drawable.ic_play)
        audioActionButton.setBackgroundResource(R.drawable.btn_bg_green)
        recordProgress.setProgress(0, false)
        context?.let {
            recordProgress.setIndicatorColor(ContextCompat.getColor(it, R.color.appGreen))
        }
        hintTextView.setText(R.string.hintPlay)
        cancelButton.isVisible = false
        doneButton.isVisible = true
    }

    private fun FragmentRecordBinding.onPlaybackStarted() {
        root.setBackgroundResource(R.drawable.bg_greenish)
        timeTextView.text = "0.00"
        audioActionButton.setImageResource(R.drawable.ic_pause)
        audioActionButton.setBackgroundResource(R.drawable.btn_bg_green)
        recordProgress.setProgress(0, false)
        context?.let {
            recordProgress.setIndicatorColor(ContextCompat.getColor(it, R.color.appGreen))
        }
        hintTextView.text = ""
        cancelButton.isVisible = false
        doneButton.isVisible = true
    }

    private fun FragmentRecordBinding.onPlaybackPaused() {
        root.setBackgroundResource(R.drawable.bg_greenish)
        timeTextView.text = "0.00"
        audioActionButton.setImageResource(R.drawable.ic_play)
        audioActionButton.setBackgroundResource(R.drawable.btn_bg_green)
        recordProgress.setProgress(0, false)
        context?.let {
            recordProgress.setIndicatorColor(ContextCompat.getColor(it, R.color.appGreen))
        }
        hintTextView.setText(R.string.hintPlay)
        cancelButton.isVisible = false
        doneButton.isVisible = true
    }
}