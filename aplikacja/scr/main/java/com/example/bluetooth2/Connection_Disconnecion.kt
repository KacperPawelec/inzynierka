package com.example.bluetooth2

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler
import java.io.IOException
import java.util.UUID

val uuid:UUID = UUID.fromString(GlobalStrings.uuid)

@SuppressLint("MissingPermission")
fun checkConnection(bluetoothAdapter:BluetoothAdapter?):String{
    val pairedDevices:Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
    val esp32 = pairedDevices?.find{it.name == GlobalStrings.esp32}
    return if(esp32 != null){
        ""
    }else{
        GlobalStrings.esp32_not_connected
    }
}

@SuppressLint("MissingPermission")
fun connect(bluetoothAdapter:BluetoothAdapter?,handler:Handler,viewModel:AppViewModel){
    val pairedDevices:Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
    val esp32 = pairedDevices?.find{it.name == GlobalStrings.esp32}
    if(esp32 != null){
        ConnectThread(
            device = esp32,
            handler = handler,
            viewModel = viewModel
        ).start()
    }
}
@SuppressLint("MissingPermission")
fun disconnect(bluetoothAdapter:BluetoothAdapter?,handler:Handler,viewModel:AppViewModel){
    val pairedDevices:Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
    val esp32 = pairedDevices?.find{it.name == GlobalStrings.esp32}
    if(esp32 != null){
        DisconnectThread(
            device = esp32,
            handler = handler,
            viewModel = viewModel
        ).start()
    }
}

@SuppressLint("MissingPermission")
class ConnectThread(
    private val device:BluetoothDevice,
    private val handler:Handler,
    private val viewModel:AppViewModel
):Thread(){
    private val bluetoothSocket:BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE){
        device.createRfcommSocketToServiceRecord(uuid)
    }
    override fun run(){
        bluetoothSocket?.let{socket ->
            try{
                socket.connect()
                handler.obtainMessage(CONNECTION_SUCCESS).sendToTarget()
                dataExchange = DataExchange(socket)
                viewModel.isConnected.value = true
            }catch(e:Exception){
                try{
                    socket.connect()
                    handler.obtainMessage(CONNECTION_SUCCESS).sendToTarget()
                    dataExchange = DataExchange(socket)
                    viewModel.isConnected.value = true
                }catch(e:Exception){
                    handler.obtainMessage(CONNECTION_FAILED).sendToTarget()
                }
            }
        }
    }
}
@SuppressLint("MissingPermission")
class DisconnectThread(
    private val device:BluetoothDevice,
    private val handler:Handler,
    private val viewModel:AppViewModel
):Thread(){
    private val bluetoothSocket:BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE){
        device.createRfcommSocketToServiceRecord(uuid)
    }
    override fun run(){
        bluetoothSocket?.let{socket ->
            try{
                socket.close()
                handler.obtainMessage(DISCONNECTION_SUCCESS).sendToTarget()
                dataExchange = null
                viewModel.isConnected.value = false
            }catch(e:IOException){
                try{
                    socket.close()
                    handler.obtainMessage(DISCONNECTION_SUCCESS).sendToTarget()
                    dataExchange = null
                    viewModel.isConnected.value = false
                }catch(e:Exception){
                    handler.obtainMessage(DISCONNECTION_FAILED).sendToTarget()
                }
            }
        }
    }
}