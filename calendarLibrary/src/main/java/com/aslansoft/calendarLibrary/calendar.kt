package com.aslansoft.calendarLibrary

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DateFormatSymbols
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Calendar(
    MarkedDays: List<LocalDate>,
    ContainerColor: Color = if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFF5F5F5),
    HeaderColor: Color = if (isSystemInDarkTheme()) Color(0xFF1E1E1E) else Color(0xFFE0E0E0),
    TitleColor: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    WeekDaysColor: Color = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray,
    CurrentDaysBorderColor: Color = Color(0xFF00ACC1),
    SelectedDayBorderColor: Color = if (isSystemInDarkTheme()) Color(0xFFFCB986) else Color(
        0xFF6200EA
    ),
    TodayBorderColor: Color = if (isSystemInDarkTheme()) Color(0xFFDC64CC) else Color(0xFF0288D1),
    CurrentDaysTextColor: Color = if (isSystemInDarkTheme()) Color(0xFFE0E0E0) else Color(0xFF424242),
    SelectedDayTextColor: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    TodayTextColor: Color = if (isSystemInDarkTheme()) Color.White else Color.Black,
    BadgeFirstColor: Color = Color(0xFFFBBC05),
    BadgeTwoColor: Color = Color(0xFFEA4335),
    dayOnclick: () -> Unit = {}
) {


    var selectedDay by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    val context = LocalContext.current
    val currentMonth = LocalDate.now().month.value
    val currentYear = LocalDate.now().year
    val today = LocalDate.now().dayOfMonth
    val weekDays = context.resources.getStringArray(R.array.week_days)
    val month = remember { mutableIntStateOf(currentMonth) }
    val year = remember { mutableIntStateOf(currentYear) }
    val days = getMonthDays(year.intValue, month.intValue)
    val monthName = DateFormatSymbols(Locale.getDefault()).months[month.intValue - 1]

    println("month value int:"+month.intValue)
    println("year value int " +year.intValue)
    println("today" + today)
    val lazyState = rememberLazyGridState(
        initialFirstVisibleItemIndex = month.intValue,
        initialFirstVisibleItemScrollOffset = month.intValue
    )


    val selectedDayState = remember { mutableStateOf(selectedDay) }

    LaunchedEffect(selectedDay) {
        selectedDayState.value = selectedDay
    }
    Spacer(Modifier.padding(vertical = 10.dp))
    Column(modifier = Modifier.clip((RoundedCornerShape(15.dp))), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        //ay yıl başlık
        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(color = HeaderColor),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (month.intValue > 1) {
                    month.value -= 1
                } else {
                    month.intValue = 12
                    year.value -= 1 // Yılı düşür
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "",
                    tint = TitleColor
                )
            }
            Text(
                monthName + " ${if (currentYear == year.intValue) "" else year.intValue}",
                fontSize = 25.sp,
                color = TitleColor
            )
            IconButton(onClick = {
                if (month.intValue < 12) {
                    month.value += 1
                } else {
                    month.intValue = 1
                    year.value += 1 // Yılı artır
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "",
                    tint = TitleColor
                )
            }
        }
        // Haftanın günleri başlık
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = HeaderColor),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            weekDays.forEach { days ->
                Text(
                    text = days,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = WeekDaysColor
                )
            }
        }
        //takvim kısmı
            LazyVerticalGrid(
                modifier = Modifier
                    .background(color = ContainerColor),
                columns = GridCells.Fixed(weekDays.size),
                state = lazyState,
                contentPadding = PaddingValues(top = 8.dp)
            ) {

                items(days) { day ->
                    val parsedDay = day.toIntOrNull()
                    val markeDatesFormated = MarkedDays.groupBy { Pair(it.year,it.monthValue) }
                        .mapValues { entry -> entry.value.map{
                            it.dayOfMonth
                        }  }
                    val isMarked = markeDatesFormated[Pair(year.intValue,month.intValue)]?.contains(parsedDay) == true
                    //badge çağırılacak
                    Box(
                        Modifier
                            .padding(4.dp)
                            .aspectRatio(1f)
                            .clickable(enabled = day.isNotEmpty(), role = Role.RadioButton) {
                                selectedDay = Pair(day.trim().toIntOrNull() ?: 1, month.intValue)
                                Log.d("Calendar", day)
                                dayOnclick()
                            }, contentAlignment = Alignment.Center
                    ) {
                        if (day == today.toString() && month.intValue == currentMonth && year.intValue == currentYear) {
                            if (parsedDay != null && selectedDay?.first == parsedDay && selectedDay?.second == month.intValue) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            TodayBorderColor,
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .border(
                                            1.dp, TodayTextColor, shape = RoundedCornerShape(15.dp)
                                        ), contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            day, fontWeight = FontWeight.Bold, color = Color.White
                                        )
                                        if (isMarked){
                                            HexagonBadge(BadgeFirstColor,BadgeTwoColor)
                                        }
                                    }

                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .border(
                                            1.dp,
                                            TodayBorderColor,
                                            shape = RoundedCornerShape(15.dp)
                                        ), contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            day,
                                            fontWeight = FontWeight.Bold,
                                            color = TodayTextColor
                                        )
                                        if (isMarked){
                                        HexagonBadge(BadgeFirstColor,BadgeTwoColor)
                                    }

                                    }


                                }
                            }

                        } else if (parsedDay != null && selectedDay?.first == parsedDay && selectedDay?.second == month.intValue) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        1.dp,
                                        SelectedDayBorderColor,
                                        shape = RoundedCornerShape(15.dp)
                                    ), contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        day,
                                        fontWeight = FontWeight.Bold,
                                        color = SelectedDayTextColor
                                    )
                                    if (isMarked){
                                        HexagonBadge(BadgeFirstColor,BadgeTwoColor)
                                    }
                                }

                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Color.Transparent, shape = RoundedCornerShape(15.dp)
                                    )
                                    .border(
                                        0.3.dp,
                                        if (day.isNotEmpty()) CurrentDaysBorderColor else Color.Transparent,
                                        RoundedCornerShape(15.dp)
                                    ), contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        day,
                                        fontWeight = FontWeight.Bold,
                                        color = CurrentDaysTextColor
                                    )
                                    if (isMarked){
                                        HexagonBadge(BadgeFirstColor,BadgeTwoColor)
                                    }
                                }
                            }
                        }
                    }
                }

            }


    }
}

