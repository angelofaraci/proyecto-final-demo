package com.example.prototipoproyectofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.BoxWithConstraints
import com.example.prototipoproyectofinal.ui.theme.PrototipoProyectoFinalTheme

// Simple navigation states for this prototype
private enum class Screen {
    Courses,
    Units,
    Lessons
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrototipoProyectoFinalTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    color = Color.Transparent
                ) {
                    var currentScreen by remember { mutableStateOf(Screen.Courses) }

                    when (currentScreen) {
                        Screen.Courses -> CoursesScreen(
                            onFirstYearClick = { currentScreen = Screen.Units }
                        )

                        Screen.Units -> UnitsScreen(
                            onBackClick = { currentScreen = Screen.Courses },
                            onFirstUnitClick = { currentScreen = Screen.Lessons }
                        )

                        Screen.Lessons -> LessonsScreen(
                            onBackClick = { currentScreen = Screen.Units }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CoursesScreen(
    modifier: Modifier = Modifier,
    onFirstYearClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF5F5F5)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Cursos",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                CoursePill(
                    text = "Matemáticas - 1er Año",
                    textColor = Color(0xFFD32F2F),
                    onClick = onFirstYearClick
                )

                Spacer(modifier = Modifier.height(8.dp))

                CoursePill(
                    text = "Matemáticas - 2do Año",
                    onClick = { /* TODO: navigate to 2do Año */ }
                )

                Spacer(modifier = Modifier.height(8.dp))

                CoursePill(
                    text = "Matemáticas - 3er Año",
                    onClick = { /* TODO: navigate to 3er Año */ }
                )
            }
        }
    }
}

@Composable
fun UnitsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onFirstUnitClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF5F5F5)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "← Volver",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6200EE),
                    modifier = Modifier
                        .clickable(onClick = onBackClick)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Unidades",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                CoursePill(
                    text = "Unidad 1 - N° Enteros",
                    textColor = Color(0xFFD32F2F),
                    onClick = onFirstUnitClick
                )

                Spacer(modifier = Modifier.height(8.dp))

                CoursePill(
                    text = "Unidad 2 - Ecuaciones",
                    onClick = { /* TODO: navigate to Unidad 2 */ }
                )

                Spacer(modifier = Modifier.height(8.dp))

                CoursePill(
                    text = "Unidad 3 - Geometría",
                    onClick = { /* TODO: navigate to Unidad 3 */ }
                )
            }
        }
    }
}

@Composable
fun LessonsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "←",
                fontSize = 24.sp,
                modifier = Modifier.clickable(onClick = onBackClick)
            )

            // Right-side icon placeholder (stats icon) can be added later if desired
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            LessonPath()
        }
    }
}

@Composable
private fun LessonPath(
    modifier: Modifier = Modifier
) {
    // Normalized (0f..1f) positions along the path
    val points = listOf(
        Offset(0.5f, 0.1f),  // top start of vertical segment
        Offset(0.5f, 0.25f), // corner for Lec 5
        Offset(0.3f, 0.35f), // corner for Lec 4
        Offset(0.55f, 0.5f), // corner for Lec 3
        Offset(0.3f, 0.7f),  // corner for Lec 2
        Offset(0.55f, 0.9f)  // corner for Lec 1
    )

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val boxWidth = maxWidth
        val boxHeight = maxHeight

        // Draw the polyline
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val strokeWidth = 2.dp.toPx()

            val absPoints = points.map { Offset(it.x * w, it.y * h) }

            for (i in 0 until absPoints.lastIndex) {
                drawLine(
                    color = Color.Black,
                    start = absPoints[i],
                    end = absPoints[i + 1],
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
        }

        // Place lesson nodes centered at each vertex (skip very top index 0)
        LessonNode("Lec 5", points[1], boxWidth, boxHeight)
        LessonNode("Lec 4", points[2], boxWidth, boxHeight)
        LessonNode("Lec 3", points[3], boxWidth, boxHeight)
        LessonNode("Lec 2", points[4], boxWidth, boxHeight)
        LessonNode("Lec 1", points[5], boxWidth, boxHeight)
    }
}

@Composable
private fun LessonNode(
    label: String,
    normalizedPos: Offset,
    boxWidth: Dp,
    boxHeight: Dp
) {
    val circleSize = 64.dp

    Box(
        modifier = Modifier
            .offset(
                x = boxWidth * normalizedPos.x - circleSize / 2,
                y = boxHeight * normalizedPos.y - circleSize / 2
            )
            .size(circleSize)
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFE0E0E0)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = label, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun CoursePill(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = Color(0xFFE0E0E0),
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CoursesScreenPreview() {
    PrototipoProyectoFinalTheme {
        CoursesScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UnitsScreenPreview() {
    PrototipoProyectoFinalTheme {
        UnitsScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LessonsScreenPreview() {
    PrototipoProyectoFinalTheme {
        LessonsScreen()
    }
}
