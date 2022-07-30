package com.example.rhythmic.di

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.room.Room
import com.example.rhythmic.data.api.ShazamApi
import com.example.rhythmic.data.db.SongDatabase
import com.example.rhythmic.data.repo.SongRepository
import com.example.rhythmic.domain.repo.Repository
import com.example.rhythmic.domain.util.UIFunctions
import com.example.rhythmic.ui.activities.main_activity.fragments.bottom_nav.home.top_nav.album.AlbumFragment
import com.example.rhythmic.ui.activities.main_activity.fragments.bottom_nav.home.top_nav.artist.ArtistFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

        private const val BASE_URL = "https://shazam.p.rapidapi.com/"

        @Singleton
        @Provides
        fun provideUIFunctions() = UIFunctions()

        @Singleton
        @Provides
        fun provideSongDatabase(app: Application): SongDatabase = Room.databaseBuilder(
                app,
                SongDatabase::class.java,
                SongDatabase.DATABASE_NAME
        ).build()

        @Singleton
        @Provides
        fun provideSongRepository(db: SongDatabase): Repository = SongRepository(db.songDao)

        @Singleton
        @Provides
        fun provideMediaPlayer() = MediaPlayer().apply {
                setAudioAttributes(
                        AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                )
        }

        @Singleton
        @Provides
        fun provideArtistFragment() = ArtistFragment()

        @Singleton
        @Provides
        fun provideAlbumFragment() = AlbumFragment()

        @Singleton
        @Provides
        fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
                .apply {
                        level = HttpLoggingInterceptor.Level.BODY
                }

        @Singleton
        @Provides
        fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
                OkHttpClient
                        .Builder()
                        .addInterceptor(httpLoggingInterceptor)
                        .build()

        @Singleton
        @Provides
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build()

        @Singleton
        @Provides
        fun provideShazamAPi(retrofit: Retrofit): ShazamApi = retrofit.create(ShazamApi::class.java)
}