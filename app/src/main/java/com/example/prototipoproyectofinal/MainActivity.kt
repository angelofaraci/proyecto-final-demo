package com.example.prototipoproyectofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prototipoproyectofinal.ui.theme.PrototipoProyectoFinalTheme

// --- PALETA DE COLORES "GAMER/NEÓN" ---
val DarkBackground = Color(0xFF0B0B1E)   // Fondo casi negro/azul
val SurfaceColor = Color(0xFF1E1E3F)     // Color de las tarjetas
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
        installSplashScreen()
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
                                "• 'y' es la variable dependiente.\n" +
                                "• 'x' es la variable independiente.\n" +
                                "• 'm' es la pendiente.\n" +
                                "• 'b' es la ordenada al origen.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray
                    )
                }
            }
        }
    }
}

@Composable
fun LessonPath(onLec1Click: () -> Unit) {
    // Placeholder implementation
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = onLec1Click) {
            Text("Go to Exercise")
        }
    }
}

@Composable
fun ExerciseScreen(onBackClick: () -> Unit, onCorrectAnswered: () -> Unit) {
    // Placeholder implementation
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Exercise Screen", color = TextWhite)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onCorrectAnswered) {
            Text("Submit Answer")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBackClick) {
            Text("Back to Lessons")
        }
    }
}
