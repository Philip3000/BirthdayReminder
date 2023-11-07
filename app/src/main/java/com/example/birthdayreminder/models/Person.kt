package com.example.birthdayreminder.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.io.Serializable
import java.time.LocalDate
import java.time.Period
import java.util.Calendar

private var auth: FirebaseAuth = FirebaseAuth.getInstance()

val userMail: String = auth.currentUser?.email.toString()
data class Person(

    val id: Int, val userId: String, var name: String, var birthYear: Int, var birthMonth: Int, var birthDayOfMonth: Int,
    var remarks: String?, var pictureUrl: String?, var age: Int, var countdown: Int) : Serializable {
        constructor(id: Int, name: String, birthYear: Int, birthMonth: Int, birthDayOfMonth: Int,
            age: Int, countdown: Int) : this(id, userMail, name,
            birthYear, birthMonth, birthDayOfMonth, null, null, age, countdown)

    fun calculateDays() : Int {
        val currentDate = Calendar.getInstance()
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.YEAR, birthYear)
            set(Calendar.MONTH, birthMonth - 1) // Months in Calendar are 0-based
            set(Calendar.DAY_OF_MONTH, birthDayOfMonth)
        }

        // Calculate the time difference between today and the person's next birthday
        nextBirthday.add(Calendar.YEAR, currentDate.get(Calendar.YEAR) - birthYear)

        // If the birthday is in the future, calculate the difference
        if (nextBirthday.before(currentDate)) {
            nextBirthday.add(Calendar.YEAR, 1)
        }

        // Calculate the countdown in days
        val countdownInDay = (nextBirthday.timeInMillis - currentDate.timeInMillis) / (24 * 60 * 60 * 1000) // Convert milliseconds to days

        countdown = countdownInDay.toInt()
        return countdown
    }
    fun calculateAge(birthYear: Int, birthMonth: Int, birthDay: Int): Int {
        val today = LocalDate.now()
        val birthDate = LocalDate.of(birthYear, birthMonth, birthDay)
        return Period.between(birthDate, today).years
    }
}