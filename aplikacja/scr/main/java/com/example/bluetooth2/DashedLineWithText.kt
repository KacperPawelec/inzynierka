package com.example.bluetooth2

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun DashedLineWithText(
    text:String,
    fontColor:Color,
){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val dash = "-"
    val textWidth = measureTextWidthInDp(text = text)
    val dashWidth = measureTextWidthInDp(text = dash)
    
    val dashNumber = (((screenWidth.value - textWidth.value)/dashWidth.value/2) - 5).roundToInt()
    val dashes = dash.repeat(n = dashNumber)
    val line = "$dashes$text$dashes"

    Text(
        text = line,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        color = fontColor
    )
}

@Composable
fun measureTextWidthInDp(text:String,fontSize:TextUnit = 18.sp):Dp{

    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    val textLayoutResult = remember(text){
        textMeasurer.measure(
            text = AnnotatedString(text),
            style = TextStyle(
                fontSize = fontSize,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Default,
                textAlign = TextAlign.Left
            )
        )
    }

    val textWidth = textLayoutResult.size.width
    return with(density){textWidth.toDp()}
}