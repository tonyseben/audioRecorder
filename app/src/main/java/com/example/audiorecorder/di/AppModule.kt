package com.example.audiorecorder.di

import com.example.audiorecorder.main.domain.GetNextAudioUiStateUseCase
import com.example.audiorecorder.main.domain.GetNextAudioUiStateUseCaseImpl
import com.example.audiorecorder.audio.AudioPlayer
import com.example.audiorecorder.audio.AudioPlayerImpl
import com.example.audiorecorder.audio.AudioRecorder
import com.example.audiorecorder.audio.AudioRecorderImpl
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
        fun bindGetNextAudioUiStateUseCase(useCase: GetNextAudioUiStateUseCaseImpl): GetNextAudioUiStateUseCase
    }

}