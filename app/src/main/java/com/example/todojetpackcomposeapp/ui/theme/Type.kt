package com.example.todojetpackcomposeapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.todojetpackcomposeapp.R


val roboto_light = FontFamily(Font(R.font.roboto_light))
val roboto_regular = FontFamily(Font(R.font.roboto_regular))
val roboto_medium = FontFamily(Font(R.font.roboto_medium))
val roboto_medium_italic = FontFamily(Font(R.font.roboto_medium_italic))
val roboto_bold = FontFamily(Font(R.font.roboto_bold))
val roboto_black = FontFamily(Font(R.font.roboto_black))
val roboto_black_italic = FontFamily(Font(R.font.roboto_black_italic))

val color = Color(0xFF1E1D1A)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = roboto_black,
        fontSize = 26.sp,
        color = color
    ),
    headlineLarge = TextStyle(
        fontFamily = roboto_black_italic,
        fontSize = 24.sp,
        color = color
    ),
    headlineSmall = TextStyle(
        fontFamily = roboto_bold,
        fontSize = 18.sp,
        color = color
    ),
    titleLarge = TextStyle(
        fontFamily = roboto_medium_italic,
        fontSize = 16.sp,
        color = color
    ),
    titleMedium = TextStyle(
        fontFamily = roboto_medium,
        fontSize = 16.sp,
        color = color
    ),
    labelLarge = TextStyle(
        fontFamily = roboto_regular,
        fontSize = 14.sp,
        color = color
    ),
    labelMedium = TextStyle(
        fontFamily = roboto_light,
        fontSize = 10.sp,
        color = color
    ),
    bodyMedium = TextStyle(
        fontFamily = roboto_regular,
        fontSize = 14.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = roboto_regular,
        fontSize = 10.sp,
        color = color
    ),
    displayMedium = TextStyle(
        fontFamily = roboto_medium,
        fontSize = 14.sp,
        color = color
    ),
    displaySmall = TextStyle(
        fontFamily = roboto_medium,
        fontSize = 10
            .sp,
        color = color
    )
)