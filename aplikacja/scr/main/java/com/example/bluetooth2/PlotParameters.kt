package com.example.bluetooth2

enum class PlotType{
    X,
    Y,
    Z,
    BPM,
    SPO2,
    BREATH
}

class PlotParameters(
    measurements:List<SensorsData>,
    type:PlotType
){
    private val xValues = getX(measurements = measurements)
    private val yValues = getY(measurements = measurements,type = type)
    private val title = when(type){
        PlotType.X -> GlobalStrings.adxl_x
        PlotType.Y -> GlobalStrings.adxl_y
        PlotType.Z -> GlobalStrings.adxl_z
        PlotType.BPM -> GlobalStrings.bpm
        PlotType.SPO2 -> GlobalStrings.spo2
        PlotType.BREATH -> GlobalStrings.breath
    }
    private val xLabel = GlobalStrings.table_header_time
    private val yLabel = title

    private fun getX(measurements:List<SensorsData>):MutableList<Float>{
        val xValues = mutableListOf<Float>()
        for(i in measurements.indices){
            xValues.add(measurements[i].time.toFloat())
        }
        return  xValues
    }
    private fun getY(
        measurements:List<SensorsData>,
        type:PlotType
    ):MutableList<Float>{
        val yValues = mutableListOf<Float>()
        for(i in measurements.indices){
            if(type == PlotType.X){
                yValues.add(measurements[i].x.toFloat())
            }
            if(type == PlotType.Y){
                yValues.add(measurements[i].y.toFloat())
            }
            if(type == PlotType.Z){
                yValues.add(measurements[i].z.toFloat())
            }
            if(type == PlotType.BPM){
                yValues.add(measurements[i].bpm.toFloat())
            }
            if(type == PlotType.SPO2){
                yValues.add(measurements[i].spo2.toFloat())
            }
            if(type == PlotType.BREATH){
                yValues.add(measurements[i].breath.toFloat())
            }
        }
        return yValues
    }
    fun getXValues():List<Float>{
        return xValues
    }
    fun getYValues():List<Float>{
        return yValues
    }
    fun getXLabel():String{
        return xLabel
    }
    fun getYLabel():String{
        return yLabel
    }
    fun getTitle():String{
        return title
    }
}