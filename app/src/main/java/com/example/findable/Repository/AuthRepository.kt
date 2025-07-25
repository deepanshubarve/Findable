package com.example.findable.Repository

import com.example.findable.model.AuthModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class AuthRepository{

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    val currentUser : FirebaseUser?
        get() = firebaseAuth.currentUser

    fun mapFirebaseUserToAppUser(firebaseUser: FirebaseUser?) : AuthModel?{
        return firebaseUser?.let {
            AuthModel(
                uid = it.uid,
                email = it.email,
                displayName = it.displayName
            )

        }
    }

    suspend fun signInWithGoogle(idToke : String) : Result<AuthModel>{
        return try {
            val credential = GoogleAuthProvider.getCredential(idToke, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                Result.success(mapFirebaseUserToAppUser(firebaseUser)!!) // Assume mapping is successful if user not null
            } else {
                Result.failure(Exception("Firebase user is null after sign-in."))
            }

        }catch (e : Exception){
            Result.failure(e)
        }
    }

    fun signOut(){
        firebaseAuth.signOut()
    }




}