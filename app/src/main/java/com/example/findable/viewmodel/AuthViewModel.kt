package com.example.findable.viewmodel

import android.app.Application
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.findable.Repository.AuthRepository
import com.example.findable.model.AuthModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
data class AuthUiState(
    val isLoading : Boolean = false,
    val isAuthenticated : Boolean = false,
    val appUser : AuthModel? = null,
    val errorMessage : String? = null,
)


class AuthViewModel(application: Application) : AndroidViewModel(application){
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState : StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val authRepository = AuthRepository()
    private val credentialManger = CredentialManager.create(application)

    val googleWebClintId = "360343145922-fh13s1abh4hm2s3moba587tfd5n1721c.apps.googleusercontent.com"
    

    init {
        checkCurrentUser()
    }

    fun checkCurrentUser(){
        val firebaseUser = authRepository.currentUser
        if(firebaseUser != null){
            _uiState.value = AuthUiState(
                isAuthenticated = true, appUser = authRepository.mapFirebaseUserToAppUser(firebaseUser)
            )
        }
    }

//    fun onSignInWithGoogleClick(){
//        _uiState.value = _uiState.value.copy(isLoading = true, requiredGoogleSignIn = true, errorMessage = null)
//    }

    fun startGoogleSignIn(activityContext : Context){
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val googleIdOption : GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(googleWebClintId)
                .build()

            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            try{
                val result : GetCredentialResponse = credentialManger.getCredential(
                    request = request,
                    context = activityContext
                )
                handleGoogleSignInCredential(result)
            }catch (e : GetCredentialException){
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Google Sign In is failed: ${e.errorMessage}"
                )
            }
        }
    }

    private fun handleGoogleSignInCredential(result: GetCredentialResponse){
        when(val credential = result.credential){
            is GoogleIdTokenCredential -> {
                val googleIdToken = credential.idToken
                signInWithFirebase(googleIdToken)
            }
            else -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Unexpected credential type obtained"
                )
            }
        }
    }

    private fun signInWithFirebase(idToken : String){
        viewModelScope.launch {

            val result = authRepository.signInWithGoogle(idToken)
            result.fold(
                onSuccess = { appUser ->
                    _uiState.value = AuthUiState(isAuthenticated = true,
                        appUser = appUser, isLoading = false)
                },
                onFailure = {exception ->
                    _uiState.value = AuthUiState(
                        errorMessage = "Fire SignIn failed ${exception.message}"
                    )
                }
            )
        }
    }

    fun clearErrorMessage(){
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }


    fun signOut(){
        authRepository.signOut()
        _uiState.value = AuthUiState()
    }
}