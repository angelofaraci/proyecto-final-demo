package com.example.prototipoproyectofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prototipoproyectofinal.ui.theme.PrototipoProyectoFinalTheme
import kotlinx.coroutines.delay

// --- PALETA DE COLORES "GAMER/NEÓN" ---
val DarkBackground = Color(0xFF0B0B1E)   // Fondo casi negro/azul
val SurfaceColor = Color(0xFF1E1E3F)     // Color de las tarjetas
val NeonBlue = Color(0xFF00BFFF)         // Color principal activo (Cyan eléctrico)
val NeonGreen = Color(0xFF00E676)        // Éxito
val LockedColor = Color(0xFF2D2D5A)      // Elementos bloqueados
val TextWhite = Color(0xFFFFFFFF)
val TextGray = Color(0xFF8F9BB3)

// --- DATA CLASSES SIMPLES ---
data class CourseData(val title: String, val desc: String, val progress: Float, val icon: String, val isActive: Boolean, val cardColor: Color? = null)
data class UnitData(val title: String, val state: LessonState)

// Simple navigation states
enum class Screen { Courses, Units, Lessons, Exercise }
enum class LessonState { COMPLETED, ACTIVE, LOCKED }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrototipoProyectoFinalTheme {
                // Fondo global oscuro con gradiente sutil
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF121230), Color(0xFF050510))
                            )
                        )
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
                            onBackClick = { currentScreen = Screen.Lessons },
                            onCorrectAnswered = { currentScreen = Screen.Lessons }
                        )
                    }
                }
            }
        }
    }
}

// --- PANTALLA 1: SELECCIÓN DE AÑO (COURSES) ---
@Composable
fun CoursesScreen(
    modifier: Modifier = Modifier,
    onFirstYearClick: () -> Unit = {}
) {
    val courses = listOf(
        CourseData("1º Año", "Álgebra y Geometría", 1.0f, "📐", false, cardColor = Color(0xFF1E3F35)),
        CourseData("2º Año", "Funciones Básicas", 1.0f, "📈", false, cardColor = Color(0xFF1E3F35)),
        CourseData("3º Año", "Ecuaciones Cuadráticas", 0.4f, "🧪", true, cardColor = Color(0xFF483D8B)), // Activo
        CourseData("4º Año", "Trigonometría", 0.0f, "📐", false),
        CourseData("5º Año", "Cálculo y Límites", 0.0f, "♾️", false)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Seleccioná tu Año",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = TextWhite
        )
        Text(
            text = "Continuá tu camino al éxito",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray
        )
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(courses) { course ->
                CourseCard(
                    data = course,
                    onClick = { if (course.isActive) onFirstYearClick() else { /* Opcional: Toast bloqueado */ } }
                )
            }
        }
    }
}

@Composable
fun CourseCard(data: CourseData, onClick: () -> Unit) {
    val borderColor = if (data.progress >= 1f) NeonGreen else if (data.isActive) NeonBlue else Color.White.copy(alpha = 0.05f)
    val elevation = if (data.isActive) 16.dp else 0.dp
    val shadowColor = if (data.isActive) NeonBlue else Color.Transparent

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .shadow(elevation, RoundedCornerShape(24.dp), spotColor = shadowColor)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = data.cardColor ?: (if (data.isActive) SurfaceColor else LockedColor.copy(alpha = 0.5f)),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono Circular
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFF2D2D5A), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = data.icon, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (data.isActive) TextWhite else TextGray
                )
                Text(
                    text = data.desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Barra de progreso
                NeonProgressBar(progress = data.progress, isActive = data.isActive)
            }
        }
    }
}

@Composable
fun NeonProgressBar(progress: Float, isActive: Boolean) {
    val barColor = if (isActive) NeonBlue else Color.Gray
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .background(barColor, CircleShape)
        )
    }
}

