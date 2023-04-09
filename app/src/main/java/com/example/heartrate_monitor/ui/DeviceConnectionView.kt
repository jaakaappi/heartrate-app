package com.example.heartrate_monitor.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DeviceConnectionView(viewModel: DeviceConnectionViewModel = viewModel()) {
    val devices = viewModel.scannedDevices.observeAsState(initial = listOf())
    val isScanning = viewModel.scanning.observeAsState(initial = false)
    val isConnected = viewModel.connected.observeAsState(initial = false)

    if (!isConnected.value)
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(12.dp)
        ) {
            if (!isScanning.value) {
                Text(text = "No suitable devices connected")
                Button(onClick = { viewModel.startScanning() }) {
                    Text(text = "Scan for devices")
                }
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
                            Text(
                                text = it.name,
                                fontSize = 20.sp,
                                modifier = Modifier.clickable { viewModel.connectBondAndListen(it) })
                        }
                        else Text("No devices found")
                    }
                }
            }

        }
}