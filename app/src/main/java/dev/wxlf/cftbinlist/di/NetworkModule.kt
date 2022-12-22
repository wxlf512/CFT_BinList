package dev.wxlf.cftbinlist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.wxlf.cftbinlist.data.network.BINListApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideOkHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return httpLoggingInterceptor
    }

    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    fun provideGson() : GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideBINListAPI(okHttpClient: OkHttpClient, gson: GsonConverterFactory): BINListApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://lookup.binlist.net/")
            .client(okHttpClient)
            .addConverterFactory(gson)
            .build()
        return retrofit.create(BINListApi::class.java)
    }
}