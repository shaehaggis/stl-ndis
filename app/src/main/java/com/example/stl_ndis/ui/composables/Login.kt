package com.example.stl_ndis.ui.composables

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stl_ndis.data.models.LoginCredentials
import com.example.stl_ndis.ui.state.LoginState
import com.example.stl_ndis.ui.viewmodels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    navigateToHome: () -> Unit,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier
){

    val username = loginViewModel.username.value
    val password = loginViewModel.password.value
    val loginState by loginViewModel.loadingState.collectAsState()
    val showToast = loginViewModel.showToast.collectAsState()

    if (showToast.value){
        Toast.makeText(LocalContext.current, "Logout Successful", Toast.LENGTH_SHORT).show()
        loginViewModel.hideToast()
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            fontSize = 64.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        TextField(
            value = username,
            onValueChange = { loginViewModel.setUsername(it) },
            placeholder = { Text(text = "Username") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = password,
            onValueChange = { loginViewModel.setPassword(it) },
            placeholder = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = { loginViewModel.login(LoginCredentials(username, password)) }) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(60.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val currentLoginState = loginState) {
                is LoginState.Idle -> {}
                is LoginState.Loading -> CircularProgressIndicator()
                is LoginState.Success -> navigateToHome()
                is LoginState.Error -> Text(
                    text = "Error logging in... ${currentLoginState.errorMessage}",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}