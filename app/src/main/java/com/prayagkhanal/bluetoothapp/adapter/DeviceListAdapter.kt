package com.prayagkhanal.bluetoothapp.adapter

import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prayagkhanal.bluetoothapp.R
import com.prayagkhanal.bluetoothapp.bluetooth.client.Connect

class DeviceListAdapter:RecyclerView.Adapter<DeviceListAdapter.ViewHolder>(){

    var deviceList = mutableListOf<BluetoothDevice>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    fun clear(){
        deviceList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount() = deviceList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItemAtPosition(position)?.let {
            holder.bind(it)
        }
    }

    fun addDevice(device: BluetoothDevice) {
        deviceList?.apply {
            add(device)
            notifyItemInserted(this.size - 1)
        }
    }

    private fun getItemAtPosition(position: Int): BluetoothDevice? {
        return deviceList?.get(position)
    }


    inner class ViewHolder:RecyclerView.ViewHolder{
        constructor(parent: ViewGroup) : super(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_discovered_device,
                parent, false
            )
        )
        private lateinit var item:BluetoothDevice

        val addressTextView: TextView = itemView.findViewById(R.id.text_address)
        val nameTextView: TextView = itemView.findViewById(R.id.text_name)

        init {
            itemView.findViewById<Button>(R.id.button_connect).setOnClickListener {
                Log.d("  Device To Connect:-  ",item.address+" " + item.name)
                //Use Connect class pass it the device to initate connection
                val connectBeingClient = Connect(item)
                connectBeingClient.connectToDevice()
            }
        }

        fun bind(device: BluetoothDevice) {
            item = device
            addressTextView.text = device.address
            nameTextView.text = device.name
        }

    }
}