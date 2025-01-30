package com.example.bluetooth2

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun ModifiedText(
    text:String,
){
    Text(
        text = text,
        color = Color.White,
        fontSize = 20.sp,
    )
}

@Composable
fun ModifiedTextForTable(
    text:String,
){
    Text(
        text = text,
        color = Color.White,
        fontSize = 20.sp,
        textAlign = TextAlign.Left,
    )
}