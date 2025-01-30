package com.example.bluetooth2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TableFromMeasurements(measurements:List<SensorsData>){

    Spacer(modifier = Modifier.padding(vertical = 5.dp))
    DashedLineWithText(
        text = "",
        fontColor = Color.White
    )
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        ModifiedTextForTable(text = GlobalStrings.table_header_time)
        ModifiedText(text = "|")
        ModifiedTextForTable(text = GlobalStrings.adxl_x)
        ModifiedText(text = "|")
        ModifiedTextForTable(text = GlobalStrings.adxl_y)
        ModifiedText(text = "|")
        ModifiedTextForTable(text = GlobalStrings.adxl_z)
        ModifiedText(text = "|")
        ModifiedTextForTable(text = GlobalStrings.bpm)
        ModifiedText(text = "|")
        ModifiedTextForTable(text = GlobalStrings.spo2)
        ModifiedText(text = "|")
        ModifiedTextForTable(text = GlobalStrings.breath)
    }
    DashedLineWithText(
        text = "",
        fontColor = Color.White
    )
    for(i in measurements.indices){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            ModifiedTextForTable(text = measurements[i].time.toString())
            ModifiedText(text = "|")
            ModifiedTextForTable(text = measurements[i].x.toString())
            ModifiedText(text = "|")
            ModifiedTextForTable(text = measurements[i].y.toString())
            ModifiedText(text = "|")
            ModifiedTextForTable(text = measurements[i].z.toString())
            ModifiedText(text = "|")
            ModifiedTextForTable(text = measurements[i].bpm.toString())
            ModifiedText(text = "|")
            ModifiedTextForTable(text = measurements[i].spo2.toString())
            ModifiedText(text = "|")
            ModifiedTextForTable(text = measurements[i].breath.toString())
        }
    }
}