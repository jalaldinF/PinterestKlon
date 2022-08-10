package uz.example.apppinterest.networking.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uz.example.apppinterest.model.homephoto.HomePhotoItem
import uz.example.apppinterest.model.relatedcollection.SinglePhoto
import uz.example.apppinterest.model.search.ResponseSearch
import uz.example.apppinterest.model.topic.Topic
import uz.example.apppinterest.model.userprofile.User

@JvmSuppressWildcards
interface ApiService {

    @GET("photos?")
    fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<List<HomePhotoItem>>

    @GET("photos/{id}")
    fun getSelectedPhoto(@Path("id") id: String): Call<HomePhotoItem>

    @GET("search/photos?")
    fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<ResponseSearch>

    @GET("users/{username}")
    fun getUser(@Path("username") username: String): Call<User>

    @GET("photos/{id}")
    fun getImageToRelated(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<SinglePhoto>

    @GET("topics")
    fun getTopics(): Call<List<Topic>>

    @GET("topics/{id}/photos")
    fun getTopicPhotos(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<List<HomePhotoItem>>
}