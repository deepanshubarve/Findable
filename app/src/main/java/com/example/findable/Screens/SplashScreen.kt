package com.example.findable.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.findable.R
import com.example.findable.navigation.Routes
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navHostController: NavHostController){

    ConstraintLayout(modifier = Modifier.padding(5.dp)){

        val (image) = createRefs()

        Image(painter = painterResource(id = R.drawable.appicon),
            contentDescription = "App icon", modifier = Modifier.constrainAs(image){
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }.size(80.dp))

    }
    LaunchedEffect(true) {
        delay(3000)
        navHostController.navigate(Routes.Login.routes){
            popUpTo(navHostController.graph.startDestinationId)
            launchSingleTop = true

        }
    }
}



