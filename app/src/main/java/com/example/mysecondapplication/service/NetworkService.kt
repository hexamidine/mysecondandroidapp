package com.example.mysecondapplication.service

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request

//private const val BASE_URL = "https://api.github.com/graphql"
//
//class NetworkService {
//
////    fun getApolloClient(): ApolloClient {
////        val okHttp = OkHttpClient
////            .Builder()
////            .build()
////
////        return ApolloClient.Builder()
////            .serverUrl(BASE_URL)
////            .okHttpClient(okHttp)
////            .build()
////    }
//
//    fun getApolloClientWithTokenInterceptor(token: String): ApolloClient {
//        val httpClient = OkHttpClient.Builder()
//            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
//                val original: Request = chain.request()
//
//                val builder: Request.Builder = original
//                    .newBuilder()
//                    .method(original.method, original.body)
//
//                builder.header("Authorization", "Bearer $token")
//                return@Interceptor chain.proceed(builder.build())
//            })
//            .build()
//
//        return ApolloClient.Builder()
//            .serverUrl(BASE_URL)
//            .okHttpClient(httpClient)
//            .build()
//    }
//
////    companion object {
////        private var mInstance: NetworkService? = null
////
////        fun getInstance(): NetworkService? {
////            if (mInstance == null) {
////                mInstance = NetworkService()
////            }
////            return mInstance
////        }
////    }
//}