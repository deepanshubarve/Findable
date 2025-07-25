package com.example.findable.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.findable.R
import com.example.findable.navigation.Routes
import com.example.findable.viewmodel.AuthViewModel


@Composable
fun Login(navHostController: NavHostController,
          authViewModel: AuthViewModel = viewModel()
) {

    val uiState by authViewModel.uiState.collectAsState()
    val context = LocalContext.current



    LaunchedEffect(uiState.isAuthenticated,uiState.appUser) {
        if(uiState.isAuthenticated && uiState.appUser != null){
            Toast.makeText(
                context,
                "Welcome ${uiState.appUser?.displayName ?: "User"}",
                Toast.LENGTH_SHORT
            ).show()

            navHostController.navigate(Routes.Home.routes){
                popUpTo(navHostController.graph.startDestinationId){inclusive = true}
            }
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
            authViewModel.clearErrorMessage()
        }
    }

    Box(modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center){

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            Box(modifier = Modifier.height(80.dp))
            Image(
                painter = painterResource(id = R.drawable.boyspng),
                contentDescription = "upper logo",
                modifier = Modifier
                    .size(200.dp).align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),text = "Join the Community",
                style = TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Join our community of friendly folks discovering and sharing the latest products in tech",
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(54.dp))

            // Social Buttons
            SocialButton(
                text = "Connect with Google",
                icon = Icons.Default.Email,
                backgroundColor = Color.White,
                contentColor = Color.Black,
                onClick = {
                     if(context != null){
                         authViewModel.startGoogleSignIn(context)
                    }else{
                        Toast.makeText(context,"Can not start google sign In :invalid context",Toast.LENGTH_SHORT)
                     }

                },
                enable = !uiState.isLoading
            )
            Spacer(modifier = Modifier.height(14.dp))
            SocialButton(
                text = "Connect with Twitter",
                icon = Icons.Default.ThumbUp,
                backgroundColor = Color.White,
                contentColor = Color(0xFF1DA1F2),
                onClick = { /* Handle Twitter login */ }
            )
            Spacer(modifier = Modifier.height(14.dp))

            SocialButton(
                text = "Connect with Facebook",
                icon = Icons.Default.Home,
                backgroundColor = Color.White,
                contentColor = Color(0xFF4267B2),
                onClick = { /* Handle Facebook login */ }
            )
            Spacer(modifier = Modifier.height(14.dp))

            SocialButton(
                text = "Connect with LinkedIn",
                icon = Icons.Default.Favorite,
                backgroundColor = Color.White,
                contentColor = Color(0xFF0077B5),
                onClick = { /* Handle LinkedIn login */ }
            )


        }
        if(uiState.isLoading){
            CircularProgressIndicator()
        }

    }

}


@Composable
fun SocialButton(
    text: String,
    icon: ImageVector,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    enable : Boolean = true
) {
    ElevatedButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(50.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}

@Preview
@Composable
fun GetPreview(){
    //Login(nav)
}