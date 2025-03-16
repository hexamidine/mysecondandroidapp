package com.example.mysecondapplication

import android.app.Application
import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import com.example.mysecondapplication.resolver.ContentResolverHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@HiltAndroidApp
class MySecondApp : Application()

private const val BASE_URL = "https://api.github.com/graphql"
private const val TOKEN = "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"

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun contentResolverHelper(@ApplicationContext context: Context) = ContentResolverHelper(context)
//    fun appDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
//        context,
//        AppDatabase::class.java, "myfirstdb"
//    ).build()

    @Singleton
    @Provides
    fun apolloClient(): ApolloClient {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original: Request = chain.request()

                val builder: Request.Builder = original
                    .newBuilder()
                    .method(original.method, original.body)

                builder.header("Authorization", "Bearer $TOKEN")
                return@Interceptor chain.proceed(builder.build())
            })
            .build()

        return ApolloClient.Builder()
            .serverUrl(BASE_URL)
            .okHttpClient(httpClient)
            .build()
    }
//    fun dao(db: AppDatabase) = db.gitHubRepositoryDao()
}