// --- PANTALLA 2: SELECCIÓN DE UNIDAD (UNITS) ---
@Composable
fun UnitsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onFirstUnitClick: () -> Unit
) {
    val units = listOf(
        UnitData("1. Números Enteros", LessonState.COMPLETED),
        UnitData("2. Ecuaciones", LessonState.ACTIVE),
        UnitData("3. Geometría", LessonState.LOCKED),
        UnitData("4. Polinomios", LessonState.LOCKED)
    )

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Spacer(modifier = Modifier.height(48.dp))
        // Header con botón atrás
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "←",
                fontSize = 28.sp,
                color = TextWhite,
                modifier = Modifier.clickable(onClick = onBackClick)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Unidades",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = TextWhite
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(units) { unit ->
                UnitRow(
                    unit = unit,
                    onClick = {
                        if (unit.state != LessonState.LOCKED) onFirstUnitClick()
                    }
                )
            }
        }
    }
}

@Composable
fun UnitRow(unit: UnitData, onClick: () -> Unit) {
    val isActive = unit.state == LessonState.ACTIVE
    val isLocked = unit.state == LessonState.LOCKED

    val backgroundColor = when (unit.state) {
        LessonState.ACTIVE -> SurfaceColor
        LessonState.COMPLETED -> Color(0xFF1E3F35) // Un verde oscuro muy sutil
        LessonState.LOCKED -> Color.Transparent
    }

    val borderColor = if (isActive) NeonBlue else Color.White.copy(alpha = 0.1f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(enabled = !isLocked, onClick = onClick)
            .graphicsLayer {
                if(isActive) { shadowElevation = 10f }
            },
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = unit.title,
                color = if (isLocked) TextGray.copy(alpha = 0.5f) else TextWhite,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                fontSize = 16.sp
            )

            // Icono de estado
            Icon(
                imageVector = when(unit.state) {
                    LessonState.COMPLETED -> Icons.Default.CheckCircle
                    LessonState.ACTIVE -> Icons.Default.PlayArrow
                    LessonState.LOCKED -> Icons.Default.Lock
                },
                contentDescription = null,
                tint = when(unit.state) {
                    LessonState.COMPLETED -> NeonGreen
                    LessonState.ACTIVE -> NeonBlue
                    LessonState.LOCKED -> TextGray.copy(alpha = 0.5f)
                }
            )
        }
    }
}

// --- PANTALLA 3: MAPA DE LECCIONES (LESSONS) ---
@Composable
fun LessonsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onLec1Click: () -> Unit
) {
    // Variable para abrir la teoría
    var isBookTheoryOpen by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Transparente
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "←", fontSize = 28.sp, color = TextWhite,
                    modifier = Modifier.clickable(onClick = onBackClick)
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("N° Enteros", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Unidad 1", color = NeonBlue, fontSize = 12.sp)
                }
                IconButton(onClick = { isBookTheoryOpen = true }) {
                    Icon(Icons.AutoMirrored.Outlined.MenuBook, contentDescription = "Teoría", tint = TextWhite)
                }
            }

            // Mapa Gamificado
            LessonPath(onLec1Click = onLec1Click)
        }

        // Overlay de Teoría (Simple)
        if (isBookTheoryOpen) {
            TheoryOverlay(onClose = { isBookTheoryOpen = false })
        }
    }
}

@Composable
fun TheoryOverlay(onClose: () -> Unit) {
    // Simple full-screen overlay (prototype-friendly)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DarkBackground
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
                    color = TextWhite,
                    modifier = Modifier.clickable { onClose() }
                )

                Text(
                    text = "Teoría",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = TextWhite
                )

                // symmetrical spacer to keep title centered-ish
                Spacer(modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceColor),
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

