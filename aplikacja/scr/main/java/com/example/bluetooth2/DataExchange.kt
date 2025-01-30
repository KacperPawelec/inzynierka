package com.example.bluetooth2

import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class DataExchange(
    socket:BluetoothSocket
):Thread(){
    private val inputStream:InputStream = socket.inputStream
    private val outputStream:OutputStream = socket.outputStream

    fun write(command:String){
        val bytes = command.toByteArray()
        try{
            outputStream.write(bytes)
        }catch(_:IOException){}
    }
    fun read():String{

        var numBytesRead = 0
        val length = 2
        val buffer = ByteArray(length)

        while(numBytesRead < length){
            val len = length - numBytesRead
            val num = inputStream.read(buffer,numBytesRead,len)
            if(num == -1){break}
            numBytesRead += num
        }

        val size = 10*(buffer[0].toInt() - 48) + (buffer[1].toInt() -48)
        val dataBuffer = ByteArray(size)
        numBytesRead = 0

        return try{
            while(numBytesRead < size){
                val len = size - numBytesRead
                val num = inputStream.read(dataBuffer,numBytesRead,len)
                if(num == -1){break}
                numBytesRead += num
            }
            String(
                bytes = dataBuffer,
                offset =  0,
                length = numBytesRead
            )
        }catch(e:IOException){GlobalStrings.error}
    }
}