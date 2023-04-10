package com.example.heartrate_monitor.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.heartrate_monitor.service.HeartrateService
import com.example.heartrate_monitor.ui.theme.HeartratemonitorTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("InlinedApi") // disables whining about notifications requiring app level 33
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent() {
            val context = LocalContext.current
            val btPermissionState = rememberMultiplePermissionsState(
                listOf(
                    Manifest.permission.FOREGROUND_SERVICE, Manifest.permission.POST_NOTIFICATIONS
                )
            )

            if (!btPermissionState.allPermissionsGranted) {
                LaunchedEffect(key1 = btPermissionState, block = {
                    Toast.makeText(
                        context,
                        "Notification and background permissions are required for recording exercises",
                        Toast.LENGTH_LONG
                    ).show()
                    btPermissionState.launchMultiplePermissionRequest()
                })
            }

            HeartratemonitorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    Card(
                        backgroundColor = MaterialTheme.colors.surface,
                        modifier = Modifier.padding(all = 12.dp)
                    ) {
                        Column {
                            DeviceConnectionView()
                            LiveReadingsView()
                            //ExerciseList(LocalContext.current)
                        }
                    }

                }
            }
        }
    }
}

fun startForegroundService(context: Context) {
    val serviceIntent = Intent(context, HeartrateService::class.java)
    ContextCompat.startForegroundService(context, serviceIntent)
}

@Composable
fun ExerciseList(context: Context, exerciseListViewmodel: ExerciseListViewmodel = viewModel()) {
    val exercises by exerciseListViewmodel.exercises.collectAsState(initial = listOf())

    Column(
        modifier = Modifier.padding(all = 12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.SpaceEvenly) {
                Text(text = "Add new",
                    fontSize = 30.sp,
                    modifier = Modifier.clickable { exerciseListViewmodel.addExercise() })
                Text(text = "Delete all",
                    fontSize = 30.sp,
                    modifier = Modifier.clickable { exerciseListViewmodel.deleteAll() })
                Text(text = "Start service",
                    fontSize = 30.sp,
                    modifier = Modifier.clickable { startForegroundService(context) })
            }
        }
        exercises.map {
            Text(text = it.id.toString())
        }
    }
}