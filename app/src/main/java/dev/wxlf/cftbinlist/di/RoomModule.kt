package dev.wxlf.cftbinlist.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.wxlf.cftbinlist.data.datasources.local.room.RequestsDatabase

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    fun provideRequestsDatabase(@ApplicationContext context: Context) : RequestsDatabase =
        Room.databaseBuilder(
            context,
            RequestsDatabase::class.java,
            "requests_database"
        ).build()
}