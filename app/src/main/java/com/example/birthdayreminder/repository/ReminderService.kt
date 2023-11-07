package com.example.birthdayreminder.repository

import com.example.birthdayreminder.models.Person
import retrofit2.http.*
import retrofit2.Call
interface ReminderService {
    @GET("persons")
    fun getAllPersons(): Call<List<Person>>

    @GET("persons/{id}")
    fun getById(@Path("id") id: Int): Call<Person>

    @POST("persons")
    fun savePerson(@Body person: Person): Call<Person>

    @DELETE("persons/{id}")
    fun deletePerson(@Path("id") id: Int): Call<Person>

    @PUT("persons/{id}")
    fun updatePerson(@Path("id") id: Int, @Body person: Person): Call<Person>
}