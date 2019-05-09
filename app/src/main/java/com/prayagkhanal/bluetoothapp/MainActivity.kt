package com.prayagkhanal.bluetoothapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import permissions.dispatcher.NeedsPermission
import android.Manifest
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prayagkhanal.bluetoothapp.adapter.DeviceListAdapter
import com.prayagkhanal.bluetoothapp.bluetooth.BluetoothFacade
import com.prayagkhanal.bluetoothapp.events.DeviceFoundEvent
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.RuntimePermissions


@RuntimePermissions
class MainActivity : AppCompatActivity() {

    private lateinit var deviceListAdapter: DeviceListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scan_btn.setOnClickListener {
            deviceListAdapter.clear()
            connectUsingBluetoothWithPermissionCheck()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.device_list_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this).apply {
            orientation = RecyclerView.VERTICAL
        }
        deviceListAdapter = DeviceListAdapter()
        recyclerView.adapter = deviceListAdapter
        EventBus.getDefault().register(this)

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onDeviceFoundEvent(event: DeviceFoundEvent) {
        deviceListAdapter.addDevice(event.device)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated method
        onRequestPermissionsResult(requestCode, grantResults)
    }


    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    fun connectUsingBluetooth(){
        BluetoothFacade.connectWithAvailableUers(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            BluetoothFacade.REQUEST_ENABLE_BT -> {
                BluetoothFacade.onBluetoothEnableComplete()
            }
            BluetoothFacade.REQUEST_DISCOVERABLE -> {
                BluetoothFacade.onBluetoothDiscoverableComplete()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BluetoothFacade.destroy()
    }


}
