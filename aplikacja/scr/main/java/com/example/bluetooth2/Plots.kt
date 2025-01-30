package com.example.bluetooth2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun Plots(
    xValues:List<Float>,
    yValues:List<Float>,
    xLabel:String,
    yLabel:String,
    xAxisNumberOfTick:Int,
    yAxisNumberOfTick:Int,
    title:String,
    graphBackgroundColor:Color,
    graphLineColor:Color,
    graphBoxColor:Color
){

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        CanvasForPlot(
            xValues = xValues,
            yValues = yValues,
            xLabel = xLabel,
            yLabel = yLabel,
            xAxisNumberOfTick = xAxisNumberOfTick,
            yAxisNumberOfTick = yAxisNumberOfTick,
            title = title,
            graphLineColor = graphLineColor,
            graphBoxColor = graphBoxColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight)
                .background(graphBackgroundColor)
        )
    }
}