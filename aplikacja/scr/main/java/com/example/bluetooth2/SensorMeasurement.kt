package com.example.bluetooth2

class SensorMeasurement(
    measurement:String
){
    private val measurement = measurement.dropLast(n = 3)
    private val data = getDataFromMeasurement()

    private fun getDataFromMeasurement():List<Int>{

        val data = mutableListOf<Int>()
        val measurementSplited = splitTextByDelimiter(
            text = measurement,
            delimiter = ","
        )
        for(i in measurementSplited.indices){
            data.add(measurementSplited[i].toInt())
        }
        return data
    }

    fun getData():List<Int>{
        return data
    }
}