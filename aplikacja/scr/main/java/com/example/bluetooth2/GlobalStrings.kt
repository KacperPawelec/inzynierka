package com.example.bluetooth2

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import kotlin.reflect.KMutableProperty1

object GlobalStrings{
    lateinit var uuid:String
    lateinit var connection_failed:String
    lateinit var connection_succeed:String
    lateinit var connect_button:String
    lateinit var connect_command:String
    lateinit var connected:String
    lateinit var connection:String
    lateinit var connected_device:String
    lateinit var disconnection_failed:String
    lateinit var disconnection_succeed:String
    lateinit var disconnect_button:String
    lateinit var disconnect_command:String
    lateinit var disconnected:String
    lateinit var permission_accepted:String
    lateinit var permission_refused:String
    lateinit var permissions:String
    lateinit var esp32_not_connected:String
    lateinit var esp32:String
    lateinit var canvas_error:String
    lateinit var error:String
    lateinit var no_data:String
    lateinit var led_on_command:String
    lateinit var led_on:String
    lateinit var led_off_command:String
    lateinit var led_of:String
    lateinit var bluetooth_settings:String
    lateinit var language_settings:String
    lateinit var settings:String
    lateinit var yes:String
    lateinit var no:String
    lateinit var ok:String
    lateinit var reload_app:String
    lateinit var clear_database:String
    lateinit var received_data:String
    lateinit var return_button:String
    lateinit var language_text:String
    lateinit var language:String
    lateinit var english:String
    lateinit var english_code:String
    lateinit var polish:String
    lateinit var polish_code:String
    lateinit var dialog_message:String
    lateinit var dialog_message2:String
    lateinit var table_header_time:String
    lateinit var adxl_x:String
    lateinit var adxl_y:String
    lateinit var adxl_z:String
    lateinit var bpm:String
    lateinit var spo2:String
    lateinit var breath:String
    lateinit var file_as_table:String
    lateinit var file_as_plot:String
    lateinit var plot_message:String
    lateinit var plot_list:String
}

@SuppressLint("DiscouragedApi")
fun uploadStrings(context:Context){
    GlobalStrings::class.members
        .filterIsInstance<KMutableProperty1<GlobalStrings,String>>()
        .forEach{property ->
            val name = property.name
            val id = context.resources.getIdentifier(name,"string",context.packageName)
            if(id != 0){
                property.set(GlobalStrings,context.getString(id))
            }
        }
}