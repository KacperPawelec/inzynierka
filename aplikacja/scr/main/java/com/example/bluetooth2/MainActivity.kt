package com.example.bluetooth2

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val CONNECTION_FAILED:Int = 0
const val CONNECTION_SUCCESS:Int = 1
const val DISCONNECTION_FAILED:Int = 2
const val DISCONNECTION_SUCCESS:Int = 3
var dataExchange:DataExchange? = null

class MainActivity:ComponentActivity(){

    private lateinit var bluetoothManager:BluetoothManager
    private lateinit var bluetoothAdapter:BluetoothAdapter
    private val viewModel:AppViewModel by viewModels()
    private val database by lazy{DatabaseProvider.getDatabase(context = this)}
    private val sensorsDao by lazy{database.sensorsDao()}
    @RequiresApi(Build.VERSION_CODES.S)

    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
        uploadStrings(context = this)
        val connectionStatus = mutableStateOf(value = if(viewModel.isConnected.value){
            GlobalStrings.connected
        }else{
            GlobalStrings.disconnected
        })
        val status = mutableListOf(GlobalStrings.esp32,"")
        val handler = Handler(Looper.getMainLooper()){msg ->
            when(msg.what){
                CONNECTION_FAILED -> {
                    connectionStatus.value = GlobalStrings.connection_failed
                    true
                }
                CONNECTION_SUCCESS -> {
                    connectionStatus.value = GlobalStrings.connection_succeed
                    dataExchange?.write(command = GlobalStrings.connect_command)
                    true
                }
                DISCONNECTION_FAILED -> {
                    connectionStatus.value = GlobalStrings.disconnection_failed
                    true
                }
                DISCONNECTION_SUCCESS -> {
                    connectionStatus.value = GlobalStrings.disconnection_succeed
                    dataExchange?.write(command = GlobalStrings.disconnect_command)
                    true
                }
                else -> false
            }
        }
        val arePermissionsGranted = mutableIntStateOf(value = 2)
        val bluetoothPermission = android.Manifest.permission.BLUETOOTH_CONNECT
        val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted:Boolean ->
            if(isGranted){
                arePermissionsGranted.intValue = 1
                status[1] = GlobalStrings.permission_accepted
                connectionStatus.value = checkConnection(bluetoothAdapter)

            }else{
                status[1] = GlobalStrings.permission_refused
                arePermissionsGranted.intValue = 0
            }
        }
        if(ContextCompat.checkSelfPermission(applicationContext,bluetoothPermission) == PackageManager.PERMISSION_GRANTED){
            status[1] = GlobalStrings.permission_accepted
            arePermissionsGranted.intValue = 1
        }else{
            requestPermissionLauncher.launch(bluetoothPermission)
        }
        setContent{
            val scroll = rememberScrollState()
            if(arePermissionsGranted.intValue == 0){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Text(
                        text = GlobalStrings.reload_app,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp
                    )
                }
            }
            if(arePermissionsGranted.intValue == 1){
                ApplicationLayout(
                    viewModel = viewModel,
                    status = status,
                    connectionStatus = connectionStatus,
                    bluetoothAdapter = bluetoothAdapter,
                    handler = handler,
                    scrollState = scroll,
                    sensorsDao = sensorsDao
                )
            }
            if(arePermissionsGranted.intValue == 2){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ){}
            }
        }
    }
}

