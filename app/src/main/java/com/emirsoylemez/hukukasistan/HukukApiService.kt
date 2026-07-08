package com.emirsoylemez.hukukasistan

import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit
import retrofit2.http.Headers

// 1. Veri Modelleri
data class SoruRequest(
    val soru_metni: String
)

data class CevapResponse(
    val cevap: String,
    val kaynaklar: List<String>? = emptyList() // Null gelirse çökme, boş liste say
)

// 2. API Arayüzü
interface HukukApi {
    @Headers("ngrok-skip-browser-warning: 12345")
    @POST("soru-sor")
    suspend fun soruSor(@Body request: SoruRequest): CevapResponse
}

// 3. Daha Güçlü Retrofit İstemcisi
object RetrofitClient {
    private val BASE_URL = BuildConfig.BASE_URL

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("ngrok-skip-browser-warning", "true")
                .header("Accept", "application/json")
                .header("Connection", "close") // Bağlantının askıda kalıp kopmasını önlemek için

            chain.proceed(requestBuilder.build())
        }
        .connectTimeout(60, TimeUnit.SECONDS) // Süre 120 saniyeye çıkarıldı
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true) // Bağlantı hatalarında otomatik yeniden dene
        .protocols(listOf(Protocol.HTTP_1_1)) // unexpected end of stream için en stabil protokol
        .build()

    val instance: HukukApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HukukApi::class.java)
    }
}
