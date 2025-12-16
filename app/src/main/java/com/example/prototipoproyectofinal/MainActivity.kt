package com.example.prototipoproyectofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prototipoproyectofinal.ui.theme.PrototipoProyectoFinalTheme

// Simple navigation states for this prototype
private enum class Screen {
    Courses,
    Units,
    Lessons,
    Exercise
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
                            onBackClick = { currentScreen = Screen.Units },
                            onLec1Click = { currentScreen = Screen.Exercise }
                        )

                        Screen.Exercise -> ExerciseScreen(
                            onBackClick = { currentScreen = Screen.Lessons }
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
    // Palette for Cursos page based on provided colors
    val deepBackground = Color(0xFF3C396E) // #3C396E first background
    val cardColor = Color(0xFF4F5CA4)      // #4F5CA4 card background

    // Buttons use #B3D7F5 now
    val buttonColor = Color(0xFFB3D7F5)    // #B3D7F5 shared button color
    val buttonTextColor = Color(0xFF264939) // dark green for contrast
    val titleColor = Color(0xFFB3D7F5)     // use same light blue for title

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(deepBackground)
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        // Section title at top-left
        Text(
            text = "Cursos",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            color = titleColor,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = cardColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // All buttons share the same #B3D7F5 background and dark text
                CoursePill(
                    text = "1er Año",
                    textColor = buttonTextColor,
                    onClick = onFirstYearClick,
                    large = true,
                    backgroundColor = buttonColor
                )

                CoursePill(
                    text = "2do Año",
                    textColor = buttonTextColor,
                    onClick = { /* TODO: navigate to 2do Año */ },
                    large = true,
                    backgroundColor = buttonColor
                )

                CoursePill(
                    text = "3er Año",
                    textColor = buttonTextColor,
                    onClick = { /* TODO: navigate to 3er Año */ },
                    large = true,
                    backgroundColor = buttonColor
                )

                CoursePill(
                    text = "4to Año",
                    textColor = buttonTextColor,
                    onClick = { /* TODO: navigate to 4to Año */ },
                    large = true,
                    backgroundColor = buttonColor
                )

                CoursePill(
                    text = "5to Año",
                    textColor = buttonTextColor,
                    onClick = { /* TODO: navigate to 5to Año */ },
                    large = true,
                    backgroundColor = buttonColor
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
    // Reuse the same background and card colors as CoursesScreen
    val deepBackground = Color(0xFF3C396E)
    val cardColor = Color(0xFF4F5CA4)
    val titleColor = Color(0xFFB3D7F5)
    val buttonColor = Color(0xFFB3D7F5)
    val buttonTextColor = Color(0xFF264939)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(deepBackground)
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "←",
                fontSize = 22.sp,
                color = titleColor,
                modifier = Modifier.clickable(onClick = onBackClick)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Unidades",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                color = titleColor
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = cardColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CoursePill(
                    text = "Unidad 1 - N° Enteros",
                    textColor = buttonTextColor,
                    onClick = onFirstUnitClick,
                    large = true,
                    backgroundColor = buttonColor
                )

                CoursePill(
                    text = "Unidad 2 - Ecuaciones",
                    textColor = buttonTextColor,
                    onClick = { /* TODO: navigate to Unidad 2 */ },
                    large = true,
                    backgroundColor = buttonColor
                )

                CoursePill(
                    text = "Unidad 3 - Geometría",
                    textColor = buttonTextColor,
                    onClick = { /* TODO: navigate to Unidad 3 */ },
                    large = true,
                    backgroundColor = buttonColor
                )
            }
        }
    }
}

@Composable
fun LessonsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onLec1Click: () -> Unit = {}
) {
    val deepBackground = Color(0xFF3C396E)
    val titleColor = Color(0xFFB3D7F5)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(deepBackground)
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
                color = titleColor,
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
            LessonPath(onLec1Click = onLec1Click)
        }
    }
}

