package com.mycardsapplication.data.network

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient {

    companion object {

//        private const val BASE_URL = "https://api.mycards-saas.com:8282"
        private const val BASE_URL = "https://api.openai.com/v1/"

        private var retrofit: Retrofit? = null

        fun getClient(): Retrofit {
            if (retrofit == null) {

//                val client: OkHttpClient = OkHttpClient.Builder()
//                    .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
//                    .addInterceptor(Interceptor { chain ->
//                        val newRequest: Request = chain.request().newBuilder()
//                            .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJub25jZSI6ImNveHd1a3VSVjdWUWNRamVndWphaUdzRkJuQVIzOXRnRi03cDRVbGJiZ2MiLCJhbGciOiJSUzI1NiIsIng1dCI6IjJaUXBKM1VwYmpBWVhZR2FYRUpsOGxWMFRPSSIsImtpZCI6IjJaUXBKM1VwYmpBWVhZR2FYRUpsOGxWMFRPSSJ9.eyJhdWQiOiIwMDAwMDAwMy0wMDAwLTAwMDAtYzAwMC0wMDAwMDAwMDAwMDAiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC82MmQxZmFlZS02NGM0LTQwMzMtYjFmZC04MzdhNTE4ZmY4OTgvIiwiaWF0IjoxNjY5MTI5NzE0LCJuYmYiOjE2NjkxMjk3MTQsImV4cCI6MTY2OTEzNDQ4NiwiYWNjdCI6MCwiYWNyIjoiMSIsImFpbyI6IkFUUUF5LzhUQUFBQURyditrRE1HR1FvWXV5djZuY1AyM0VuaW0yTmJjN0Q3NFZNVW9Rb3E5OEhLaHc4bXoyTTFtQ2xIWUlRNkY2aUUiLCJhbXIiOlsicHdkIl0sImFwcF9kaXNwbGF5bmFtZSI6Im15Q2FyZHMgTGltaXRlZCIsImFwcGlkIjoiNGJlZmJjYjEtOTJhZi00YThjLTljNDYtZmI3YjdlNTEwMTNmIiwiYXBwaWRhY3IiOiIwIiwiZmFtaWx5X25hbWUiOiJTYXJmcmF6IiwiZ2l2ZW5fbmFtZSI6Ik11aGFtbWFkIiwiaWR0eXAiOiJ1c2VyIiwiaXBhZGRyIjoiMTAzLjExMy4xMTEuMTU4IiwibmFtZSI6Ik11aGFtbWFkIFNhcmZyYXoiLCJvaWQiOiIyZTliM2ZhYy1hMGExLTRkYmMtYjk4Yi0xNDgyOWQwNjcxOGIiLCJwbGF0ZiI6IjEiLCJwdWlkIjoiMTAwMzdGRkVBMTA3NkE0NiIsInJoIjoiMC5BU0FBN3ZyUllzUmtNMEN4X1lONlVZXzRtQU1BQUFBQUFBQUF3QUFBQUFBQUFBQWdBQ2MuIiwic2NwIjoiQ2FsZW5kYXJzLlJlYWRXcml0ZSBDb250YWN0cy5SZWFkIENvbnRhY3RzLlJlYWQuU2hhcmVkIENvbnRhY3RzLlJlYWRXcml0ZSBGaWxlcy5SZWFkIEZpbGVzLlJlYWRXcml0ZSBGaWxlcy5SZWFkV3JpdGUuQWxsIE1haWwuUmVhZCBNYWlsYm94U2V0dGluZ3MuUmVhZCBOb3Rlcy5SZWFkIE5vdGVzLlJlYWRXcml0ZS5BbGwgb3BlbmlkIHByb2ZpbGUgVXNlci5SZWFkIFVzZXIuUmVhZC5BbGwgVXNlci5SZWFkQmFzaWMuQWxsIFVzZXIuUmVhZFdyaXRlIFVzZXIuUmVhZFdyaXRlLkFsbCBlbWFpbCIsInNpZ25pbl9zdGF0ZSI6WyJrbXNpIl0sInN1YiI6IkNfN2tMTFlJY192VU5oelFlZ3YtQjBVeFBVTnJkRzBIQU1NMFBtX1hKbjAiLCJ0ZW5hbnRfcmVnaW9uX3Njb3BlIjoiRVUiLCJ0aWQiOiI2MmQxZmFlZS02NGM0LTQwMzMtYjFmZC04MzdhNTE4ZmY4OTgiLCJ1bmlxdWVfbmFtZSI6InNhcmZyYXpAbXljYXJkcy1zYWFzLmNvbSIsInVwbiI6InNhcmZyYXpAbXljYXJkcy1zYWFzLmNvbSIsInV0aSI6IjNNdDEwajVpZ0VDLTM2bEF5TkZMQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCIsImI3OWZiZjRkLTNlZjktNDY4OS04MTQzLTc2YjE5NGU4NTUwOSJdLCJ4bXNfc3QiOnsic3ViIjoiYXozVHVSWVNVTmkzb0NDU05lNHl4dVJMUDFmamxCekZjSWRmNnY0aEZPTSJ9LCJ4bXNfdGNkdCI6MTQ5MzIyNTI4MX0.kuO7KTYx7SiyoaBbu7hDcTbjvL_rnD6UjMdGq3tXw7TejC88StR4cMTkl6R_lbN6KelN0_gHfjso_Gw5SV-ISR2mS7UuS0YYBCMjrhIKhpPYxZncyjpJbOK9xT31XF559-h7S8e6SM9kS_CJfZhHl1Wq6PdRKhlumB4bmE_g797eb_CnKOvRJeTLUSruVaa83tK0WgnFfcr0pMl-U3uWpMmE6YS8NxnuLr2qnEtpqo3baNH_HlsX4xuV-9nw0vSGWS_GDVsynAvlsHuUL8QYDmLhpvyi8KOUo9K3PVRXinR3Iw-QP9TL8pTQfXAbPdbERiNxaAJjP6NlKPfTFsed7A")
//                            .build()
//                        chain.proceed(newRequest)
//                    })
//                    .connectTimeout(50, TimeUnit.SECONDS)
//                    .readTimeout(50, TimeUnit.SECONDS).build()
                val certPinner: CertificatePinner = CertificatePinner.Builder().add("api.mycards-saas.com", "sha256/XpVSpaHEONUSDA+q+7bvzY8aUCNerajE0ePA8FQnQdw=")
                    .add("api.mycards-saas.com", "sha256/48hXNwn3laJAzsrIBprOcewUb097BGNL7e+MVM7Rcis=").build()

                val client: OkHttpClient = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                    .connectTimeout(150, TimeUnit.SECONDS)
                    .readTimeout(150, TimeUnit.SECONDS)

                    .build()
//                    .certificatePinner(certPinner)
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            }
            return retrofit!!
        }
    }

}