@Composable
private fun LessonPath(onLec1Click: () -> Unit) {
    // Definimos los puntos del camino
    data class Point(val xOffset: Dp, val state: LessonState, val label: String)

    val lessons = listOf(
        Point(0.dp, LessonState.ACTIVE, "1"),
        Point(60.dp, LessonState.LOCKED, "2"),
        Point(30.dp, LessonState.LOCKED, "3"),
        Point((-40).dp, LessonState.LOCKED, "4"),
        Point((-60).dp, LessonState.LOCKED, "5"),
        Point(0.dp, LessonState.LOCKED, "BOSS")
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        // Dibujamos la línea curva de fondo
        Canvas(modifier = Modifier.fillMaxSize()) {
            val pathColor = Color(0xFF2D2D5A)
            val strokeWidth = 8.dp.toPx()

            // Lógica simplificada de dibujo de línea (ZigZag vertical)
            // En una app real usaríamos Path cubicTo para curvas suaves
            val centerX = size.width / 2
            val startY = 100.dp.toPx()
            val stepY = 120.dp.toPx()

            for (i in 0 until lessons.size - 1) {
                val currentX = centerX + lessons[i].xOffset.toPx()
                val currentY = startY + (i * stepY)
                val nextX = centerX + lessons[i+1].xOffset.toPx()
                val nextY = startY + ((i+1) * stepY)

                drawLine(
                    color = pathColor,
                    start = Offset(currentX, currentY),
                    end = Offset(nextX, nextY),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
        }

        // Dibujamos los nodos interactivos
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 80.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(lessons) { index, lesson ->
                Box(
                    modifier = Modifier
                        .offset(x = lesson.xOffset)
                        .padding(bottom = 40.dp) // Espacio vertical entre nodos
                ) {
                    MapNode(lesson.state, lesson.label, onClick = { if(index == 0) onLec1Click() })
                }
            }
        }
    }
}

@Composable
fun MapNode(state: LessonState, label: String, onClick: () -> Unit) {
    val size = 70.dp
    // Colores dinámicos
    val color = when (state) {
        LessonState.COMPLETED -> NeonGreen
        LessonState.ACTIVE -> NeonBlue
        LessonState.LOCKED -> LockedColor
    }

    val shadowRadius = if (state == LessonState.ACTIVE) 15.dp else 0.dp

    Box(
        modifier = Modifier
            .size(size)
            .shadow(shadowRadius, CircleShape, spotColor = color)
            .background(color, CircleShape)
            .border(4.dp, if (state == LessonState.ACTIVE) Color.White else Color.Transparent, CircleShape)
            .clickable(enabled = state != LessonState.LOCKED, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (state == LessonState.LOCKED) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White.copy(alpha = 0.3f))
        } else if (label == "BOSS") {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color.White)
        } else {
            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = if(state==LessonState.ACTIVE) Color.White else Color(0xFF121230))
        }
    }
}

// --- PANTALLA 4: EJERCICIO (GAMEPLAY) ---
@Composable
fun ExerciseScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onCorrectAnswered: () -> Unit
) {
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var isSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            delay(1500)
            onCorrectAnswered()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barra superior
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("❌", modifier = Modifier.clickable { onBackClick() }, fontSize = 24.sp)
            NeonProgressBar(progress = 0.5f, isActive = true) // Barra de vida/progreso
            Text("❤️ 3", color = Color.Red, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Pregunta
        Text("Resuelve:", color = TextGray, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("2 + 3 = ?", color = TextWhite, fontSize = 48.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.weight(1f))

        // Opciones
        val options = listOf(4, 5, 6, 8)
        options.chunked(2).forEach { rowOptions ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                rowOptions.forEach { opt ->
                    val isSelected = selectedAnswer == opt
                    val color = if (isSelected && isSuccess) NeonGreen else if (isSelected) Color.Red else SurfaceColor

                    Button(
                        onClick = {
                            selectedAnswer = opt
                            if (opt == 5) isSuccess = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = color),
                        elevation = ButtonDefaults.buttonElevation(8.dp)
                    ) {
                        Text(text = opt.toString(), fontSize = 24.sp, color = TextWhite)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(modifier = Modifier.height(32.dp))
    }

    // Success Overlay simple
    if (isSuccess) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)), contentAlignment = Alignment.Center) {
            Text("¡CORRECTO!", color = NeonGreen, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// --- PREVIEWS ---
@Preview(showBackground = true)
@Composable
fun PreviewGamerDesign() {
    PrototipoProyectoFinalTheme {
        CoursesScreen()
    }
}