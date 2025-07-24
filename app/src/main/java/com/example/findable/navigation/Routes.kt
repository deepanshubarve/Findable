package com.example.findable.navigation

sealed class Routes(val routes: String){
    data object Home : Routes("home")
    data object Discover : Routes("discover")
    data object Profile : Routes("profile")
    data object Splash : Routes("splash")
    data object BottomNav : Routes("Bottom_Nav")
    data object Login : Routes("login")
    data object Register : Routes("register")
    data object News : Routes("news")
    data object Activity : Routes("activity")
    data object Settings : Routes("settings")

}