@Composable
private fun LessonPath(
    modifier: Modifier = Modifier,
    onLec1Click: () -> Unit = {}
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
        LessonNode("Lec 1", points[5], boxWidth, boxHeight, onClick = onLec1Click)
    }
}

@Composable
private fun LessonNode(
    label: String,
    normalizedPos: Offset,
    boxWidth: Dp,
    boxHeight: Dp,
    onClick: (() -> Unit)? = null
) {
    val circleSize = 64.dp

    Box(
        modifier = Modifier
            .offset(
                x = boxWidth * normalizedPos.x - circleSize / 2,
                y = boxHeight * normalizedPos.y - circleSize / 2
            )
            .size(circleSize)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
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
fun ExerciseScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val correctAnswer = 5
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var lastWrongAnswer by remember { mutableStateOf<Int?>(null) }

    val isCorrectAnswered = selectedAnswer == correctAnswer

    // Reset wrong answer feedback after a short delay
    LaunchedEffect(lastWrongAnswer) {
        if (lastWrongAnswer != null) {
            kotlinx.coroutines.delay(600)
            lastWrongAnswer = null
        }
    }

    val deepBackground = Color(0xFF3C396E)
    val titleColor = Color(0xFFB3D7F5)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(deepBackground)
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
                color = titleColor,
                modifier = Modifier.clickable(onClick = onBackClick)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Selecciona el recuadro correcto",
            style = MaterialTheme.typography.titleMedium,
            color = titleColor,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Animate the equation when the correct answer is revealed
        val equationScale by animateFloatAsState(
            targetValue = if (isCorrectAnswered) 1.05f else 1f,
            label = "equationScale"
        )
        val equationAlpha by animateFloatAsState(
            targetValue = if (isCorrectAnswered) 1f else 0.9f,
            label = "equationAlpha"
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .graphicsLayer(
                    scaleX = equationScale,
                    scaleY = equationScale,
                    alpha = equationAlpha
                ),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF4F5CA4),
            shadowElevation = 0.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isCorrectAnswered) "2 + 3 = 5" else "2 + 3 = ?",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        val answers = listOf(5, 6, 7, 8)

        answers.forEach { value ->
            val isCorrect = value == correctAnswer
            val isSelectedCorrect = selectedAnswer == value && isCorrect
            val isWrong = lastWrongAnswer == value

            AnswerButton(
                value = value,
                isCorrectSelected = isSelectedCorrect,
                isWrong = isWrong,
                enabled = !isCorrectAnswered
            ) {
                if (isCorrectAnswered) return@AnswerButton

                if (isCorrect) {
                    selectedAnswer = value
                } else {
                    lastWrongAnswer = value
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun AnswerButton(
    value: Int,
    isCorrectSelected: Boolean,
    isWrong: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    // Use #8DBC69 for correct, #C76562 for wrong, base dark for neutral
    val baseColor = Color(0xFF333333)
    val correctColor = Color(0xFF8DBC69)
    val wrongColor = Color(0xFFC76562)

    val targetColor = when {
        isCorrectSelected -> correctColor
        isWrong -> wrongColor
        else -> baseColor
    }

    val backgroundColor by animateColorAsState(targetValue = targetColor, label = "answerColor")

    val targetScale = if (isCorrectSelected) 1.05f else 1f
    val scale by animateFloatAsState(targetValue = targetScale, label = "answerScale")

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .then(
                if (enabled) Modifier.clickable(onClick = onClick) else Modifier
            ),
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}

@Composable
fun CoursePill(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
    large: Boolean = false,
    backgroundColor: Color = Color(0xFFE0E0E0),
    onClick: () -> Unit
) {
    val minHeight = if (large) 60.dp else 40.dp
    val textStyle = if (large) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = backgroundColor,
        shadowElevation = if (large) 3.dp else 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = if (large) 16.dp else 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = textStyle.copy(fontWeight = FontWeight.SemiBold),
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExerciseScreenPreview() {
    PrototipoProyectoFinalTheme {
        ExerciseScreen()
    }
}