fun getMonthDays(year: Int, month: Int): List<String> {
    // Belirtilen yıl ve ay için YearMonth nesnesi oluştur
    val yearMonth = YearMonth.of(year, month)

    // Ayın ilk gününü al
    val firstDayOfMonth = yearMonth.atDay(1)

    // İlk günün haftanın hangi günü olduğunu bul (Pazartesi=1, Pazar=7)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value

    // Haftanın başlangıç gününü Pazartesi olarak ayarla
    val startDayOffset = (firstDayOfWeek - DayOfWeek.MONDAY.value + 7) % 7

    // Ayın toplam gün sayısını bul
    val daysInMonth = yearMonth.lengthOfMonth()

    val daysList = mutableListOf<String>()

    // İlk gün Pazartesi olacak şekilde boşlukları ekle
    for (i in 0 until startDayOffset) {
        daysList.add("")
    }

    // Ayın günlerini listeye ekle
    for (day in 1..daysInMonth) {
        daysList.add(day.toString())
    }

    return daysList
}

@Composable
fun HexagonBadge(badgeColorOne: Color, badgeColorTwo: Color) {
    Badge(
        modifier = Modifier
            .size(7.5.dp)
            .drawBehind {
                val path = Path()
                val radius = size.minDimension / 2
                val centerX = size.width / 2
                val centerY = size.height / 2
                val angle = 60f
                for (i in 0..5) {
                    val theta = Math.toRadians((angle * i).toDouble())
                    val x = (centerX + radius * cos(theta)).toFloat()
                    val y = (centerY + radius * sin(theta)).toFloat()
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                val brush = Brush.linearGradient(
                    listOf(
                        badgeColorOne, badgeColorTwo
                    )
                )
                path.close()
                drawPath(path = path, brush = brush)
            }, containerColor = Color.Transparent
    )
}