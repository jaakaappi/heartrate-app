package com.example.heartrate_monitor.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DeviceConnectionView(viewModel: DeviceConnectionViewModel = viewModel()) {
    val devices = viewModel.scannedDevices.observeAsState(initial = listOf())
    val isScanning = viewModel.scanning.observeAsState(initial = false)
    val isConnected = viewModel.connected.observeAsState(initial = false)
    val context = LocalContext.current

    val btPermissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.BLUETOOTH_CONNECT,
            android.Manifest.permission.BLUETOOTH_SCAN
        )
    )

    if (!btPermissionState.allPermissionsGranted) {
        LaunchedEffect(key1 = btPermissionState, block = {
            Toast.makeText(
                context,
                "Permissions are required to scan your devices and connect to them",
                Toast.LENGTH_LONG
            ).show()
            btPermissionState.launchMultiplePermissionRequest()
        })
    } else {
        if (!isConnected.value) Column(
            verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(12.dp)
        ) {
            if (!isScanning.value) {
                Text(text = "No suitable devices connected")
                Button(onClick = {
                    viewModel.startScanning()
                }) { Text(text = "Scan for devices") }
            }
            if (isScanning.value || !devices.value.isEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isScanning.value) "Scan results" else "Found devices",
                        fontSize = 30.sp
                    )
                    if (isScanning.value) CircularProgressIndicator()
                }
                if (!devices.value.isEmpty()) Text(text = "Tap to connect")
                Card {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(12.dp)
                    ) {
                        if (!devices.value.isEmpty()) devices.value.map {
                            Text(text = it.name, fontSize = 20.sp, modifier = Modifier.clickable {
                                viewModel.connectBondAndListen(
                                    it
                                )
                            })
                        }
                        else Text("No devices found")
                    }
                }
            }

        }
    }
}