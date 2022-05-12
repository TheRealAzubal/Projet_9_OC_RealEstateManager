package com.openclassrooms.realestatemanager.ui.screens

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.realestatemanager.ui.NewRealEstateActivity
import com.openclassrooms.realestatemanager.utils.Screen

@Composable
@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
fun MainScreen(navControllerDrawer: NavController, auth: FirebaseAuth) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(
        Screen.ListScreen,
        Screen.MapScreen,
    )



    val navController = rememberNavController()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerScreen(drawerState,scope,navControllerDrawer,auth)
        },
        content = {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { /* do something */
                            context.startActivity(
                                Intent(context,
                                    NewRealEstateActivity::class.java)
                            )
                        },
                        modifier = Modifier.clip(RoundedCornerShape(15.dp))


                    ) {
                        Icon(Icons.Filled.Add, "Localized description")
                    }
                },
                bottomBar = {
                    NavigationBar {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                icon = { Icon(item.icon, contentDescription = null) },
                                label = { Text(item.title) },
                                selected = selectedItem == index,
                                onClick = { selectedItem = index
                                    navController.navigate(item.route)
                                }
                            )
                        }
                    }
                }
            ) {
                NavHost(navController = navController, startDestination = "listScreen") {
                    composable(Screen.ListScreen.route) { ListScreen(drawerState,scope) }
                    composable(Screen.MapScreen.route) { MapScreen(drawerState,scope) }
                }
            }

        },
    )
}