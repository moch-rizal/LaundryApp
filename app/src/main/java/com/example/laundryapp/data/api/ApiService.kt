package com.example.laundryapp.data.api

import com.example.laundryapp.data.*
import retrofit2.http.*

interface ApiService {

    // --- AUTH ---
    @FormUrlEncoded
    @POST("api/register")
    suspend fun register(
        @Field("nama_lengkap") namaLengkap: String,
        @Field("email") email: String,
        @Field("no_telepon") noTelepon: String,
        @Field("password") password: String
    ): GeneralResponse

    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    // --- LAYANAN ---
    @GET("api/layanan")
    suspend fun getLayanan(): List<Layanan>

    // --- PESANAN ---
    @GET("api/pesanan")
    suspend fun getPesanan(@Header("Authorization") token: String): List<Pesanan>

    // Buat Pesanan
    @POST("api/pesanan")
    suspend fun createPesanan(
        @Header("Authorization") token: String,
        @Body requestBody: Map<String, @JvmSuppressWildcards Any> // Kirim data sebagai body JSON
    ): GeneralResponse

    // Batalkan Pesanan
    @PUT("api/pesanan/{id}")
    suspend fun cancelPesanan(
        @Header("Authorization") token: String,
        @Path("id") pesananId: Int
    ): GeneralResponse

    // Menghapus Pesanan
    @DELETE("api/pesanan/{id}")
    suspend fun deletePesanan(
        @Header("Authorization") token: String,
        @Path("id") pesananId: Int
    ): GeneralResponse
}
