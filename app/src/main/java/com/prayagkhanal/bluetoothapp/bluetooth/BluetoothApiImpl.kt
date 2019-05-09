package com.prayagkhanal.bluetoothapp.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.prayagkhanal.bluetoothapp.events.DeviceFoundEvent
import org.greenrobot.eventbus.EventBus

class BluetoothApiImpl():BluetoothApi {

    private var activity:Activity? = null
    private  var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var bluetoothDialogListener: BluetoothDialogListener


    override fun initialize(activity: Activity): Boolean {

        this.activity = activity

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter?.let {adapter ->

            if(isDiscoverable()){
                if(adapter.isEnabled){
                    getDevices()
                }else{
                   val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    activity.startActivityForResult(enableIntent,BluetoothFacade.REQUEST_ENABLE_BT)
                }
            }else{
                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                    putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION)
                }
                activity.startActivityForResult(enableIntent,BluetoothFacade.REQUEST_DISCOVERABLE)
            }
        } ?: run {
            //TODO Error handle

            return false
        }

        return true
    }


    private fun isDiscoverable() =
            bluetoothAdapter?.scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE

    private val receiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    EventBus.getDefault().postSticky(DeviceFoundEvent(device))
                }
            }
        }
    }

    override fun unRegisterReceiver() {
        activity?.unregisterReceiver(receiver)
        if (bluetoothAdapter?.isDiscovering == true) stopDiscovery()
    }

    override fun stopDiscovery() {
        bluetoothAdapter?.cancelDiscovery()
    }

    private fun getDevices(){
        val intent = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED).apply{
            addAction(BluetoothDevice.ACTION_FOUND)
        }

        activity?.registerReceiver(receiver,intent)
        bluetoothAdapter?.startDiscovery()

    }

    override fun setBluetoothDialogListener(dialogListener: BluetoothDialogListener) {
        bluetoothDialogListener = dialogListener
    }

    override fun onBluetoothEnableComplete() {
        getDevices()
    }

    override fun onBluetoothDiscoverableComplete() {
        getDevices()
    }

    companion object {
        const val DISCOVERABLE_DURATION = 15
    }
}