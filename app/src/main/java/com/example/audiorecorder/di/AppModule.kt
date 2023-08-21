package com.example.audiorecorder.di

import com.example.audiorecorder.messages.domain.GetNextAudioStateUseCase
import com.example.audiorecorder.messages.domain.GetNextAudioStateUseCaseImpl
import com.example.audiorecorder.messages.ui.audio.AudioPlayer
import com.example.audiorecorder.messages.ui.audio.AudioPlayerImpl
import com.example.audiorecorder.messages.ui.audio.AudioRecorder
import com.example.audiorecorder.messages.ui.audio.AudioRecorderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Module
    @InstallIn(SingletonComponent::class)
    interface AppModuleInt {

        @Binds
        @Singleton
        fun bindAudioRecorder(useCase: AudioRecorderImpl): AudioRecorder

        @Binds
        @Singleton
        fun bindAudioPlayer(useCase: AudioPlayerImpl): AudioPlayer

        @Binds
        @Singleton
        fun bindGetNextAudioStateUseCase(useCase: GetNextAudioStateUseCaseImpl): GetNextAudioStateUseCase
    }

}