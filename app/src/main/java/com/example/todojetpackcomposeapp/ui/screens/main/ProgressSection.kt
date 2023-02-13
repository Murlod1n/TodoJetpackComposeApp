package com.example.todojetpackcomposeapp.ui.screens.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todojetpackcomposeapp.R
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProgressSection(
    modifier: Modifier = Modifier,
    allTasksCount: Int,
    allCheckedTasksCount: Int,
    navigateToListScreen: () -> Unit
) {

    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val percentage = allCheckedTasksCount.toFloat() / allTasksCount.toFloat()
    val percentageCheckedTasks: Float = if (percentage.isNaN()) 0f else percentage

    val curPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentageCheckedTasks else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 0
        )
    )

    val date = LocalDate.now()

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Box(modifier = modifier.clip(MaterialTheme.shapes.small)) {
        Row(
            modifier = modifier
                .background(Color(0xFFFDFFE7))
                .padding(10.dp)
                .height(IntrinsicSize.Min)
        ) {
            Column(modifier = modifier.weight(1f)) {
                Text(
                    text = "${date.dayOfMonth} ${date.month}",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "You've made significant progress",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = modifier.padding(vertical = 6.dp))
                Text(
                    modifier = modifier.padding(vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall,
                    text = "Completed $allCheckedTasksCount out of $allTasksCount task"
                )
                LinearProgressIndicator(
                    modifier = modifier
                        .height(15.dp)
                        .clip(RoundedCornerShape(25.dp)),
                    color = Color(0xFFF0DC28),
                    progress = curPercentage.value
                )
                Button(
                    modifier = modifier.padding(vertical = 8.dp),
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1D1A)),
                    onClick = navigateToListScreen
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        text = "View all task"
                    )
                }
            }
            Column(modifier = modifier.weight(1f)) {
                Image(
                    painter = painterResource(id = R.drawable.editor),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}