package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.ui.screens.*
import com.openclassrooms.realestatemanager.ui.ui.theme.Projet_9_OC_RealEstateManagerTheme
import com.openclassrooms.realestatemanager.utils.rememberWindowSizeClass
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import com.openclassrooms.realestatemanager.viewmodels.UserViewModel

class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private val realEstateViewModel: RealEstateViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var startScreen : String

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            Projet_9_OC_RealEstateManagerTheme {
                val currentUser = auth.currentUser

                startScreen = if (currentUser == null) {
                    "signInScreen"
                } else {
                    "mainScreen"
                }

                val windowSize = rememberWindowSizeClass()


                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = startScreen) {
                    composable("mainScreen") {
                        MainScreen(
                            navControllerDrawer = navController,
                            auth = auth,
                            userViewModel,
                            realEstateViewModel,
                            windowSize
                        )
                    }
                    composable("settingsScreen") { SettingsScreen(navController = navController) }
                    composable("registerScreen") {
                        RegisterScreen(
                            navController = navController,
                            userViewModel = userViewModel
                        )
                    }
                    composable("signInScreen") {
                        SignInScreen(
                            navController = navController,
                            userViewModel = userViewModel
                        )
                    }
                    composable("filterScreen") { FilterScreen(navController) }
                    composable("editScreen/{item}",
                        arguments = listOf(
                            navArgument("item") {
                                type = RealEstate.NavigationType
                            }
                        )
                        ) { backStackEntry ->

                        val item = backStackEntry.arguments?.getParcelable<RealEstate>("item")

                        EditScreenRealEstate(
                            realEstateViewModel,
                            item,
                            navController
                        )
                    }


                    composable(
                        route = "detailScreen/{item}",
                        arguments = listOf(
                            navArgument("item") {
                                type = RealEstate.NavigationType
                            }
                        )
                    ) { backStackEntry ->
                        val item = backStackEntry.arguments?.getParcelable<RealEstate>("item")

                        RealEstateDetailScreen(
                            realEstateViewModel,
                            item,
                            navController,
                            windowSize
                        )
                    }


                }




            }
        }
    }
}




























