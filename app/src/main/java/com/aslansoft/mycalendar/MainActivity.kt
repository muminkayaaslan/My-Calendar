package com.aslansoft.mycalendar

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.aslansoft.calendarLibrary.Calendar
import com.aslansoft.mycalendar.ui.theme.MyCalendarTheme
import java.time.LocalDate
import java.time.Month


class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyCalendarTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                stringResource(R.string.app_name),
                                fontSize = 30.sp,
                                color = Color.White
                            )
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.DarkGray
                        )
                    )
                }) { innerPadding ->
                    var alertDialogState by remember { mutableStateOf(false) }
                    if(alertDialogState){
                        BasicAlertDialog(onDismissRequest = { alertDialogState = false }) {
                                Card{
                                    Text("Hello World")
                                }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(color = if (isSystemInDarkTheme()) Color.Black else Color.White)
                    ) {

                        val dateList = listOf(
                            LocalDate.of(2025,Month.MARCH,3),
                            LocalDate.now(),
                            LocalDate.of(2025,2,16),
                            LocalDate.of(2025,Month.MARCH,23)
                        )
                        Calendar(
                            dateList,
                            dayOnclick = { alertDialogState = true })
                    }
                }
            }
        }
    }
}

