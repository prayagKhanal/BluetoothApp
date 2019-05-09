package com.prayagkhanal.bluetoothapp.bluetooth

import android.app.Activity

interface BluetoothApi {

    fun initialize(actiity:Activity): Boolean
//    fun setBluetoothDeviceListener(deviceListener:BluetoothDeviceListener)
    fun setBluetoothDialogListener(dialogListener: BluetoothDialogListener)
    fun onBluetoothDiscoverableComplete()
    fun onBluetoothEnableComplete()
    fun unRegisterReceiver()
    fun stopDiscovery()


}