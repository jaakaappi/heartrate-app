package com.example.heartrate_monitor.ble

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.welie.blessed.*
import com.welie.blessed.BluetoothBytesParser.Companion.FORMAT_UINT16
import com.welie.blessed.BluetoothBytesParser.Companion.FORMAT_UINT8
import kotlinx.coroutines.*
import java.nio.ByteOrder
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class BluetoothHandler(context: Context) {
    var scannedDevices = MutableLiveData<Set<BluetoothPeripheral>>(setOf())
    var scanning = MutableLiveData(false)
    var connectedDevice = MutableLiveData<BluetoothPeripheral?>(null)
    var connected = MutableLiveData(false)
    var latestHeartRateReading = MutableLiveData(0)

    private val centralManager: BluetoothCentralManager = BluetoothCentralManager(context)
    private val HRS_SERVICE_UUID: UUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb")
    private val HRS_MEASUREMENT_CHARACTERISTIC_UUID: UUID =
        UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb")

    fun startScanning() {
        scanning.postValue(true)
        Log.d("BluetoothHandler", "Started scanning")

        Executors.newScheduledThreadPool(1).schedule(Runnable {
            centralManager.stopScan()
            scanning.postValue(false)
            Log.d("BluetoothHandler", "Stopped scanning")
        }, 60, TimeUnit.SECONDS);

        centralManager.scanForPeripheralsWithServices(
            arrayOf(HRS_SERVICE_UUID),
            { peripheral, ScanResult ->
                Log.d("BluetoothHandler", "Found peripheral ${peripheral.address}")
                if (!ScanResult.scanRecord?.deviceName.isNullOrEmpty()) {
                    scannedDevices.postValue((scannedDevices.value ?: setOf()) + peripheral)
                } else {
                    Log.d("BluetoothHandler", "Did not provide device name")
                }
            },
            { scanFailure ->
                Log.d("BluetoothHandler", scanFailure.toString())
            },
        )
    }

    fun connectBondAndListen(peripheral: BluetoothPeripheral) {
        centralManager.stopScan()
        scanning.postValue(false)
        // if (peripheral.bondState == BondState.BONDED)
        peripheral.connect()
        // else
        //     peripheral.createBond()

        centralManager.observeConnectionState { peripheral, state ->
            if (state == ConnectionState.CONNECTED) {
                connected.postValue(true)
                connectedDevice.postValue(peripheral)
                CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                    peripheral.getCharacteristic(
                        HRS_SERVICE_UUID, HRS_MEASUREMENT_CHARACTERISTIC_UUID
                    )?.let {
                        peripheral.observe(it) { value ->
                            val parser = BluetoothBytesParser(value, ByteOrder.LITTLE_ENDIAN)
                            val flags = parser.getIntValue(FORMAT_UINT8)
                            val pulse =
                                if (flags and 0x01 == 0) parser.getIntValue(FORMAT_UINT8) else parser.getIntValue(
                                    FORMAT_UINT16
                                )
                            latestHeartRateReading.postValue(pulse)
                            Log.d("BluetoothHandler", pulse.toString())
                        }
                    }
                }
            } else {
                // todo remove extra connected property, use device
                connected.postValue(false)
                connectedDevice.postValue(null)
            }
        }
    }
}