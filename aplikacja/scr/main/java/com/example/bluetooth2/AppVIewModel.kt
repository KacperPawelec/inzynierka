package com.example.bluetooth2

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AppViewModel:ViewModel(){
    var isDialog1Shown = false
    var language = GlobalStrings.polish
    var isMenuShown = true
    var areSettingsShown = false
    var isConnected = mutableStateOf(value = false)
}