package com.example.heartrate_monitor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.heartrate_monitor.R

@Composable
fun LiveReadingsView(deviceConnectionViewModel: DeviceConnectionViewModel = viewModel()) {
    val currentReading = deviceConnectionViewModel.latestHeartrateReading.observeAsState()
    val isConnected = deviceConnectionViewModel.connected.observeAsState(initial = false)
    val connectedDevice = deviceConnectionViewModel.connectedDevice.observeAsState(initial = null)

    if (isConnected.value) Card {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(12.dp)
        ) {
            if (connectedDevice.value != null) Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bluetooth),
                    contentDescription = "Bluetooth icon",
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(text = connectedDevice.value!!.name, fontSize = 20.sp)
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_ecg_heart),
                    contentDescription = "ECG icon",
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(text = currentReading.value.toString(), fontSize = 30.sp)
            }
        }
    }
}