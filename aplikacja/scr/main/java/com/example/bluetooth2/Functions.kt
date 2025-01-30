@file:Suppress("DEPRECATION")

package com.example.bluetooth2

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import java.util.Locale
import kotlin.math.pow

fun splitTextByDelimiter(text:String,delimiter:String):List<String>{
    return text.split(delimiter)
}
fun Double.round(decimals:Int):Double{
    val base = 10.0
    val multiplier = base.pow(decimals.toDouble())
    return kotlin.math.round(x = multiplier*this)/multiplier
}
fun setLocale(context:Context,languageCode:String){
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.resources.updateConfiguration(config,context.resources.displayMetrics)
    (context as? Activity)?.recreate()
}