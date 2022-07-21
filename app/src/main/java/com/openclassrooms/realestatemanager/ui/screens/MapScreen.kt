package com.openclassrooms.realestatemanager.ui.screens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.*
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.utils.WindowSize

import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    realEstateViewModel: RealEstateViewModel,
    navControllerDrawer: NavController,
    navControllerTwoPane: NavHostController,
    windowSize: WindowSize
) {
    val navController = rememberNavController()
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val activity = LocalContext.current as Activity
    val context = LocalContext.current

    var userPosition by remember {
        mutableStateOf(LatLng(0.0, 0.0))
    }

    val items: List<RealEstate> by realEstateViewModel.getRealEstates.observeAsState(listOf())

    fun startLocationUpdates() {
        fusedLocationProviderClient = getFusedLocationProviderClient(activity)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {

            if (it != null) {
                userPosition = LatLng(it.latitude, it.longitude)
            }

        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            if (it) {
                startLocationUpdates()

            } else {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
            }
        },
    )

    when {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED -> {
            startLocationUpdates()


        }
        ActivityCompat.shouldShowRequestPermissionRationale(activity, "") -> {}
        else -> {
            SideEffect {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }

        }
    }

    ConstraintLayout {
        val (centerAlignedTopAppBar, map) = createRefs()





                    CenterAlignedTopAppBar(
                        modifier = Modifier.constrainAs(centerAlignedTopAppBar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                        title = {
                            Text(text = "Map")
                        },
                        navigationIcon = {
                            if(windowSize == WindowSize.COMPACT) {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(Icons.Filled.Menu, "")
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                navControllerDrawer.navigate("filterScreen")

                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_baseline_filter_list_24),
                                    contentDescription = ""
                                )
                            }
                        },

                        )

        if (userPosition != LatLng(0.0, 0.0)) {

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(userPosition, 10f)
            }

            val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
            val uiSettings by remember { mutableStateOf(MapUiSettings(myLocationButtonEnabled = true)) }

            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(map) {
                        top.linkTo(centerAlignedTopAppBar.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = uiSettings,
            ) {
                items.forEach {

                    val realEstate: RealEstate = it

                    if (it.lat != null && it.lng != null) {

                        val latLng = LatLng(
                            it.lat!!, it.lng!!
                        )
                        Marker(
                            state = MarkerState(position = latLng),
                            title = "title",
                            onInfoWindowClick = {
                                val item = Uri.encode(Gson().toJson(realEstate))

                                navControllerTwoPane.navigate("detailScreen/$item")
                            }
                        )
                    }
                }
            }

        } else {
            Text(text = "Impossible de recupérer la localisation des services google play verifier que une localisation a été enregistré dans ceux ci ")
        }
    }

}