@Composable
fun ApplicationLayout(
    viewModel:AppViewModel,
    status:MutableList<String>,
    connectionStatus:MutableState<String>,
    bluetoothAdapter:BluetoothAdapter,
    handler:Handler,
    scrollState:ScrollState,
    sensorsDao:SensorsDao
){

    var measurements by remember{mutableStateOf(listOf<SensorsData>())}
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    var isLanguageChangeEnabled by remember{mutableStateOf(value = true)}
    var areSettingsShown by remember{mutableStateOf(value = viewModel.areSettingsShown)}
    var isDialog1Shown by remember{mutableStateOf(value = viewModel.isDialog1Shown)}
    var isDialog2Shown by remember{mutableStateOf(value = false)}
    var isConnected by remember{mutableStateOf(value = viewModel.isConnected)}
    var isMenuShown by remember{mutableStateOf(value = viewModel.isMenuShown)}
    var isTableShown by remember{mutableStateOf(value = false)}
    var isPlotShown by remember{mutableStateOf(value = false)}

    var dataReceived by remember{mutableStateOf(value = GlobalStrings.no_data)}

    val languageStringList = listOf(
        GlobalStrings.english,
        GlobalStrings.polish
    )
    val languageCodeList = listOf(
        GlobalStrings.english_code,
        GlobalStrings.polish_code
    )
    var languageIndex by rememberSaveable{mutableIntStateOf(value = 1)}
    var languageSelected by remember{mutableStateOf(viewModel.language)}

    val plotTypeList = listOf(
        PlotType.X,
        PlotType.Y,
        PlotType.Z,
        PlotType.BPM,
        PlotType.SPO2,
        PlotType.BREATH
    )
    val plotStringList = listOf(
        GlobalStrings.adxl_x,
        GlobalStrings.adxl_y,
        GlobalStrings.adxl_z,
        GlobalStrings.bpm,
        GlobalStrings.spo2,
        GlobalStrings.breath
    )
    var plotIndex by rememberSaveable{mutableIntStateOf(value = 3)}
    var plotSelected by remember{mutableStateOf(value = plotTypeList[3])}

    LaunchedEffect(viewModel.isConnected){
        isConnected = viewModel.isConnected
    }
    LaunchedEffect(Unit){
        while(true){
            dataReceived = dataExchange?.read()?:GlobalStrings.no_data
            if(dataReceived != GlobalStrings.no_data){
                val sensorMeasurement = SensorMeasurement(measurement = dataReceived)
                val sensorData = sensorMeasurement.getData()
                val newMeasurement = SensorsData(
                    time = sensorData[0],
                    x = sensorData[1],
                    y = sensorData[2],
                    z = sensorData[3],
                    bpm = sensorData[4],
                    spo2 = sensorData[5],
                    breath = sensorData[6]
                )
                sensorsDao.insert(newMeasurement)
            }
            delay(timeMillis = 1000)
        }
    }
    ModifiedDialog(
        onDismiss = {
            viewModel.isDialog1Shown = false
            isDialog1Shown = false
        },
        onClick1 = {
            viewModel.isDialog1Shown = false
            isDialog1Shown = false
        },
        onClick2 = {},
        text = GlobalStrings.dialog_message,
        isDialogShown = isDialog1Shown,
        dialogType = DialogType.OK
    )
    ModifiedDialog(
        onDismiss = {
            isDialog2Shown = false
        },
        onClick1 = {
            isDialog2Shown = false
            coroutineScope.launch{
                sensorsDao.deleteAll()
                measurements = sensorsDao.getAll()
            }
        },
        onClick2 = {
            isDialog2Shown = false
        },
        text = GlobalStrings.dialog_message2,
        isDialogShown = isDialog2Shown,
        dialogType = DialogType.YesOrNo
    )
    if(isMenuShown){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            ModifiedButton(
                onClick = {
                    viewModel.isMenuShown = false
                    isTableShown = true
                    isMenuShown = false
                },
                text = GlobalStrings.file_as_table,
                enable = true
            )
            ModifiedButton(
                onClick = {
                    viewModel.isMenuShown = false
                    isMenuShown = false
                    isPlotShown = true
                },
                text = GlobalStrings.file_as_plot,
                enable = true
            )
            ModifiedButton(
                onClick = {
                    viewModel.areSettingsShown = true
                    viewModel.isMenuShown = false
                    areSettingsShown = true
                    isMenuShown = false
                },
                text = GlobalStrings.settings,
                enable = true
            )
        }
    }else{
        if(isTableShown){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                LaunchedEffect(Unit){
                    try{
                        measurements = sensorsDao.getAll()
                    }catch(e:Exception){
                        e.printStackTrace()
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    Spacer(modifier = Modifier.padding(vertical = 5.dp))
                    ModifiedButton(
                        onClick = {
                            isTableShown = false
                            viewModel.isMenuShown = true
                            isMenuShown = true
                        },
                        text = GlobalStrings.return_button,
                        enable = true
                    )
                    ModifiedButton(
                        onClick = {
                            isDialog2Shown = true
                        },
                        text = GlobalStrings.clear_database,
                        enable = true
                    )
                }
                TableFromMeasurements(measurements = measurements)
            }
        }
        if(isPlotShown){
            LaunchedEffect(Unit){
                try{
                    measurements = sensorsDao.getAll()
                }catch(e:Exception){
                    e.printStackTrace()
                }
            }
            var plotParameters = PlotParameters(
                measurements = measurements,
                type = plotSelected
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    ModifiedButton(
                        onClick = {
                            isPlotShown = false
                            viewModel.isMenuShown = true
                            isMenuShown = true
                        },
                        text = GlobalStrings.return_button,
                        enable = true
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                    DropdownList(
                        itemList = plotStringList,
                        selectedIndex = plotIndex,
                        text = GlobalStrings.plot_list,
                        modifier = Modifier.width(100.dp),
                        enable = true,
                        onItemClick = {
                            plotIndex = it
                            plotSelected = plotTypeList[plotIndex]
                            coroutineScope.launch{
                                measurements = sensorsDao.getAll()
                            }
                            plotParameters = PlotParameters(
                                measurements = measurements,
                                type = plotSelected
                            )
                        }
                    )
                }
                if(plotParameters.getXValues().size > 3){
                    Plots(
                        xValues = plotParameters.getXValues(),
                        yValues = plotParameters.getYValues(),
                        xLabel = plotParameters.getXLabel(),
                        yLabel = plotParameters.getYLabel(),
                        xAxisNumberOfTick = 10,
                        yAxisNumberOfTick = 10,
                        title = plotParameters.getTitle(),
                        graphBackgroundColor = Color.Black,
                        graphLineColor = Color.Red,
                        graphBoxColor = Color.White
                    )
                }else{
                    ModifiedText(text = GlobalStrings.plot_message)
                }
            }
        }
        if(areSettingsShown){

            val settings = listOf(
                GlobalStrings.bluetooth_settings,
                GlobalStrings.language_settings,
            )
            val bluetoothList = listOf(
                GlobalStrings.connected_device,
                GlobalStrings.permissions,
                GlobalStrings.connection
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                ModifiedButton(
                    onClick = {
                        areSettingsShown = false
                        viewModel.areSettingsShown = false
                        isMenuShown = true
                        viewModel.isMenuShown = true
                    },
                    text = GlobalStrings.return_button,
                    enable = true
                )
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                DashedLineWithText(
                    text = settings[0],
                    fontColor = Color.White
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        ModifiedButton(
                            onClick = {
                                isLanguageChangeEnabled = false
                                connect(
                                    bluetoothAdapter = bluetoothAdapter,
                                    handler = handler,
                                    viewModel = viewModel
                                )
                                if(viewModel.isConnected.value){
                                    dataExchange?.write(command = GlobalStrings.connect_command)
                                }
                                isConnected = viewModel.isConnected
                            },
                            text = GlobalStrings.connect_button,
                            enable = if(status[1] == GlobalStrings.permission_refused){
                                false
                            }else{
                                !isConnected.value
                            }
                        )
                        ModifiedButton(
                            onClick = {
                                isLanguageChangeEnabled = true
                                disconnect(
                                    bluetoothAdapter = bluetoothAdapter,
                                    handler = handler,
                                    viewModel = viewModel
                                )
                                if(viewModel.isConnected.value){
                                    dataExchange?.write(command = GlobalStrings.disconnect_command)
                                }
                                isConnected = viewModel.isConnected
                            },
                            text = GlobalStrings.disconnect_button,
                            enable = isConnected.value
                        )
                    }
                    for(i in 0..1){
                        BluetoothRow(
                            text1 = bluetoothList[i],
                            text2 = status[i]
                        )
                    }
                    BluetoothRow(
                        text1 = bluetoothList[2],
                        text2 = connectionStatus.value
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        ModifiedText(text = GlobalStrings.received_data)
                        ModifiedText(
                            text = if(dataReceived == GlobalStrings.no_data){
                                GlobalStrings.no_data
                            }else{
                                dataReceived.dropLast(n = 3)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 10.dp))
                DashedLineWithText(
                    text = settings[1],
                    fontColor = Color.White
                )
                ModifiedText(text = GlobalStrings.language_text)
                DropdownList(
                    itemList = languageStringList,
                    selectedIndex = languageIndex,
                    text = GlobalStrings.language,
                    modifier = Modifier.width(100.dp),
                    enable = isLanguageChangeEnabled,
                    onItemClick = {
                        languageIndex = it
                        languageSelected = languageStringList[languageIndex]
                        if(languageSelected != viewModel.language){
                            for(i in languageStringList.indices){
                                if(languageStringList[i] == languageSelected){
                                    viewModel.isDialog1Shown = true
                                    viewModel.language = languageSelected
                                    setLocale(
                                        context = context,
                                        languageCode = languageCodeList[i]
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}