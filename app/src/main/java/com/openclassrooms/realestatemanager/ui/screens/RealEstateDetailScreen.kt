package com.openclassrooms.realestatemanager.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowColumn
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.utils.WindowSize
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealEstateDetailScreen(
    realEstateViewModel: RealEstateViewModel,
    itemRealEstate: RealEstate?,
    navController: NavController,
    windowSize: WindowSize,
    ) {

    if(itemRealEstate != null) {


        val listPhotos by realEstateViewModel.getRealEstatePhotosWithId(itemRealEstate.id.toString()).observeAsState()


        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {

                        val item = Uri.encode(Gson().toJson(itemRealEstate))

                        navController.navigate("editScreen/$item")


                              },
                    icon = { Icon(Icons.Filled.Edit, "Localized description") },
                    text = { Text(text = "Edit") },
                    modifier = Modifier.clip(RoundedCornerShape(15.dp))
                )
            },
            content = {

                ConstraintLayout(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxHeight()
                ) {

                    val (centerAlignedTopAppBar, textType, textPrice, textArea, textNumberRoom, textDescription, textAddress, textStatus, textDateOfEntry, textDateOfSale, textRealEstateAgent, lazyColumnPhoto, googleMap) = createRefs()

                    val (rowHospital, rowSchool, rowShops, rowParks) = createRefs()

                    CenterAlignedTopAppBar(
                        title = {
                            Text(text = "Estate Manager")
                        },
                        navigationIcon = {
                            if(windowSize == WindowSize.COMPACT || windowSize == WindowSize.MEDIUM) {
                                IconButton(onClick = {
                                    navController.popBackStack()
                                }) {
                                    Icon(Icons.Filled.ArrowBack, "")
                                }
                            }
                        },
                        modifier = Modifier.constrainAs(centerAlignedTopAppBar) {
                            top.linkTo(parent.top, margin = 0.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                        }

                    )

                    FlowColumn(modifier = Modifier.constrainAs(lazyColumnPhoto) {
                        top.linkTo(centerAlignedTopAppBar.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 25.dp)
                        end.linkTo(parent.end, margin = 25.dp)
                    }) {
                        repeat(listPhotos?.size ?: 0) {
                            Box(modifier = Modifier.size(184.dp).clip(RoundedCornerShape(15.dp))) {

                                Column {
                                    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                                        val (image, text) = createRefs()

                                        GlideImage(
                                            imageModel = listPhotos?.get(it)?.photoUrl,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.constrainAs(image) {
                                                top.linkTo(parent.top, margin = 0.dp)
                                                start.linkTo(parent.start, margin = 0.dp)
                                                end.linkTo(parent.end, margin = 0.dp)
                                            }
                                        )
                                        Text(
                                            text = listPhotos?.get(it)?.text ?: "",
                                            modifier = Modifier.constrainAs(text) {
                                                top.linkTo(image.bottom, margin = 0.dp)
                                                start.linkTo(parent.start, margin = 0.dp)
                                                end.linkTo(parent.end, margin = 0.dp)
                                            })

                                    }


                                }
                            }
                        }
                    }



                    Row(modifier = Modifier.constrainAs(textType) {
                        top.linkTo(lazyColumnPhoto.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }) {
                        Text(text = itemRealEstate.type.toString())
                    }

                    Row(modifier = Modifier.constrainAs(textPrice) {
                        top.linkTo(textType.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }) {
                        Text(text = itemRealEstate.price.toString())
                    }

                    Row(modifier = Modifier.constrainAs(textArea) {
                        top.linkTo(textPrice.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_crop_square_24),
                            contentDescription = ""
                        )
                        Text(text = itemRealEstate.area.toString())
                    }

                    Row(modifier = Modifier.constrainAs(textNumberRoom) {
                        top.linkTo(textArea.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_house_24),
                            contentDescription = ""
                        )
                        Text(text = itemRealEstate.numberRoom.toString())
                    }

                    Row(modifier = Modifier.constrainAs(textDescription) {
                        top.linkTo(textNumberRoom.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }) {
                        Text(text = itemRealEstate.description.toString())
                    }

                    Row(modifier = Modifier.constrainAs(textAddress) {
                        top.linkTo(textDescription.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }) {
                        Icon(Icons.Filled.LocationOn, contentDescription = "")
                        Text(text = itemRealEstate.numberAndStreet.toString())
                    }

                    Row(
                        modifier = Modifier
                            .constrainAs(rowHospital) {
                                top.linkTo(textAddress.bottom, margin = 5.dp)
                                start.linkTo(parent.start, margin = 50.dp)
                                end.linkTo(parent.end, margin = 50.dp)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,

                        ) {

                        Checkbox(
                            checked = itemRealEstate.hospitalsNear,
                            onCheckedChange = { itemRealEstate.hospitalsNear = it },
                        )
                        Text(text = "Near Hospital")
                    }

                    Row(
                        modifier = Modifier
                            .constrainAs(rowSchool) {
                                top.linkTo(rowHospital.bottom, margin = 5.dp)
                                start.linkTo(parent.start, margin = 50.dp)
                                end.linkTo(parent.end, margin = 50.dp)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,

                        ) {
                        Checkbox(
                            checked = itemRealEstate.schoolsNear,
                            onCheckedChange = { itemRealEstate.schoolsNear = it }
                        )
                        Text(text = "Near School")
                    }

                    Row(
                        modifier = Modifier
                            .constrainAs(rowShops) {
                                top.linkTo(rowSchool.bottom, margin = 5.dp)
                                start.linkTo(parent.start, margin = 50.dp)
                                end.linkTo(parent.end, margin = 50.dp)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Checkbox(
                            checked = itemRealEstate.shopsNear,
                            onCheckedChange = { itemRealEstate.shopsNear = it }
                        )
                        Text(text = "Near Shops")
                    }

                    Row(
                        modifier = Modifier
                            .constrainAs(rowParks) {
                                top.linkTo(rowShops.bottom, margin = 5.dp)
                                start.linkTo(parent.start, margin = 50.dp)
                                end.linkTo(parent.end, margin = 50.dp)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Checkbox(
                            checked = itemRealEstate.parksNear,
                            onCheckedChange = { itemRealEstate.parksNear = it }
                        )
                        Text(text = "Near Parks")
                    }

                    Row(modifier = Modifier.constrainAs(textStatus) {
                        top.linkTo(rowParks.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }) {
                        Text(text = itemRealEstate.status.toString())
                    }

                    Row(modifier = Modifier.constrainAs(textDateOfEntry) {
                        top.linkTo(textStatus.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }) {
                        Text(text = itemRealEstate.dateOfEntry.toString())
                    }

                    Row(modifier = Modifier.constrainAs(textDateOfSale) {
                        top.linkTo(textDateOfEntry.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }) {
                        Text(text = itemRealEstate.dateOfSale.toString())
                    }

                    Row(modifier = Modifier.constrainAs(textRealEstateAgent) {
                        top.linkTo(textDateOfSale.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }) {
                        Text(text = itemRealEstate.realEstateAgent.toString())
                    }


                    if (itemRealEstate.lat != null && itemRealEstate.lng != null) {

                        val latLng = LatLng(
                            itemRealEstate.lat!!, itemRealEstate.lng!!
                        )

                        val cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(latLng, 10f)
                        }



                        GoogleMap(cameraPositionState = cameraPositionState, modifier = Modifier
                            .constrainAs(googleMap) {
                                top.linkTo(textRealEstateAgent.bottom, margin = 5.dp)
                                start.linkTo(parent.start, margin = 5.dp)
                                end.linkTo(parent.end, margin = 5.dp)
                                bottom.linkTo(parent.bottom, margin = 75.dp)
                            }
                            .size(200.dp)
                            .clip(RoundedCornerShape(15.dp))
                        ) {
                            Marker(state = MarkerState(position = latLng)) {

                            }
                        }

                    }


                }
            }
        )

    }





}

