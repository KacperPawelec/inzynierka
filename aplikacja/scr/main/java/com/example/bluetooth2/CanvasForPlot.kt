package com.example.bluetooth2

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.math.absoluteValue

@Composable
fun CanvasForPlot(
    xValues:List<Float>,
    yValues:List<Float>,
    xLabel:String,
    yLabel:String,
    xAxisNumberOfTick:Int,
    yAxisNumberOfTick:Int,
    title:String,
    graphLineColor:Color,
    graphBoxColor:Color,
    modifier:Modifier = Modifier
){
    if(xValues.size != yValues.size){
        throw IllegalArgumentException(GlobalStrings.canvas_error)
    }

    Canvas(modifier = modifier){

        val width = size.width
        val height = size.height

        val xMax = xValues.max()
        val yMax = yValues.max()
        val xMin = xValues.min()
        val yMin = yValues.min()

        val padding = 100f
        val tickLength = 10f

        val xZero = padding - xMin/(xMax - xMin)*(height - 2*padding)
        val yZero = padding - yMin/(yMax - yMin)*(width - 2*padding)

        drawLine(
            color = graphBoxColor,
            start = Offset(
                x = padding,
                y = height - padding
            ),
            end = Offset(
                x = width - padding,
                y = height - padding
            ),
            strokeWidth = 3f
        )
        drawLine(
            color = graphBoxColor,
            start = Offset(
                x = padding,
                y = padding
            ),
            end = Offset(
                x = padding,
                y = height - padding
            ),
            strokeWidth = 3f
        )
        drawLine(
            color = graphBoxColor,
            start = Offset(
                x = padding,
                y = padding
            ),
            end = Offset(
                x = width - padding,
                y = padding
            ),
            strokeWidth = 3f
        )
        drawLine(
            color = graphBoxColor,
            start = Offset(
                x = width - padding,
                y = padding
            ),
            end = Offset(
                x = width - padding,
                y = height - padding
            ),
            strokeWidth = 3f,
        )
        if(xMin < 0 && xMax > 0){
            drawLine(
                color = graphBoxColor,
                start = Offset(
                    x = padding,
                    y = xZero
                ),
                end = Offset(
                    x = width - padding,
                    y = xZero
                ),
                strokeWidth = 2f,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(10f, 10f),
                    phase = 0f
                )
            )
        }
        if(yMin < 0 && yMax > 0){
            drawLine(
                color = graphBoxColor,
                start = Offset(
                    x = yZero,
                    y = padding
                ),
                end = Offset(
                    x = yZero,
                    y = height - padding
                ),
                strokeWidth = 2f,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(10f, 10f),
                    phase = 0f
                )
            )
        }

        val path = Path().apply{
            xValues.forEachIndexed{index,xValue ->
                val x = padding + (xValue - xMin)/(xMax - xMin)*(height - 2*padding)
                val y = padding + (yValues[index] - yMin)/(yMax - yMin)*(width - 2*padding)

                if(index == 0){
                    moveTo(y,x)
                }else{
                    lineTo(y,x)
                }
            }
        }

        drawPath(
            path = path,
            color = graphLineColor,
            style = Stroke(
                width = 5f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        val titlePaint = Paint().apply{
            textSize = 50f
            color = graphBoxColor.hashCode()
        }
        val labelPaint = Paint().apply{
            textSize = 40f
            color = graphBoxColor.hashCode()
        }
        val axisPaint = Paint().apply{
            textSize = 30f
            color = graphBoxColor.hashCode()
        }

        drawContext.canvas.nativeCanvas.apply{
            save()
            rotate(90f)
            drawText(
                title,
                xZero - height/5,
                -(width - 0.6f*padding),
                titlePaint
            )
            drawText(
                xLabel,
                height/2 - 0.1f*padding,
                -0.1f*padding,
                labelPaint
            )
            drawText(
                yLabel,
                padding,
                -width + 0.8f*padding,
                labelPaint
            )
            restore()
        }

        val xStep = (xMax - xMin)/xAxisNumberOfTick
        for(i in 0..xAxisNumberOfTick){

            val x = padding + i*(height - 2*padding)/xAxisNumberOfTick
            var label = ((xMin + i*xStep).toDouble().round(decimals = 2)).toString()
            var xAxisPadding = padding/100

            if(label.toDouble().absoluteValue >= 100f){
                label = (xMin + i*xStep).toDouble().round(decimals = 1).toString()
                xAxisPadding = padding/100
            }

            drawLine(
                color = graphBoxColor,
                start = Offset(
                    x = padding + tickLength,
                    y = x
                ),
                end = Offset(
                    x = padding - tickLength,
                    y = x
                ),
                strokeWidth = 2f
            )

            drawContext.canvas.nativeCanvas.apply{
                save()
                rotate(90f)
                drawText(
                    label,
                    x - 30f,
                    xAxisPadding - 50f,
                    axisPaint
                )
                restore()
            }
        }

        val yStep = (yMax - yMin)/yAxisNumberOfTick
        for(i in 0..yAxisNumberOfTick){
            val y = padding + i*(width - 2*padding)/yAxisNumberOfTick
            var label = (yMin + i*yStep).toDouble().round(decimals = 2).toString()

            if(label.toDouble().absoluteValue >= 100f){
                label = (yMin + i*yStep).toDouble().round(decimals = 1).toString()
            }

            drawLine(
                color = graphBoxColor,
                start = Offset(
                    x = y,
                    y = padding - tickLength
                ),
                end = Offset(
                    x = y,
                    y = padding + tickLength
                ),
                strokeWidth = 2f
            )
            drawContext.canvas.nativeCanvas.apply{
                save()
                rotate(90f)
                drawText(
                    label,
                    0f,
                    -y + 10f,
                    axisPaint
                )
                restore()
            }
        }
    }
}