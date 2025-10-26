# Barber Project

Aplicación Android para gestión de citas de barbería desarrollada como proyecto de curso.

## 📱 Características

- **Autenticación con Google** - Login seguro con Firebase Authentication
- **Gestión de Citas en Tiempo Real** - Ver y administrar citas con actualización instantánea
- **Swipe para Cambiar Estado** - Desliza las citas para cambiar su estado
  - Pendiente → En proceso / Cancelada
  - En proceso → Completada / Cancelada
- **Filtros de Citas** - Ver citas activas, canceladas o completadas
- **Gestión de Clientes** - Lista de clientes con última interacción
- **Organización Automática** - Citas en proceso primero, pendientes después

## 🛠️ Tecnologías y Bibliotecas

### Core
- **Kotlin** - Lenguaje de programación
- **Jetpack Compose** - UI moderna y declarativa
- **Material 3** - Componentes de diseño Material Design

### Firebase
- **Firebase Authentication** - Autenticación de usuarios
- **Firebase Firestore** - Base de datos en tiempo real

### Arquitectura
- **ViewModel** - Gestión de estado y lógica de negocio
- **Repository Pattern** - Capa de datos
- **StateFlow** - Flujos reactivos para UI
- **Coroutines** - Programación asíncrona

### UI Components
- **Atomic Design** - Organización de componentes (Atoms, Molecules, Organisms, Screens)
- **SwipeToDismissBox** - Gestos de deslizamiento nativos de Material 3

## 📁 Estructura del Proyecto

```
app/src/main/java/com/cibertec/barber_project/
├── data/
│   ├── models/          # Modelos de datos (Appointment, Customer)
│   └── repositories/    # Repositorios para Firestore
├── viewmodels/          # ViewModels para lógica de negocio
└── ui/
    ├── components/
    │   ├── atoms/       # Componentes básicos
    │   └── molecules/   # Componentes compuestos
    ├── screens/         # Pantallas completas
    └── theme/           # Tema y estilos
```

## 🚀 Instalación

1. Clonar el repositorio
2. Abrir el proyecto en Android Studio
3. Agregar tu archivo `google-services.json` de Firebase en `app/`
4. Sincronizar Gradle
5. Ejecutar la aplicación

## 📊 Colecciones de Firestore

- `appointments` - Citas de barbería
  - id, userId, userName, phone, date, timeSlot, status, type, createdAt, updatedAt
  
- `chatbot_users` - Clientes registrados
  - id, name, phone, chat, createdAt, lastInteraction

## 🎓 Proyecto de Curso

Desarrollado para Cibertec como proyecto académico.

---

**Año:** 2025

