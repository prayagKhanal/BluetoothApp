package com.prayagkhanal.bluetoothapp.bluetooth.client

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.util.Log
import com.prayagkhanal.bluetoothapp.bluetooth.BluetoothFacade
import java.io.IOException
import java.util.*

class Connect(val device:BluetoothDevice){

    fun connectToDevice(){
        ConnectThread(device)
    }

    private fun manageMyConnectedSocket(socket: BluetoothSocket){
        //From this point the socket can be used to read or write stream to
        //the RFCOMM channel

    }


    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(Companion.MY_UUID)
        }

        override fun run() {
            BluetoothFacade.cancelDiscovery()

            mmSocket?.use { socket ->
                socket.connect()
                manageMyConnectedSocket(socket)
            }
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }

    companion object {
        val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
    }

}
