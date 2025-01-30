package com.example.bluetooth2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

enum class DialogType{
    OK,
    YesOrNo
}

@Composable
fun ModifiedDialog(
    onDismiss:() -> Unit,
    onClick1:() -> Unit,
    onClick2:() -> Unit,
    text:String,
    isDialogShown:Boolean,
    dialogType:DialogType
){
    if(isDialogShown){
        Dialog(
            onDismissRequest = onDismiss
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = BorderStroke(5.dp,Color.Black)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ){
                    Text(
                        text = text,
                        modifier = Modifier
                            .wrapContentSize(Alignment.Center),
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp
                    )
                    if(dialogType == DialogType.OK){
                        Button(
                            modifier = Modifier.width(100.dp),
                            onClick = onClick1,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black
                            )
                        ){
                            Text(
                                text = GlobalStrings.ok,
                                fontSize = 15.sp
                            )
                        }
                    }
                    if(dialogType == DialogType.YesOrNo){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            Button(
                                modifier = Modifier.width(100.dp),
                                onClick = onClick1,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black
                                )
                            ){
                                Text(
                                    text = GlobalStrings.yes,
                                    fontSize = 15.sp
                                )
                            }
                            Button(
                                modifier = Modifier.width(100.dp),
                                onClick = onClick2,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black
                                )
                            ){
                                Text(
                                    text = GlobalStrings.no,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}