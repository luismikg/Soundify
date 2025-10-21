# Soundify

Muestra artistas, álbumes y canciones usando la **Spotify Web API**.  
Desarrollada en **Kotlin**, **Jetpack Compose**, **MVVM** y **Clean Architecture**.

![myfile](https://github.com/luismikg/Soundify/blob/main/Soundify.gif)

---

## Tecnologías usadas

- **Kotlin** – lenguaje principal del proyecto.
- **Jetpack Compose** – para la UI declarativa.
- **MVVM** – patrón de arquitectura.
- **Clean Architecture** – separación de capas y responsabilidades.
- **Coroutines** – para procesos asíncronos.
- **Retrofit** – para consumir la API de Spotify.
- **Hilt** – para inyección de dependencias.
- **StateFlow / Flow** – manejo de estado reactivo.
- **EncryptedSharedPreferences** – almacenamiento seguro de tokens.
- **Figma** – diseño de la interfaz gráfica ([Musium Music App UI](https://www.figma.com/design/2Jv2554QofJiOcspngIAD2/Musium---Music-App-UI--Community-)).

---

## Índice
1. Requisitos (hardware/software)
2. Instalación paso a paso (desde cero)
3. Registro y configuración en Spotify Developer Dashboard
4. Configurar el proyecto (variables locales y gradle)
5. Ejecutar la app (emulador o dispositivo)
6. Flujo de autenticación (PKCE) y seguridad de tokens
7. Endpoints importantes usados
8. Estructura del proyecto
9. Inspiración del diseño
10. Contacto

---

## 1. Requisitos (hardware / software)
- **PC o Mac** con al menos 8 GB RAM (recomendado 16 GB para emuladores).
- **Android Studio** (recomendado: última versión estable; idealmente Arctic Fox / Bumblebee o superior — usa la versión soportada por tu equipo).  
  Descarga: https://developer.android.com/studio
- **JDK 11 o 17** (Android Studio normalmente instala/gestiona esto).
- Conexión a internet para descargar dependencias y comunicarse con Spotify.
- (Opcional) Un **dispositivo Android físico** con depuración USB activada.

---

## 2. Instalación paso a paso (desde cero)

### A. Instalar Android Studio
1. Descarga Android Studio desde la web oficial e instálalo.
2. Al abrirlo por primera vez, sigue el asistente: instala el SDK, herramientas y un **emulador** (recomendado: Pixel 6 API 33 o similar).
3. Verifica que Android Studio puede compilar un proyecto nuevo (File → New → New Project → Empty Compose Activity → Run).

### B. Clonar el repositorio
Abre una terminal y ejecuta:

```bash
git clone https://github.com/tu-usuario/Soundify.git
cd Soundify
```

### C. Abrir el proyecto en Android Studio

1. Abre **Android Studio**.  
2. En la barra de menú selecciona:  
   **File → Open: selecciona la carpeta Soundify**  
3. Busca y selecciona la carpeta del proyecto llamada **Soundify**.  
4. Espera a que **Gradle sincronice** (esto puede tardar varios minutos la primera vez).  
5. Una vez termine, podrás ejecutar la app o abrir los archivos fuente desde el panel lateral.

---

## 3. Registro y configuración en Spotify Developer Dashboard

### A. Crear app en Spotify  

1. Ve al panel de desarrolladores de Spotify:  
   👉 [https://developer.spotify.com/dashboard/](https://developer.spotify.com/dashboard/)  
   *(necesitas una cuenta Spotify activa).*

2. Haz clic en **Create an App** y asigna un nombre (por ejemplo: `SoundifyApp`).  
3. Copia el **Client ID** — lo necesitarás más adelante para configurar el proyecto.  

4. En la configuración de la app, haz clic en **Edit Settings** y agrega un **Redirect URI**:
Este valor **debe coincidir exactamente** con el que está configurado en tu proyecto Android.

5. Si la app está en modo *Development*, agrega las cuentas que usarás para probar en la sección:  
**Users and Testers** → **Add Users**  
*(esto es necesario para evitar errores 403 al autenticar con cuentas no autorizadas).*

## 4. Configurar el proyecto (variables locales y Gradle)

### A. No subir secretos al repo

Nunca subas `client_secret`, tokens o `local.properties` al repo. Añade estos archivos a `.gitignore` (ya incluido normalmente).

### B. Crear `local.properties` (archivo local, NO subir)

En la raíz del proyecto crea (o edita) `local.properties` y añade:

```properties
# Local properties (NO subir a git)
CLIENT_ID=tu_client_id_aqui
REDIRECT_URI=tu_REDIRECT_URI_aqui
AUTHORIZATION_ENDPOINT=https://accounts.spotify.com/authorize
```

Reemplaza `tu_client_id_aqui` por el Client ID que obtuviste del Dashboard.
Reemplaza `tu_REDIRECT_URI_aqui` por el REDIRECT_URI que configuraste en el Dashboard.

### C. Exponer variables en `BuildConfig`

En `app/build.gradle.kts` antes de:

```kotlin
android {
    defaultConfig {
        ...
    }
}
```

agrega:

```kotlin
val localProps = Properties()
val localFile = rootProject.file("local.properties")
if (localFile.exists()) {
    localProps.load(localFile.inputStream())
}
```

y dentro de:

```kotlin
android {
    defaultConfig {
        ...
    }
}
```

agrega:

```kotlin
buildConfigField("String", "CLIENT_ID", "\"${project.findProperty("CLIENT_ID") ?: ""}\"")
buildConfigField("String", "REDIRECT_URI", "\"${project.findProperty("REDIRECT_URI") ?: ""}\"")
buildConfigField("String", "AUTHORIZATION_ENDPOINT", "\"${project.findProperty("AUTHORIZATION_ENDPOINT") ?: ""}\"")
```

## 5. Ejecutar la app

### A. Usando un emulador

1. Abre Android Studio → **AVD Manager** → crea un emulador (p. ej. Pixel 6, API 33).
2. **Run** → selecciona el emulador y ejecuta la app.

### B. Usando dispositivo físico

1. Activa **Developer options** y **USB debugging** en el dispositivo.
2. Conecta por USB y acepta la depuración.
3. **Run** en Android Studio → selecciona tu dispositivo.

---

## 6. Flujo de autenticación (PKCE) y seguridad de tokens

### A. Resumen del flujo usado

1. Se usa **Authorization Code + PKCE**:
    - La app abre el navegador con URL `/authorize` y parámetros: `client_id`, `response_type=code`, `redirect_uri`, `code_challenge`, `scope`, etc.
    - El usuario inicia sesión y autoriza.
    - Spotify redirige al REDIRECT_URI que definiste `<REDIRECT_URI>?code=ABC...`
    - La app captura el `code` en `onNewIntent`.
2. Se hace **POST /api/token** con `code + code_verifier` para obtener `access_token` y `refresh_token`.

### B. Almacenamiento seguro

- Tokens se guardan cifrados usando **security-crypto (EncryptedSharedPreferences)**.

### C. Manejo del refresh

- El **SpotifyAuthInterceptor** añade `Authorization: Bearer <token>`.
- Si una petición falla con 401, el **SpotifyAuthenticator** hace `refresh_token` (`POST /api/token` grant_type=refresh_token), guarda el nuevo token y reintenta la petición.

---

## 7. Endpoints importantes usados

- `GET /v1/search?q={query}&type=artist&limit={limit}` → buscar artistas.
- `GET /v1/artists/{id}/albums?include_groups=album,single&limit={limit}` → álbumes del artista.
- `GET /v1/albums/{id}/tracks?limit={limit}` → canciones del álbum.
- `POST /api/token` → intercambio de `code` por `access_token` y `refresh_token` (y refresh de tokens).

---

## 8. Estructura del proyecto

La estructura general del proyecto Soundify es la siguiente:

```
app/src/main/java/com/luis/soundify/
│
├── core/ # Contiene codigo aprovechable para todo el proyecto
├── data/ # Contiene la capa de datos
│ ├── entities/ # Clases relacionadas con las respuestas de la API (Modelos de datos)
│ ├── remote/ # Clases relacionadas con Retrofit y llamadas a la API
│ ├── local/ # Almacenamiento seguro (EncryptedSharedPreferences)
│ ├── repository/ # Implementaciones de los repositorios 
│ └── mappers/ # Transformaciones entre modelos de datos y modelos de dominio
│
├── domain/ # Contiene la lógica de negocio
│ ├── models/ # Modelos de datos
│ └── repository/ # Interfaces que abstraen la capa de datos
│
├── presentation/ # Contiene la UI de Jetpack Compose y ViewModels
│ └── screensX/ # Pantallas y composables
│
├── di/ # Módulos de Hilt para inyección de dependencias
│
├── composables/ # Composables generales
│
├── SoundifyApplication.kt # Inicialización de Hilt
└── SpotifyAuthManager.kt # Autenticación de Spotify
```

## 9. Inspiración del diseño

La interfaz gráfica de esta aplicación está inspirada en el diseño de la siguiente propuesta en Figma:

[Musium - Music App UI & Community](https://www.figma.com/design/2Jv2554QofJiOcspngIAD2/Musium---Music-App-UI--Community-)

Se utilizó como referencia para la paleta de colores, tipografía y disposición de elementos, adaptándola al desarrollo con Jetpack Compose.

## 10. Contacto

Si deseas ponerte en contacto conmigo, puedes encontrarme en LinkedIn:

[Luis Miguel Cabral Guzmán](https://www.linkedin.com/in/luismiguelcabralguzman/)

¡Gracias por revisar Soundify! 🎧

