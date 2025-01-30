package com.example.bluetooth2

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ModifiedButton(
    onClick:() -> Unit,
    text:String,
    enable:Boolean
){
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Green,
            disabledContainerColor = Color.Red
        ),
        enabled = enable,
        modifier = Modifier.width(180.dp)
    ){
        Text(
            text = text,
            color = Color.Black,
            fontSize = 16.sp
        )
    }
}