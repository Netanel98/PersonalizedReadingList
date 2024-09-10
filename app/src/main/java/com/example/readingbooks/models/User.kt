package com.example.readingbooks.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
class User(uid: String, email: String, firstName: String, lastName: String) {
    @PrimaryKey
    @ColumnInfo(name = "uid")
    var uid: String

    @ColumnInfo(name = "email")
    private var email: String

    @ColumnInfo(name = "first_name")
    private var firstName: String

    @ColumnInfo(name = "last_name")
    private var lastName: String

    // Make sure to provide a constructor that Room can use if not using setters
    init {
        this.uid = uid
        this.email = email
        this.firstName = firstName
        this.lastName = lastName
    }

    // Getters and setters
    fun getUid(): String {
        return uid
    }

    fun setUid(uid: String) {
        this.uid = uid
    }

    fun getEmail(): String {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getFirstName(): String {
        return firstName
    }

    fun setFirstName(firstName: String) {
        this.firstName = firstName
    }

    fun getLastName(): String {
        return lastName
    }

    fun setLastName(lastName: String) {
        this.lastName = lastName
    }
}