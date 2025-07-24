package com.example.findable.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.findable.Screens.HomeScreen
import com.example.findable.Screens.Login
import com.example.findable.Screens.SplashScreen


@Composable
fun NavGraph(navController: NavHostController){

    NavHost(navController = navController,
        startDestination = Routes.Splash.routes) {

        composable(Routes.Splash.routes){
            SplashScreen(navController)
        }

        composable(Routes.Login.routes){
            Login(navController)
        }

        composable(Routes.Home.routes){
            HomeScreen(navController)
        }

    }

}