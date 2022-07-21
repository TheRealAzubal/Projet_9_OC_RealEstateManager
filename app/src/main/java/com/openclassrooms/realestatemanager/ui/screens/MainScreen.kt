package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu

import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.ui.NewRealEstateActivity
import com.openclassrooms.realestatemanager.utils.Screen
import com.openclassrooms.realestatemanager.utils.WindowSize
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import com.openclassrooms.realestatemanager.viewmodels.UserViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
fun MainScreen(
    navControllerDrawer: NavController,
    auth: FirebaseAuth,
    userViewModel: UserViewModel,
    realEstateViewModel: RealEstateViewModel,
    windowSize: WindowSize
) {


    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(Screen.ListScreen, Screen.MapScreen)

    val navController = rememberNavController()
    val context = LocalContext.current


    val navControllerTwoPane = rememberNavController()



    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            DrawerScreen(
                drawerState,
                scope,
                navControllerDrawer,
                auth,
                userViewModel
            )
        },
        content = {

            if (windowSize == WindowSize.COMPACT ) {

                Scaffold(
                    content = { innerPadding ->

                        NavHost(
                            navController = navController,
                            startDestination = "listScreen"
                        ) {
                            composable(Screen.ListScreen.route) {
                                ListScreen(
                                    drawerState,
                                    scope,
                                    realEstateViewModel,
                                    innerPadding,
                                    navControllerDrawer,
                                    windowSize,
                                    navControllerTwoPane

                                )
                            }
                            composable(Screen.MapScreen.route) {
                                MapScreen(
                                    drawerState,
                                    scope,
                                    realEstateViewModel,
                                    navControllerDrawer,
                                    navControllerTwoPane,
                                    windowSize
                                )
                            }
                        }

                    },
                    bottomBar = {
                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = { Icon(item.icon, contentDescription = null) },
                                    label = { Text(item.title) },
                                    selected = selectedItem == index,
                                    onClick = {
                                        selectedItem = index
                                        navController.navigate(item.route)
                                    }
                                )
                            }
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { /* do something */
                                context.startActivity(
                                    Intent(context, NewRealEstateActivity::class.java)
                                )
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(15.dp))
                        ) {
                            Icon(Icons.Filled.Add, "Localized description")
                        }
                    }

                )

            } else if(windowSize == WindowSize.MEDIUM){
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (navigationRail, box) = createRefs()

                    NavigationRail(modifier = Modifier.constrainAs(navigationRail) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }.fillMaxWidth(0.10f)) {
                        NavigationRailItem(
                            icon = { Icon(Icons.Default.Menu, contentDescription = null) },
                            selected = selectedItem == 3,
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        )

                        items.forEachIndexed { index, item ->
                            NavigationRailItem(
                                icon = { Icon(item.icon, contentDescription = null) },
                                label = { Text(item.title) },
                                selected = selectedItem == index,
                                onClick = {
                                    selectedItem = index
                                    navController.navigate(item.route)
                                }
                            )
                        }
                    }

                    Scaffold(
                        modifier = Modifier
                            .constrainAs(box) {
                                top.linkTo(parent.top, margin = 0.dp)
                                start.linkTo(navigationRail.end, margin = 10.dp)
                                bottom.linkTo(parent.bottom, margin = 0.dp)
                                end.linkTo(parent.end, margin = 10.dp)
                            }
                            .fillMaxWidth(0.90f)
                            .fillMaxHeight(),
                        content = { innerPadding ->

                            NavHost(
                                navController = navController,
                                startDestination = "listScreen"
                            ) {
                                composable(Screen.ListScreen.route) {
                                    ListScreen(
                                        drawerState,
                                        scope,
                                        realEstateViewModel,
                                        innerPadding,
                                        navControllerDrawer,
                                        windowSize ,
                                        navControllerTwoPane
                                    )
                                }
                                composable(Screen.MapScreen.route) {
                                    MapScreen(
                                        drawerState,
                                        scope,
                                        realEstateViewModel,
                                        navControllerDrawer,
                                        navControllerTwoPane,
                                        windowSize
                                    )
                                }
                            }

                        },
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = { /* do something */
                                    context.startActivity(
                                        Intent(context, NewRealEstateActivity::class.java)
                                    )
                                },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(15.dp))
                            ) {
                                Icon(Icons.Filled.Add, "Localized description")
                            }
                        }

                    )














                }








            }
            else if (windowSize == WindowSize.EXPANDED){



                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (navigationRail, box,box2) = createRefs()

                    NavigationRail(modifier = Modifier.constrainAs(navigationRail) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    }) {
                        NavigationRailItem(
                            icon = { Icon(Icons.Default.Menu, contentDescription = null) },
                            selected = selectedItem == 3,
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        )

                        items.forEachIndexed { index, item ->
                            NavigationRailItem(
                                icon = { Icon(item.icon, contentDescription = null) },
                                label = { Text(item.title) },
                                selected = selectedItem == index,
                                onClick = {
                                    selectedItem = index
                                    navController.navigate(item.route)
                                }
                            )
                        }
                    }

                    Scaffold(
                        modifier = Modifier
                            .constrainAs(box) {
                                top.linkTo(parent.top, margin = 10.dp)
                                start.linkTo(navigationRail.end, margin = 10.dp)
                                bottom.linkTo(parent.bottom, margin = 10.dp)
                            }
                            .fillMaxWidth(0.35f)
                            .fillMaxHeight(0.95f)
                            .clip(RoundedCornerShape(15.dp)),
                        content = { innerPadding ->

                            NavHost(
                                navController = navController,
                                startDestination = "listScreen"
                            ) {
                                composable(Screen.ListScreen.route) {
                                    ListScreen(
                                        drawerState,
                                        scope,
                                        realEstateViewModel,
                                        innerPadding,
                                        navControllerDrawer,
                                        windowSize ,
                                        navControllerTwoPane
                                    )
                                }
                                composable(Screen.MapScreen.route) {
                                    MapScreen(
                                        drawerState,
                                        scope,
                                        realEstateViewModel,
                                        navControllerDrawer,
                                        navControllerTwoPane,
                                        windowSize
                                    )
                                }
                            }

                        },
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = { /* do something */
                                    context.startActivity(
                                        Intent(context, NewRealEstateActivity::class.java)
                                    )
                                },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(15.dp))
                            ) {
                                Icon(Icons.Filled.Add, "Localized description")
                            }
                        }

                    )



                        Box( modifier = Modifier
                            .constrainAs(box2) {
                                top.linkTo(parent.top, margin = 0.dp)
                                start.linkTo(box.end, margin = 0.dp)
                                bottom.linkTo(parent.bottom, margin = 0.dp)
                                end.linkTo(parent.end, margin = 0.dp)
                            }
                            .fillMaxWidth(0.55f)
                            .fillMaxHeight(0.95f)
                            .clip(RoundedCornerShape(15.dp))) {

                            NavHost(navController = navControllerTwoPane, startDestination = "start") {



                                composable("detailScreen/{item}",arguments = listOf(
                                    navArgument("item") {
                                        type = RealEstate.NavigationType
                                    }
                                )
                                ) { backStackEntry ->

                                    val item = backStackEntry.arguments?.getParcelable<RealEstate>("item")

                                    RealEstateDetailScreen(
                                        realEstateViewModel,
                                        item,
                                        navControllerDrawer,
                                        windowSize
                                    ) }
                                composable("start"){ Start()}
                            }



                        }










                    }




            }















        },

        )





}

@Composable
fun Start(){
    Text(text = "")
}
