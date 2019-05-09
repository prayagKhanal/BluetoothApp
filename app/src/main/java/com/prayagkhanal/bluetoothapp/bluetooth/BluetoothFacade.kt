package com.prayagkhanal.bluetoothapp.bluetooth

import android.app.Activity

object BluetoothFacade {

    private val bluetoothApi: BluetoothApi = BluetoothApiImpl()


    fun connectWithAvailableUers(activity: Activity) = bluetoothApi.initialize(activity)

    fun onBluetoothEnableComplete() = bluetoothApi.onBluetoothEnableComplete()

    fun onBluetoothDiscoverableComplete() = bluetoothApi.onBluetoothDiscoverableComplete()

    fun destroy() = bluetoothApi.unRegisterReceiver()

    fun cancelDiscovery() = bluetoothApi.stopDiscovery()

    const val REQUEST_ENABLE_BT = 101
    const val REQUEST_DISCOVERABLE = 100

}