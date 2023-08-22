package com.example.audiorecorder.main.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.audiorecorder.R
import com.example.audiorecorder.audio.AudioPlayer
import com.example.audiorecorder.audio.AudioRecorder
import com.example.audiorecorder.databinding.FragmentMessagesBinding
import com.example.audiorecorder.main.domain.AudioUiState
import com.vmadalin.easypermissions.EasyPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding
    private val viewModel by viewModels<MessagesViewModel>()

    @Inject
    internal lateinit var audioRecorder: AudioRecorder

    @Inject
    internal lateinit var audioPlayer: AudioPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        observeState()
        binding.initViews()
        return binding.root
    }

    private fun observeState() = lifecycleScope.launch {
        viewModel.state.collect { state ->
            when (state.audioState) {
                is AudioUiState.Idle -> binding.onIdle()
                is AudioUiState.RecordStarted -> binding.onRecordStart()
                is AudioUiState.RecordCompleted -> binding.onRecordComplete()
                is AudioUiState.PlaybackStarted -> binding.onPlaybackStart()
                is AudioUiState.PlaybackPaused -> binding.onPlaybackPause()
            }

            Log.d("TEST", "Progress: ${state.playbackProgress}")
            binding.recordProgress.progress = state.playbackProgress
        }
    }

    private fun FragmentMessagesBinding.initViews() {
        audioActionButton.setOnClickListener {
            if (hasAudioRecordPermission()) {
                viewModel.setEvent(MessagesContract.Event.OnAudioActionClicked)
            } else {
                requestAudioRecordPermission()
            }
        }
        cancelButton.setOnClickListener {
            viewModel.setEvent(MessagesContract.Event.OnCancelClicked)
        }
        doneButton.setOnClickListener {
            viewModel.setEvent(MessagesContract.Event.OnDoneClicked)
        }
    }

    private fun FragmentMessagesBinding.onIdle() {
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

    private fun FragmentMessagesBinding.onRecordStart() {
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

    private fun FragmentMessagesBinding.onRecordComplete() {
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

    private fun FragmentMessagesBinding.onPlaybackStart() {
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

    private fun FragmentMessagesBinding.onPlaybackPause() {
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

    private fun hasAudioRecordPermission(): Boolean = EasyPermissions.hasPermissions(
        requireContext(),
        Manifest.permission.RECORD_AUDIO
    )

    private fun requestAudioRecordPermission(){
        EasyPermissions.requestPermissions(
            this@MessagesFragment,
            getString(R.string.audioRecordPermissionRationale),
            123,
            Manifest.permission.RECORD_AUDIO
        )
    }
}