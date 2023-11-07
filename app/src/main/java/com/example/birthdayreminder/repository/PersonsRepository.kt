package com.example.birthdayreminder.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.birthdayreminder.models.Person
import com.example.birthdayreminder.models.PersonViewModel
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PersonsRepository {
    private val baseUrl = "https://birthdaysrest.azurewebsites.net/api/"
    private val reminder: ReminderService
    private var originalList: List<Person> = emptyList()
    private var filteredList: List<Person> = emptyList()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val personsLiveData: MutableLiveData<List<Person>> = MutableLiveData<List<Person>>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()) // GSON
            .build()
        reminder = build.create(ReminderService::class.java)
        getPersons()
    }

    val currentUser = auth.currentUser
    fun getPersons() {

        reminder.getAllPersons().enqueue(object : Callback<List<Person>> {
            override fun onResponse(call: Call<List<Person>>, response: Response<List<Person>>) {
                if (response.isSuccessful) {
                    //Log.d("APPLE", response.body().toString())
                    val b: MutableList<Person>? = response.body()?.toMutableList()
                    if (b != null) {
                        val iterator = b.iterator()
                        while (iterator.hasNext()) {
                            val person = iterator.next()
                            if (person.userId != currentUser?.email.toString()) {
                                iterator.remove() // Remove element through the iterator
                            } else {
                                person.countdown = person.calculateDays()
                                person.age = person.calculateAge(person.birthYear, person.birthMonth, person.birthDayOfMonth)
                            }
                        }
                        originalList = b
                    }
                    personsLiveData.postValue(b!!)
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                }
            }

            override fun onFailure(call: Call<List<Person>>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
            }
        })
    }

    fun add(person: Person) {
        reminder.savePerson(person).enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if (response.isSuccessful) {
                    person.calculateDays()
                    updateMessageLiveData.postValue("Added: " + response.body())
                    getPersons()
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
            }
        })
    }

    fun delete(id: Int) {
        reminder.deletePerson(id).enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if (response.isSuccessful) {
                    updateMessageLiveData.postValue("Deleted: " + response.body())
                    getPersons()
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
            }
        })
    }

    fun update(person: Person) {
        Log.d("WaterBottle", "updating person")
        reminder.updatePerson(person.id, person).enqueue(object : Callback<Person> {
            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                Log.d("WaterBottle", "Next step")
                if (response.isSuccessful) {
                    updateMessageLiveData.postValue("Updated: " + response.body())
                    Log.d("Bottle", "${response.body()}")
                    getPersons()
                } else {
                    val message = response.code().toString() + " " + response.message()
                    Log.d("Bottle", "$message")
                    errorMessageLiveData.postValue(message)
                }
            }

            override fun onFailure(call: Call<Person>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("WaterBottle", "OnFailure ${t.message}")
            }
        })
    }

    fun sortByName() {
        Log.d("SortByName before filter", "observer ${personsLiveData.value}")
        personsLiveData.value = personsLiveData.value?.sortedBy { it.name }
        Log.d("Namelist", "observer ${personsLiveData.value.toString()}")
        Log.d("SortByName after filter", "observer ${personsLiveData.value}")
    }

    fun sortByNameDescending() {
        Log.d("SortByName Descending before filter", "observer ${personsLiveData.value}")
        personsLiveData.value = personsLiveData.value?.sortedByDescending { it.name }
        Log.d("SortByName Descending after filter", "observer ${personsLiveData.value}")

    }

    fun sortByAge() {
        personsLiveData.value = personsLiveData.value?.sortedBy { it.age }
        Log.d("SortByAge", "observer ${personsLiveData.value}")

    }

    fun sortByAgeDescending() {
        personsLiveData.value = personsLiveData.value?.sortedByDescending { it.age }
        Log.d("SortByAge Desc", "observer ${personsLiveData.value} ")
    }

    fun sortByCountdown() {
        personsLiveData.value = personsLiveData.value?.sortedBy { it.countdown }
        Log.d("SortCountdown", "observer ${personsLiveData.value} ")
    }

    fun sortByCountdownDescending() {
        personsLiveData.value = personsLiveData.value?.sortedByDescending { it.countdown }
        Log.d("SortCountdown After", "observer ${personsLiveData.value} ")

    }


    fun filterByName(name: String) {
        if (name.isBlank()) {
            getPersons()
        }else {
            filteredList = originalList
            personsLiveData.value = filteredList.filter { person -> person.name.contains(name) }
        }
    }
    fun filterByAge(age: Int) {
        if (age < 0) {
            getPersons()
        }else {
            filteredList = originalList
            personsLiveData.value = filteredList.filter { person -> person.age == age }
        }
    }
}
