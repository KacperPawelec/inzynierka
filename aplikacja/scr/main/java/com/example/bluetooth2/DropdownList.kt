package com.example.bluetooth2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun DropdownList(
    itemList:List<String>,
    selectedIndex:Int,
    text:String,
    modifier:Modifier,
    enable:Boolean,
    onItemClick:(Int) -> Unit
){

    var showDropdown by rememberSaveable{mutableStateOf(value = false)}
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            color = Color.White
        )
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = modifier
                    .background(Color.White)
                    .border(
                        width = 3.dp,
                        color = Color.Black
                    )
                    .height(40.dp)
                    .clickable{
                        if(enable){
                            showDropdown = true
                        }
                    },
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = itemList[selectedIndex],
                    modifier = Modifier.padding(4.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
            Box{
                if(showDropdown){
                    Popup(
                        alignment = Alignment.TopCenter,
                        properties = PopupProperties(
                            excludeFromSystemGesture = true
                        ),
                        onDismissRequest = {
                            showDropdown = false
                        }
                    ){
                        Column(
                            modifier = modifier
                                .padding(1.dp)
                                .heightIn(max = 150.dp)
                                .verticalScroll(state = scrollState)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black
                                ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            itemList.onEachIndexed{index,item ->
                                if(index != 0){
                                    Divider(
                                        thickness = 1.dp,
                                        color = Color.Black
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .background(Color.White)
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .clickable{
                                            onItemClick(index)
                                            showDropdown = !showDropdown
                                        },
                                    contentAlignment = Alignment.Center
                                ){
                                    Text(
                                        text = item,
                                        fontSize = 20.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}