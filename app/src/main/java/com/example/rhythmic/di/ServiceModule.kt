package com.example.rhythmic.di

import android.media.AudioAttributes
import android.media.MediaPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent


@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

        @Provides
        fun provideMediaPlayer() = MediaPlayer().apply {
                setAudioAttributes(
                        AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                )
        }
}