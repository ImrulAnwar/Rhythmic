package com.example.rhythmic.di

import android.app.Application
import androidx.room.Room
import com.example.rhythmic.data.db.SongDatabase
import com.example.rhythmic.data.repo.SongRepoImpl
import com.example.rhythmic.domain.repo.SongRepository
import com.example.rhythmic.domain.util.UIFunctions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
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
        fun provideSongRepository(db: SongDatabase): SongRepository = SongRepoImpl(db.songDao)
}