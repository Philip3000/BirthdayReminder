package com.example.birthdayreminder.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.birthdayreminder.repository.PersonsRepository

class PersonViewModel : ViewModel() {
    private val repository = PersonsRepository()
    val personsLiveData: LiveData<List<Person>> = repository.personsLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData

    init {
        Log.d("viewmodelClass ","${personsLiveData.value}")
        reload()
        Log.d("viewmodelClass after reload ","${personsLiveData.value}")
    }

    fun reload() {
        //personsLiveData
        repository.getPersons()
        Log.d("viewmodelClass after getPersons ", repository.toString())

    }

    operator fun get(index: Int): Person? {
        return personsLiveData.value?.get(index)
    }

    fun add(person: Person) {
        repository.add(person)
    }

    fun delete(id: Int) {
        repository.delete(id)
    }

    fun update(person: Person) {
        repository.update(person)
    }

    fun sortByName() {
        repository.sortByName()
    }

    fun sortByNameDescending() {
        repository.sortByNameDescending()
    }

    fun sortByAge() {
        repository.sortByAge()
    }

    fun sortByAgeDescending() {
        repository.sortByAgeDescending()
    }
    fun sortByCountdown() {
        repository.sortByCountdown()
    }

    fun sortByCountdownDescending() {
        repository.sortByCountdownDescending()
    }

    fun filterByName(name: String) {
        repository.filterByName(name)
    }
    fun filterByAge(age: Int) {
        repository.filterByAge(age)
    }
}