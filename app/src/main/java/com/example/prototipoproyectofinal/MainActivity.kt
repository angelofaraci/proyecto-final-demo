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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onLec1Click: () -> Unit = {}
) {
    val deepBackground = Color(0xFF3C396E)
    val titleColor = Color(0xFFB3D7F5)
    val cardColor = Color(0xFF4F5CA4)

    // State for the full screen theory overlay
    var isBookTheoryOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(deepBackground)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        // Back only
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "←",
                fontSize = 24.sp,
                color = titleColor,
                modifier = Modifier.clickable(onClick = onBackClick)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Unit title + book icon aligned on the same row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Unidad 1 - N° Enteros",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                color = titleColor
            )

            Icon(
                imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                contentDescription = "Libro",
                tint = titleColor,
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        // FIRST click opens it full screen (no partial sheet)
                        isBookTheoryOpen = true
                    }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Progreso de lecciones",
            style = MaterialTheme.typography.bodyMedium,
            color = titleColor.copy(alpha = 0.85f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Card container for the path to match the Cursos/Unidades style
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                LessonPath(onLec1Click = onLec1Click)
            }
        }
    }

    if (isBookTheoryOpen) {
        // Simple full-screen overlay (prototype-friendly)
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = deepBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                // Top bar: back/close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "←",
                        fontSize = 24.sp,
                        color = titleColor,
                        modifier = Modifier.clickable { isBookTheoryOpen = false }
                    )

                    Text(
                        text = "Teoría",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = titleColor
                    )

                    // symmetrical spacer to keep title centered-ish
                    Spacer(modifier = Modifier.size(24.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "Números enteros",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Los números enteros (\"Z\") son un conjunto de números que incluye:\n" +
                                "• Los números naturales: 0, 1, 2, 3, …\n" +
                                "• Sus opuestos (negativos): −1, −2, −3, …\n" +
                                "\n" +
                                "Se usan para representar cantidades con dirección o sentido: ganancias/pérdidas, temperaturas bajo cero, pisos bajo tierra, etc.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Recta numérica y comparación",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "En la recta numérica, hacia la derecha los números son mayores y hacia la izquierda son menores.\n" +
                                "Por eso: −3 < −1 < 0 < 2 < 5.\n" +
                                "Ojo: entre números negativos, el que está más cerca de 0 es el mayor (por ejemplo, −2 es mayor que −7).",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Valor absoluto",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "El valor absoluto de un entero es su distancia al 0, sin importar el signo.\n" +
                                "Se escribe |a|. Ejemplos: |−4| = 4, |3| = 3, |0| = 0.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Suma de enteros",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "1) Si tienen el mismo signo, se suman los valores absolutos y se conserva el signo.\n" +
                                "   • (−3) + (−5) = −(3 + 5) = −8\n" +
                                "   • 4 + 6 = 10\n" +
                                "\n" +
                                "2) Si tienen distinto signo, se restan los valores absolutos y se coloca el signo del número con mayor valor absoluto.\n" +
                                "   • (−7) + 2 = −(7 − 2) = −5\n" +
                                "   • 9 + (−4) = 9 − 4 = 5",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Resta de enteros",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Restar un número es lo mismo que sumar su opuesto:\n" +
                                "a − b = a + (−b).\n" +
                                "Ejemplos:\n" +
                                "• 5 − 8 = 5 + (−8) = −3\n" +
                                "• (−2) − (−6) = (−2) + 6 = 4",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Multiplicación y división (regla de los signos)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "• (+) × (+) = (+)\n" +
                                "• (−) × (−) = (+)\n" +
                                "• (+) × (−) = (−)\n" +
                                "• (−) × (+) = (−)\n" +
                                "\n" +
                                "La misma regla se aplica a la división.\n" +
                                "Ejemplos:\n" +
                                "• (−3) × 4 = −12\n" +
                                "• (−20) ÷ (−5) = 4",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Consejo práctico",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Cuando tengas dudas, dibuja una recta numérica o piensa en situaciones reales (por ejemplo, deudas y dinero). Eso ayuda a entender el signo y el resultado.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

// Simple state for lesson node styling
private enum class LessonState {
    COMPLETED,
    CURRENT,
    LOCKED
}

@Composable
private fun LessonPath(
    modifier: Modifier = Modifier,
    onLec1Click: () -> Unit = {}
) {
    data class LessonPoint(
        val label: String,
        val xFraction: Float,
        val yFraction: Float,
        val state: LessonState
    )

    // yFraction increases downward. 1 is near the bottom; higher numbers are above,
    // so you scroll UP to see earlier lessons. Added a bit more vertical range so
    // the top-most lesson isn't visually cut off.
    val lessonPoints = listOf(
        LessonPoint("10", 0.18f, 0.06f, LessonState.COMPLETED),
        LessonPoint("9",  0.82f, 0.14f, LessonState.COMPLETED),
        LessonPoint("8",  0.50f, 0.24f, LessonState.COMPLETED),
        LessonPoint("7",  0.18f, 0.34f, LessonState.COMPLETED),
        LessonPoint("6",  0.82f, 0.44f, LessonState.COMPLETED),
        LessonPoint("5",  0.50f, 0.54f, LessonState.COMPLETED),
        LessonPoint("4",  0.18f, 0.64f, LessonState.COMPLETED),
        LessonPoint("3",  0.82f, 0.74f, LessonState.COMPLETED),
        LessonPoint("2",  0.50f, 0.84f, LessonState.COMPLETED),
        LessonPoint("1",  0.18f, 0.94f, LessonState.CURRENT)
    )

    val scrollState = rememberScrollState()

    // Fixed container height used for drawing + node placement.
    val containerHeight = 950.dp

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(containerHeight)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val widthPx = size.width
                    val heightPx = size.height
                    val strokeWidth = 3.dp.toPx()

                    val pointsPx = lessonPoints.map { lp ->
                        Offset(
                            x = lp.xFraction * widthPx,
                            y = lp.yFraction * heightPx
                        )
                    }

                    for (i in 0 until pointsPx.lastIndex) {
                        drawLine(
                            color = Color(0xFFB3D7F5),
                            start = pointsPx[i],
                            end = pointsPx[i + 1],
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                }

                // We can't know the exact parent width in dp here without BoxWithConstraints.
                // Use a conservative width that matches the typical card content area; it still
                // positions nicely because xFraction is relative.
                val approxContainerWidth = 320.dp

                lessonPoints.forEach { lp ->
                    val isCurrent = lp.state == LessonState.CURRENT
                    val click = if (isCurrent) onLec1Click else null

                    LessonNode(
                        label = lp.label,
                        centerX = approxContainerWidth * lp.xFraction,
                        centerY = containerHeight * lp.yFraction,
                        state = lp.state,
                        onClick = click
                    )
                }
            }
        }
    }
}

@Composable
private fun LessonNode(
    label: String,
    centerX: Dp,
    centerY: Dp,
    state: LessonState,
    onClick: (() -> Unit)? = null
) {
    val baseSize = 64.dp
    val currentSize = 72.dp
    val circleSize = if (state == LessonState.CURRENT) currentSize else baseSize

    val backgroundColor: Color
    val borderColor: Color
    val textColor: Color

    when (state) {
        LessonState.COMPLETED -> {
            backgroundColor = Color(0xFFB3D7F5) // soft blue for completed
            borderColor = Color.Transparent
            textColor = Color(0xFF3C396E)
        }
        LessonState.CURRENT -> {
            backgroundColor = Color(0xFFC3DF83) // green highlight for current
            borderColor = Color.White
            textColor = Color(0xFF3C396E)
        }
        LessonState.LOCKED -> {
            backgroundColor = Color(0xFF4F5CA4) // same as card but slightly accented
            borderColor = Color(0xFFB3D7F5)
            textColor = Color(0xFFB3D7F5)
        }
    }

    val clickableModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    // Only size and offset this node; do NOT fill the whole parent.
    Box(
        modifier = Modifier
            .offset(x = centerX - circleSize / 2, y = centerY - circleSize / 2)
            .size(circleSize)
            .then(clickableModifier)
    ) {
        Surface(
            shape = CircleShape,
            color = backgroundColor,
            border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
            shadowElevation = if (state == LessonState.CURRENT) 6.dp else 2.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = textColor
                )
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