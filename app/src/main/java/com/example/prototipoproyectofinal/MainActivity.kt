package com.example.prototipoproyectofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
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
val NeonBlue = Color(0xFF2196F3)         // Azul actualizado
val NeonGreen = Color(0xFF00E676)        // Éxito
val LockedColor = Color(0xFF2D2D5A)      // Elementos bloqueados
val TextWhite = Color(0xFFFFFFFF)
val TextGray = Color(0xFF8F9BB3)
val NeonYellow = Color(0xFFDEA321)

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

                    AnimatedContent(
                        targetState = currentScreen,
                        label = "screen-transition",
                        transitionSpec = {
                            if (targetState.ordinal > initialState.ordinal) {
                                slideInHorizontally { width -> width } togetherWith slideOutHorizontally { width -> -width }
                            } else {
                                slideInHorizontally { width -> -width } togetherWith slideOutHorizontally { width -> width }
                            }
                        }
                    ) { screen ->
                        when (screen) {
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
        CourseData("3º Año", "Función Lineal y Sistemas", 0.4f, "🧪", true, cardColor = Color(0xFF483D8B)), // Activo
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
    val borderColor = if (data.progress >= 1f) NeonGreen else if (data.isActive) NeonYellow else Color.White.copy(alpha = 0.05f)
    val elevation = if (data.isActive) 16.dp else 0.dp
    val shadowColor = if (data.isActive) NeonYellow else Color.Transparent

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
    val barColor = if (isActive) NeonYellow else Color.Gray
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
        UnitData("1. Función Lineal", LessonState.ACTIVE),
        UnitData("2. Sist. Ecuaciones", LessonState.LOCKED),
        UnitData("3. Inecuaciones", LessonState.LOCKED), // Agregado como extra para completar diseño
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

    val borderColor = if (isActive) NeonYellow else Color.White.copy(alpha = 0.1f)

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
                    LessonState.ACTIVE -> NeonYellow
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
                    Text("Función Lineal", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Unidad 1", color = NeonYellow, fontSize = 12.sp)
                }
                IconButton(onClick = { isBookTheoryOpen = true }) {
                    Icon(Icons.AutoMirrored.Outlined.MenuBook, contentDescription = "Teoría", tint = TextWhite)
                }
            }

            // Mapa Gamificado
            LessonPath(onLec1Click = onLec1Click)
        }

        // Overlay de Teoría (Simple)
        AnimatedVisibility(
            visible = isBookTheoryOpen,
            enter = slideInHorizontally { width -> width },
            exit = slideOutHorizontally { width -> width }
        ) {
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
                        text = "Función Lineal",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Una función lineal es una relación entre dos variables (x, y) cuya gráfica es una línea recta. Su fórmula general es:\n" +
                                "y = mx + b\n\n" +
                                "Donde:\n" +
                                "• \'y\' es la variable dependiente.\n" +
                                "• \'x\' es la variable independiente.\n" +
                                "• \'m\' es la pendiente.\n" +
                                "• \'b\' es la ordenada al origen.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Pendiente (m)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = NeonYellow
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "La pendiente determina la inclinación de la recta.\n" +
                                "• Si m > 0, la recta es creciente (sube de izquierda a derecha).\n" +
                                "• Si m < 0, la recta es decreciente (baja de izquierda a derecha).\n" +
                                "• Si m = 0, la recta es horizontal.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Ordenada al Origen (b)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = NeonYellow
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Es el punto donde la recta corta el eje Y. Su coordenada es siempre (0, b).",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Raíz o Cero de la Función",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = NeonYellow
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Es el punto donde la recta corta el eje X. Se calcula igualando la función a cero y despejando x (y=0). Su coordenada es (-b/m, 0).",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Rectas Paralelas y Perpendiculares",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = NeonYellow
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Dadas dos rectas y = m1x + b1 e y = m2x + b2:\n" +
                                "• Son paralelas si sus pendientes son iguales (m1 = m2).\n" +
                                "• Son perpendiculares si sus pendientes son opuestas e inversas (m1 = -1/m2).",
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
        Point(0.dp, LessonState.COMPLETED, "1"),
        Point(60.dp, LessonState.COMPLETED, "2"),
        Point(30.dp, LessonState.ACTIVE, "3"),
        Point((-40).dp, LessonState.LOCKED, "4"),
        Point((-60).dp, LessonState.LOCKED, "5"),
        Point(0.dp, LessonState.LOCKED, "BOSS")
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        // Dibujamos la línea curva de fondo
        Canvas(modifier = Modifier.fillMaxSize()) {
            val lockedPathColor = Color(0xFF2D2D5A)
            val unlockedPathColor = NeonGreen
            val activePathColor = NeonYellow
            val strokeWidth = 8.dp.toPx()

            // Lógica simplificada de dibujo de línea (ZigZag vertical)
            val centerX = size.width / 2
            val startY = 100.dp.toPx()
            val stepY = 120.dp.toPx()

            for (i in 0 until lessons.size - 1) {
                val currentX = centerX + lessons[i].xOffset.toPx()
                val currentY = startY + (i * stepY)
                val nextX = centerX + lessons[i+1].xOffset.toPx()
                val nextY = startY + ((i+1) * stepY)

                val pathColor = when {
                    lessons[i].state == LessonState.COMPLETED && lessons[i+1].state == LessonState.ACTIVE -> activePathColor
                    lessons[i].state == LessonState.COMPLETED && lessons[i+1].state == LessonState.COMPLETED -> unlockedPathColor
                    else -> lockedPathColor
                }

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
            items(lessons) { lesson ->
                Box(
                    modifier = Modifier
                        .offset(x = lesson.xOffset)
                        .padding(bottom = 40.dp) // Espacio vertical entre nodos
                ) {
                    MapNode(lesson.state, lesson.label, onClick = { if (lesson.state == LessonState.ACTIVE) onLec1Click() })
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
        LessonState.ACTIVE -> NeonYellow
        LessonState.LOCKED -> LockedColor
    }

    val borderColor = when (state) {
        LessonState.ACTIVE -> Color.White
        LessonState.COMPLETED -> Color.White.copy(alpha = 0.7f)
        else -> Color.Transparent
    }

    val shadowRadius = if (state == LessonState.ACTIVE) 15.dp else 0.dp

    Box(
        modifier = Modifier
            .size(size)
            .shadow(shadowRadius, CircleShape, spotColor = color)
            .background(color, CircleShape)
            .border(4.dp, borderColor, CircleShape)
            .clickable(enabled = state != LessonState.LOCKED, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (state == LessonState.LOCKED) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White.copy(alpha = 0.3f))
        } else if (label == "BOSS") {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color.White)
        } else {
            Text(text = label, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = if(state==LessonState.ACTIVE) Color.Black else Color(0xFF121230))
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
        // Barra superior con botón de atrás y vidas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "❌",
                modifier = Modifier.clickable { onBackClick() },
                fontSize = 24.sp,
                color = TextWhite
            )
            Row {
                Icon(Icons.Filled.Favorite, contentDescription = "Life", tint = Color.Red)
                Icon(Icons.Filled.Favorite, contentDescription = "Life", tint = Color.Red)
                Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Lost life", tint = TextGray)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Pregunta
        Text("Calcula la pendiente:", color = TextGray, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("y = 3x - 2", color = TextWhite, fontSize = 48.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.weight(1f))

        // Opciones
        val options = listOf(3, -2, 2, -3)
        options.chunked(2).forEach { rowOptions ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                rowOptions.forEach { opt ->
                    val isSelected = selectedAnswer == opt
                    val color = if (isSelected && isSuccess) NeonGreen else if (isSelected) Color.Red else SurfaceColor

                    Button(
                        onClick = {
                            selectedAnswer = opt
                            if (opt == 3) isSuccess = true
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