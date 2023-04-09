package com.example.heartrate_monitor.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.heartrate_monitor.ble.BluetoothHandler
import com.welie.blessed.BluetoothPeripheral
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

@HiltViewModel
class DeviceConnectionViewModel @Inject constructor(private val bluetoothHandler: BluetoothHandler) :
    ViewModel() {
    val scannedDevices: LiveData<Set<BluetoothPeripheral>> = bluetoothHandler.scannedDevices
    val connectedDevice: LiveData<BluetoothPeripheral?> = bluetoothHandler.connectedDevice
    val scanning: LiveData<Boolean> = bluetoothHandler.scanning
    val connected: LiveData<Boolean> = bluetoothHandler.connected
    val latestHeartrateReading: LiveData<Int> = bluetoothHandler.latestHeartRateReading

    fun startScanning() = bluetoothHandler.startScanning()
    fun connectBondAndListen(peripheral: BluetoothPeripheral) =
        bluetoothHandler.connectBondAndListen(peripheral)
}