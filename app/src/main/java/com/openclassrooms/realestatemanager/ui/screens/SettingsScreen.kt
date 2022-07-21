package com.openclassrooms.realestatemanager.ui.screens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController

@Composable
@ExperimentalMaterial3Api
fun SettingsScreen(navController: NavController) {
    Scaffold(
        content = {
            ConstraintLayout {
                val (centerAlignedTopAppBar,text) = createRefs()
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Settings")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(Icons.Filled.ArrowBack, "")
                        }
                    },
                    modifier = Modifier.constrainAs(centerAlignedTopAppBar) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
                )

                Text(text = "",modifier = Modifier.constrainAs(text) {
                    top.linkTo(centerAlignedTopAppBar.bottom, margin = 10.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                })
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* do something */ },
                modifier = Modifier.clip(RoundedCornerShape(15.dp)),
            ) {
                Icon(Icons.Filled.Favorite, "Localized description")
            }
        }
    )
}