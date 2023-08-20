package com.example.audiorecorder.record.ui

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
import com.example.audiorecorder.record.domain.AudioState
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
        return binding.root
    }

    private fun observeState() = lifecycleScope.launch{
        viewModel.state.collect{ state ->
            when(state.audioState){
                is AudioState.Idle -> onIdle()
                is AudioState.RecordStarted -> onRecordStarted()
                is AudioState.RecordCompleted -> onRecordCompleted()
                is AudioState.PlaybackStarted -> onPlaybackStarted()
                is AudioState.PlaybackPaused -> onPlaybackPaused()
            }
        }
    }

    private fun onIdle() {
        binding.apply {
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
    }

    private fun onRecordStarted() {
        binding.apply {
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
    }

    private fun onRecordCompleted() {
        binding.apply {
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
    }

    private fun onPlaybackStarted() {
        binding.apply {
            root.setBackgroundResource(R.drawable.bg_greenish)
            timeTextView.text = "0.00"
            audioActionButton.setImageResource(R.drawable.ic_pause)
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

    private fun onPlaybackPaused() {
        binding.apply {
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
}