package com.ahmedapps.moviesapp.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.ahmedapps.moviesapp.data.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import android.content.Context
import android.content.SharedPreferences

private const val PREF_NAME = "user_session"
private const val PREF_IS_LOGGED_IN = "is_logged_in"
private const val PREF_USERNAME = "username"

class FirebaseRepository {
    private val database = FirebaseDatabase.getInstance().reference

    fun saveUser(userId: String, user: User, onComplete: (Boolean) -> Unit) {
        database.child("users").child(userId).setValue(user).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun getUser(username: String, onResult: (User?) -> Unit) {
        database.child("users").orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val user = userSnapshot.getValue(User::class.java)
                            onResult(user)
                        }
                    } else {
                        onResult(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onResult(null)
                }
            })
    }

    fun deleteUser(userId: String, onComplete: (Boolean) -> Unit) {
        database.child("users").child(userId).removeValue().addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }.addOnFailureListener {
            onComplete(false)
        }
    }

    fun updateUser(userId: String, updatedUser: User, onComplete: (Boolean) -> Unit) {
        database.child("users").child(userId).setValue(updatedUser).addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }.addOnFailureListener {
            onComplete(false)
        }
    }

    fun logout(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}
