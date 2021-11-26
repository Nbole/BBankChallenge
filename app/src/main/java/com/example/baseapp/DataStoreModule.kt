package com.example.baseapp

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataStoreModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext appContext: Context): SwarmDb = Room
        .databaseBuilder(appContext, SwarmDb::class.java, SwarmDb.DATABASE_NAME)
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor { chain ->
            var request = chain.request()
           /* request =  request.newBuilder().header(
                "Cache-Control",
                "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
            ).build()*/
            chain.proceed(request)
        }.build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(db: SwarmDb): MovieDao = db.movieDao()

    @Provides
    @Singleton
    fun provideSearchDao(db: SwarmDb): PreviewSearchDao = db.previewDao()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.themoviedb.org/3/movie/")
        .client(okHttpClient)
        .build()

    @Provides
    fun provideCharacterService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

    @Provides
    fun provideHttpRequest(): HttpRequest = HttpRequestImpl()

    @Provides
    fun provideSearchService(httpRequest: HttpRequest): SearchService =
        SearchServiceImpl(httpRequest)

    @Provides
    fun provideSearchDataSource(
        searchService: SearchService,
    ): SearchDataSource = SearchDataSourceImpl(searchService)

    @Provides
    fun provideMovieDataSource(
        movieService: MovieService,
    ): MovieDataSource = MovieDataSourceImpl(movieService)
}
